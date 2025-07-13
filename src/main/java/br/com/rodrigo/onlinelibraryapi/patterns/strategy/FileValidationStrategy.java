package br.com.rodrigo.onlinelibraryapi.patterns.strategy;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface FileValidationStrategy {
    void validate(MultipartFile file) throws IOException;
}
