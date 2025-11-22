package com.epam.microservices.resource.service;

import com.epam.microservices.resource.dto.SongMetadata;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.springframework.stereotype.Component;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Component
public class MP3MetadataExtractor {

    public SongMetadata extractMetadata(byte[] fileBytes) {
        try {
            Metadata metadata = new Metadata();
            Parser parser = new Mp3Parser();
            ContentHandler handler = new DefaultHandler();
            ParseContext context = new ParseContext();
            
            try (InputStream stream = new ByteArrayInputStream(fileBytes)) {
                parser.parse(stream, handler, metadata, context);
            }
            
            return createSongMetadata(metadata);
        } catch (IOException | SAXException | TikaException e) {
            throw new RuntimeException("Failed to extract metadata: " + e.getMessage(), e);
        }
    }

    private SongMetadata createSongMetadata(Metadata metadata) {
        SongMetadata metadataDto = new SongMetadata();

        String artist = metadata.get("xmpDM:artist");
        String album = metadata.get("xmpDM:album");
        String title = metadata.get("dc:title");
        String duration = metadata.get("xmpDM:duration");
        String year = metadata.get("xmpDM:releaseDate");

        metadataDto.setName(title);
        metadataDto.setArtist(artist);
        metadataDto.setAlbum(album);
        metadataDto.setDuration(formatLength(duration));
        metadataDto.setYear(formatYear(year));
        
        return metadataDto;
    }

    private String formatLength(String lengthInSeconds) {
        if (lengthInSeconds == null || lengthInSeconds.isEmpty()) {
            return "00:00";
        }

        double seconds = Double.parseDouble(lengthInSeconds);
        int totalSecs = (int) Math.round(seconds);
        int minutes = totalSecs / 60;
        int secs = totalSecs % 60;
        return String.format("%02d:%02d", minutes, secs);
    }

    private String formatYear(String year) {
        if (year == null || year.isEmpty()) {
            return null;
        }

        if (year.length() >= 4) {
            return year.substring(0, 4);
        }
        
        return year;
    }
}
