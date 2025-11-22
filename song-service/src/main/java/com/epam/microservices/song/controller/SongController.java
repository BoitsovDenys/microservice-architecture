package com.epam.microservices.song.controller;

import com.epam.microservices.song.dto.SongDto;
import com.epam.microservices.song.dto.SongIdResponse;
import com.epam.microservices.song.dto.SongIdsResponse;
import com.epam.microservices.song.service.SongService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/songs")
public class SongController {
    
    private final SongService songService;
    
    public SongController(SongService songService) {
        this.songService = songService;
    }
    
    @PostMapping
    public ResponseEntity<SongIdResponse> createSong(@Valid @RequestBody SongDto songDto) {
        SongIdResponse response = songService.createSong(songDto);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<SongDto> getSong(@PathVariable Long id) {
        SongDto songDto = songService.getSongById(id);
        return ResponseEntity.ok(songDto);
    }
    
    @DeleteMapping
    public ResponseEntity<SongIdsResponse> deleteSongs(@RequestParam String id) {
        SongIdsResponse response = songService.deleteSongs(id);
        return ResponseEntity.ok(response);
    }
}
