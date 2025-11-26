package com.epam.microservices.resource.validator;

import com.epam.microservices.resource.constants.MediaTypeConstants;
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

        Tika tika = new Tika();
        String detectedType = tika.detect(fileBytes);
        if (!detectedType.equals(MediaTypeConstants.AUDIO_MPEG)) {
            throw new InvalidMP3FileException("Invalid file format: " + detectedType + ". Only MP3 files are allowed");
        }
    }
}
