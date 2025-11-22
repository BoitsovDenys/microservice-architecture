package com.epam.microservices.resource.validator;

import com.epam.microservices.resource.dto.SongMetadata;
import com.epam.microservices.resource.exception.InvalidMP3FileException;
import org.springframework.stereotype.Component;

@Component
public class SongMetadataValidator {

    public void validate(SongMetadata metadata) {
        if (metadata.getName() == null || metadata.getName().isEmpty()) {
            throw new InvalidMP3FileException("Missing required metadata: title");
        }
        if (metadata.getArtist() == null || metadata.getArtist().isEmpty()) {
            throw new InvalidMP3FileException("Missing required metadata: artist");
        }
        if (metadata.getAlbum() == null || metadata.getAlbum().isEmpty()) {
            throw new InvalidMP3FileException("Missing required metadata: album");
        }

        if (metadata.getName().length() > 100 || metadata.getArtist().length() > 100 ||
            metadata.getAlbum().length() > 100) {
            throw new InvalidMP3FileException("Text fields (name, artist, album) exceed maximum length of 100 characters.");
        }

        if (metadata.getDuration() == null || !metadata.getDuration().matches("^\\d{2}:\\d{2}$")) {
            throw new InvalidMP3FileException("Invalid duration format: " + metadata.getDuration() +
                ". Must be mm:ss with leading zeros.");
        }

        if (metadata.getYear() == null || !metadata.getYear().matches("^(19\\d{2}|20\\d{2})$")) {
            throw new InvalidMP3FileException("Invalid year format: " + metadata.getYear() +
                ". Must be between 1900-2099.");
        }
    }
}
