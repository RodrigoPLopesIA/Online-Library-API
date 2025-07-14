package br.com.rodrigo.onlinelibraryapi.patterns.strategy;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

public class ImageValidationStrategy implements FileValidationStrategy {

    private static final long MAX_IMAGE_SIZE = 5 * 1024 * 1024; // 5MB
    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png", "gif", "bmp", "webp");

    @Override
    public void validate(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("No image uploaded");
        }

        if (file.getSize() > MAX_IMAGE_SIZE) {
            throw new IllegalArgumentException("Image size cannot exceed 5MB");
        }

        String contentType = file.getContentType();
        String filename = file.getOriginalFilename();

        if (contentType == null || !contentType.startsWith("image/") || filename == null) {
            throw new IllegalArgumentException("Invalid image type");
        }

        String ext = filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
        if (!ALLOWED_EXTENSIONS.contains(ext)) {
            throw new IllegalArgumentException("Unsupported image extension");
        }

        try (InputStream is = file.getInputStream()) {
            byte[] bytes = new byte[12];
            if (is.read(bytes) < 8) throw new IllegalArgumentException("Invalid image");

            if (!(isJpeg(bytes) || isPng(bytes) || isGif(bytes) || isBmp(bytes) || isWebp(bytes))) {
                throw new IllegalArgumentException("Unrecognized image format");
            }
        }
    }

    private boolean isJpeg(byte[] bytes) {
        return bytes[0] == (byte) 0xFF && bytes[1] == (byte) 0xD8;
    }

    private boolean isPng(byte[] bytes) {
        return bytes[0] == (byte) 0x89 && bytes[1] == (byte) 0x50 &&
               bytes[2] == (byte) 0x4E && bytes[3] == (byte) 0x47;
    }

    private boolean isGif(byte[] bytes) {
        return bytes[0] == (byte) 0x47 && bytes[1] == (byte) 0x49 && bytes[2] == (byte) 0x46;
    }

    private boolean isBmp(byte[] bytes) {
        return bytes[0] == (byte) 0x42 && bytes[1] == (byte) 0x4D;
    }

    private boolean isWebp(byte[] bytes) {
        return bytes[8] == (byte) 0x57 && bytes[9] == (byte) 0x45 &&
               bytes[10] == (byte) 0x42 && bytes[11] == (byte) 0x50;
    }
}

