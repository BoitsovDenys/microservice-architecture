package com.epam.microservices.song.service;

import com.epam.microservices.song.dto.SongDto;
import com.epam.microservices.song.dto.SongIdResponse;
import com.epam.microservices.song.dto.SongIdsResponse;
import com.epam.microservices.song.exception.InvalidRequestException;
import com.epam.microservices.song.exception.SongNotFoundException;
import com.epam.microservices.song.exception.SongAlreadyExistsException;
import com.epam.microservices.song.model.Song;
import com.epam.microservices.song.repository.SongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class SongService {
    private final SongRepository songRepository;
    
    private static final int CSV_MAX_LENGTH = 200;
    private static final Pattern VALID_ID_PATTERN = Pattern.compile("^\\d+(,\\d+)*$");
    
    public SongIdResponse createSong(SongDto songDto) {
        if (songRepository.existsById(songDto.getId())) {
            throw new SongAlreadyExistsException("Metadata for resource ID=" + songDto.getId() + " already exists");
        }
        
        Song song = new Song(
            songDto.getId(),
            songDto.getName(),
            songDto.getArtist(),
            songDto.getAlbum(),
            songDto.getDuration(),
            songDto.getYear()
        );
    
        Song savedSong = songRepository.save(song);
        return new SongIdResponse(savedSong.getId());
    }
    
    public SongDto getSongById(Long id) {
        if (id == null || id <= 0) {
            throw new InvalidRequestException("Invalid ID format: '" + id + "'. Only positive integers are allowed");
        }
    
        Song song = songRepository.findById(id)
            .orElseThrow(() -> new SongNotFoundException("Song metadata for ID=" + id + " not found"));

        return new SongDto(
            song.getId(),
            song.getName(),
            song.getArtist(),
            song.getAlbum(),
            song.getDuration(),
            song.getYear()
        );
    }
    
    public SongIdsResponse deleteSongs(String idsString) {
        if (idsString == null || idsString.isEmpty()) {
            throw new InvalidRequestException("ID list cannot be empty");
        }
    
        if (idsString.length() > CSV_MAX_LENGTH) {
            throw new InvalidRequestException("CSV string is too long: received " + idsString.length() + " characters, maximum allowed is " + CSV_MAX_LENGTH);
        }
    
        if (!VALID_ID_PATTERN.matcher(idsString).matches()) {
            // We need to identify which exact value is invalid to match the spec
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
    
        for (Long id : ids) {
            if (songRepository.existsById(id)) {
                songRepository.deleteById(id);
                deletedIds.add(id);
            }
        }
        
        return new SongIdsResponse(deletedIds);
    }
}
