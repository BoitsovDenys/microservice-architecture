package com.epam.microservices.song.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "songs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Song {
    
    @Id
    @EqualsAndHashCode.Include
    private Long id;
    
    @NotBlank(message = "Name is required")
    @Size(min = 1, max = 100, message = "Name must be between 1 and 100 characters")
    private String name;
    
    @NotBlank(message = "Artist is required")
    @Size(min = 1, max = 100, message = "Artist must be between 1 and 100 characters")
    private String artist;
    
    @NotBlank(message = "Album is required")
    @Size(min = 1, max = 100, message = "Album must be between 1 and 100 characters")
    private String album;
    
    @NotBlank(message = "Duration is required")
    @Pattern(regexp = "^\\d{2}:\\d{2}$", message = "Duration must be in mm:ss format with leading zeros (e.g., 03:45)")
    private String duration;
    
    @NotBlank(message = "Year is required")
    @Pattern(regexp = "^(19\\d{2}|20\\d{2})$", message = "Year must be between 1900 and 2099")
    private String year;
}
