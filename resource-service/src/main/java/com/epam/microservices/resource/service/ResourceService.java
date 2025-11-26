package com.epam.microservices.resource.service;

import com.epam.microservices.resource.client.SongClient;
import com.epam.microservices.resource.dto.ResourceIdResponse;
import com.epam.microservices.resource.dto.ResourceIdsResponse;
import com.epam.microservices.resource.dto.SongMetadata;
import com.epam.microservices.resource.exception.ResourceServiceException;
import com.epam.microservices.resource.exception.InvalidRequestException;
import com.epam.microservices.resource.exception.ResourceNotFoundException;
import com.epam.microservices.resource.model.Resource;
import com.epam.microservices.resource.repository.ResourceRepository;
import com.epam.microservices.resource.validator.MP3FileValidator;
import com.epam.microservices.resource.validator.SongMetadataValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class ResourceService {

    private final ResourceRepository resourceRepository;
    private final SongClient songClient;
    private final MP3FileValidator mp3FileValidator;
    private final MP3MetadataExtractor metadataExtractor;
    private final SongMetadataValidator songMetadataValidator;

    private static final int CSV_MAX_LENGTH = 200;
    private static final Pattern VALID_ID_PATTERN = Pattern.compile("^\\d+(,\\d+)*$");

    public ResourceIdResponse uploadResource(byte[] fileBytes) {
        mp3FileValidator.validateFile(fileBytes);


        Resource saved = resourceRepository.save(new Resource(null, fileBytes));
        Long resourceId = saved.getId();

        try {
            SongMetadata metadata = metadataExtractor.extractMetadata(fileBytes);
            metadata.setId(resourceId);

            songMetadataValidator.validate(metadata);

            songClient.createSongMetadata(metadata);

            return new ResourceIdResponse(resourceId);
        } catch (Exception e) {
            resourceRepository.deleteById(resourceId);
            throw new ResourceServiceException("Failed to process MP3 file");
        }
    }

    public byte[] getResourceById(Long id) {
        if (id == null || id <= 0) {
            throw new InvalidRequestException("Invalid value '" + id + "' for ID. Must be a positive integer");
        }

        try {
            return resourceRepository.findById(id)
                .map(Resource::getData)
                .orElseThrow(Exception::new);

        } catch (Exception e) {
            throw new ResourceNotFoundException("Resource with ID=" + id + " not found");
        }
    }

    public ResourceIdsResponse deleteResources(String idsString) {
        if (idsString == null || idsString.isEmpty()) {
            throw new InvalidRequestException("ID list cannot be empty");
        }

        if (idsString.length() > CSV_MAX_LENGTH) {
            throw new InvalidRequestException("CSV string is too long: received " +
                idsString.length() + " characters, maximum allowed is " + CSV_MAX_LENGTH);
        }

        if (!VALID_ID_PATTERN.matcher(idsString).matches()) {
            String[] idParts = idsString.split(",");
            for (String idPart : idParts) {
                try {
                    long idValue = Long.parseLong(idPart);
                    if (idValue <= 0) {
                        throw new InvalidRequestException("Invalid ID format: '" + idPart + "'. Only positive integers are allowed");
                    }
                } catch (NumberFormatException e) {
                    throw new InvalidRequestException("Invalid ID format: '" + idPart + "'. Only positive integers are allowed");
                }
            }
            throw new InvalidRequestException("Invalid ID format. IDs must be positive integers separated by commas");
        }

        List<Long> ids = Arrays.stream(idsString.split(","))
                .map(Long::parseLong)
                .toList();

        List<Long> deletedIds = new ArrayList<>();

        try {
            for (Long id : ids) {
                if (resourceRepository.existsById(id)) {
                    resourceRepository.deleteById(id);
                    deletedIds.add(id);
                    songClient.deleteSongsMetadata(Collections.singletonList(id));
                }
            }
        } catch (Exception e) {
            throw new com.epam.microservices.resource.exception.ResourceDeletionException(
                "Failed to delete one or more resources: " + e.getMessage(), e
            );
        }
        
        return new ResourceIdsResponse(deletedIds);
    }
}
