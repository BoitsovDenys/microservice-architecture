package com.epam.microservices.resource.client;

import com.epam.microservices.resource.dto.SongMetadata;
import com.epam.microservices.resource.exception.SongServiceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;

@Component
public class SongClient {
    
    private final WebClient webClient;
    
    public SongClient(WebClient.Builder webClientBuilder, @Value("${song-service.url}") String songServiceUrl) {
        this.webClient = webClientBuilder.baseUrl(songServiceUrl).build();
    }
    
    public void createSongMetadata(SongMetadata songMetadata) {
        try {
            webClient.post()
                    .uri("/songs")
                    .bodyValue(songMetadata)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();
        } catch (WebClientResponseException e) {
            throw new SongServiceException("Failed to create song metadata: " + e.getMessage() +
                    ", Status: " + e.getStatusCode() + ", Response: " + e.getResponseBodyAsString());
        } catch (Exception e) {
            throw new SongServiceException("Unexpected error calling Song Service: " + e.getMessage());
        }
    }
    
    public void deleteSongsMetadata(List<Long> resourceIds) {
        try {
            String idsParam = String.join(",", resourceIds.stream().map(Object::toString).toList());
            
            webClient.delete()
                    .uri(uriBuilder -> uriBuilder.path("/songs")
                            .queryParam("id", idsParam)
                            .build())
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();
        } catch (WebClientResponseException e) {
            if (e.getStatusCode() != HttpStatus.NOT_FOUND) {
                throw new SongServiceException("Failed to delete songs: " + e.getMessage());
            }
        } catch (Exception e) {
            throw new SongServiceException("Unexpected error deleting songs: " + e.getMessage());
        }
    }
}
