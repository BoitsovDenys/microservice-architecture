package com.epam.microservices.resource.controller;

import com.epam.microservices.resource.dto.ResourceIdResponse;
import com.epam.microservices.resource.dto.ResourceIdsResponse;
import com.epam.microservices.resource.service.ResourceService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/resources")
public class ResourceController {

    private final ResourceService resourceService;

    public ResourceController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @PostMapping
    public ResponseEntity<ResourceIdResponse> uploadResource(@RequestBody byte[] fileBytes) {
        ResourceIdResponse response = resourceService.uploadResource(fileBytes);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getResource(@PathVariable Long id) {
        byte[] resourceData = resourceService.getResourceById(id);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("audio/mpeg"))
                .body(resourceData);
    }

    @DeleteMapping
    public ResponseEntity<ResourceIdsResponse> deleteResources(@RequestParam String id) {
        ResourceIdsResponse response = resourceService.deleteResources(id);
        return ResponseEntity.ok(response);
    }
}
