package com.epam.microservices.song.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SongIdsResponse {
    private List<Long> ids;
}
