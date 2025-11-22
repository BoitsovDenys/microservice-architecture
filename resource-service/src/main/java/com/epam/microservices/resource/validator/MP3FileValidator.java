package com.epam.microservices.resource.validator;

import com.epam.microservices.resource.exception.EmptyFileException;
import com.epam.microservices.resource.exception.InvalidMP3FileException;
import org.apache.tika.Tika;
import org.springframework.stereotype.Component;

@Component
public class MP3FileValidator {

    public void validateFile(byte[] fileBytes) {
        if (fileBytes == null || fileBytes.length == 0) {
            throw new EmptyFileException("File is empty");
        }

        try {
            Tika tika = new Tika();
            String detectedType = tika.detect(fileBytes);
            if (!detectedType.equals("audio/mpeg")) {
                throw new InvalidMP3FileException("Invalid MP3 file format. Detected: " + detectedType);
            }
        } catch (Exception e) {
            throw new InvalidMP3FileException("Error validating MP3 file: " + e.getMessage());
        }
    }
}
