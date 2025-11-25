package com.epam.microservices.song.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SongDto {
    @NotNull(message = "ID must be a positive integer")
    @Positive(message = "ID must be a positive integer")
    private Long id;

    @NotBlank(message = "Name must be between 1 and 100 characters")
    @Size(min = 1, max = 100, message = "Name must be between 1 and 100 characters")
    private String name;

    @NotBlank(message = "Artist must be between 1 and 100 characters")
    @Size(min = 1, max = 100, message = "Artist must be between 1 and 100 characters")
    private String artist;

    @NotBlank(message = "Album must be between 1 and 100 characters")
    @Size(min = 1, max = 100, message = "Album must be between 1 and 100 characters")
    private String album;

    @NotBlank(message = "Duration must be in mm:ss format with leading zeros")
    @Pattern(regexp = "^\\d{2}:[0-5]\\d$", message = "Duration must be in mm:ss format with leading zeros")
    private String duration;
    
    @NotBlank(message = "Year must be between 1900 and 2099")
    @Pattern(regexp = "^(19\\d{2}|20\\d{2})$", message = "Year must be between 1900 and 2099")
    private String year;
}
