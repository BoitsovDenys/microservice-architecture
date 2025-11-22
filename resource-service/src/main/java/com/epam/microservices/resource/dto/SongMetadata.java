package com.epam.microservices.resource.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SongMetadata {
    private Long id;
    private String name;
    private String artist;
    private String album;
    private String duration;
    private String year;
}
