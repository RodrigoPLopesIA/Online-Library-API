package br.com.rodrigo.onlinelibraryapi.patterns.strategy;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

public class DocumentValidationStrategy implements FileValidationStrategy {

    private static final long MAX_DOCUMENT_SIZE = 20 * 1024 * 1024; // 20MB
    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList("pdf", "epub", "mobi");

    @Override
    public void validate(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("No document uploaded");
        }

        if (file.getSize() > MAX_DOCUMENT_SIZE) {
            throw new IllegalArgumentException("Document size cannot exceed 20MB");
        }

        String contentType = file.getContentType();
        String filename = file.getOriginalFilename();

        if (contentType == null || filename == null) {
            throw new IllegalArgumentException("Invalid document");
        }

        String ext = filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
        if (!ALLOWED_EXTENSIONS.contains(ext)) {
            throw new IllegalArgumentException("Unsupported document extension");
        }

        try (InputStream is = file.getInputStream()) {
            byte[] bytes = new byte[8];
            if (is.read(bytes) < 4) throw new IllegalArgumentException("Invalid document content");

            switch (ext) {
                case "pdf":
                    if (!(bytes[0] == '%' && bytes[1] == 'P' && bytes[2] == 'D' && bytes[3] == 'F'))
                        throw new IllegalArgumentException("Invalid PDF file");
                    break;
                case "epub":
                    if (!(bytes[0] == 'P' && bytes[1] == 'K' && bytes[2] == 0x03 && bytes[3] == 0x04))
                        throw new IllegalArgumentException("Invalid EPUB file");
                    break;
                case "mobi":
                    if (!((bytes[0] == 'B' && bytes[1] == 'O' && bytes[2] == 'O' && bytes[3] == 'K') ||
                          (bytes[0] == 'T' && bytes[1] == 'E' && bytes[2] == 'X' && bytes[3] == 'T')))
                        throw new IllegalArgumentException("Invalid MOBI file");
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported document format");
            }
        }
    }
}

