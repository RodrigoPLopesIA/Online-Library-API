package br.com.rodrigo.onlinelibraryapi.services;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import io.minio.BucketExistsArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.http.Method;

@Service
public class StorageService {

    private static final long MAX_IMAGE_SIZE = 5 * 1024 * 1024; // 5MB
    private static final long MAX_DOCUMENT_SIZE = 20 * 1024 * 1024; // 20MB
    
    private static final List<String> ALLOWED_IMAGE_EXTENSIONS = Arrays.asList(
        "jpg", "jpeg", "png", "gif", "bmp", "webp"
    );
    
    private static final List<String> ALLOWED_DOCUMENT_EXTENSIONS = Arrays.asList(
        "pdf", "epub", "mobi"
    );

    @Autowired
    private MinioClient minioClient;

    public void uploadFile(String bucketName, String objectName, 
                         InputStream inputStream, String contentType) {
        try {
            boolean bucketExists = minioClient.bucketExists(
                BucketExistsArgs.builder().bucket(bucketName).build()
            );
            
            if (!bucketExists) {
                minioClient.makeBucket(
                    MakeBucketArgs.builder().bucket(bucketName).build()
                );
            }
            
            minioClient.putObject(
                PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .stream(inputStream, inputStream.available(), -1)
                    .contentType(contentType)
                    .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("Error uploading file: " + e.getMessage(), e);
        }
    }

    public String getFileUrl(String objectName, String bucketName) {
        try {
            return minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                    .method(Method.GET)
                    .bucket(bucketName)
                    .object(objectName)
                    .expiry(7, TimeUnit.DAYS)
                    .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("Error generating file URL", e);
        }
    }

    public String generateFileName(String prefix, String originalFilename) {
        String cleanName = originalFilename.replaceAll("[^a-zA-Z0-9.-]", "_");
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        return String.format("%s_%s_%s", prefix, timestamp, cleanName);
    }

    public void validateFile(MultipartFile file, boolean isDocument) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("No file was uploaded");
        }

        long maxSize = isDocument ? MAX_DOCUMENT_SIZE : MAX_IMAGE_SIZE;
        if (file.getSize() > maxSize) {
            throw new IllegalArgumentException(
                String.format("File size cannot exceed %dMB", maxSize / (1024 * 1024))
            );
        }

        if (isDocument) {
            if (!isValidDocument(file)) {
                throw new IllegalArgumentException(
                    "Unsupported document type. Allowed formats: PDF, EPUB, MOBI"
                );
            }
        } else {
            if (!isValidImage(file)) {
                throw new IllegalArgumentException(
                    "Unsupported image type. Allowed formats: JPEG, PNG, GIF, BMP, WEBP"
                );
            }
        }
    }

    private boolean isValidImage(MultipartFile file) throws IOException {
        String contentType = file.getContentType();
        String originalFilename = file.getOriginalFilename();
        
        if (contentType == null || !contentType.startsWith("image/") || originalFilename == null) {
            return false;
        }

        String extension = originalFilename.substring(
            originalFilename.lastIndexOf(".") + 1
        ).toLowerCase();

        if (!ALLOWED_IMAGE_EXTENSIONS.contains(extension)) {
            return false;
        }

        try (InputStream is = file.getInputStream()) {
            byte[] bytes = new byte[12];
            if (is.read(bytes) < 8) {
                return false;
            }

            return isJpeg(bytes) || isPng(bytes) || isGif(bytes) || 
                   isBmp(bytes) || isWebp(bytes);
        }
    }

    private boolean isValidDocument(MultipartFile file) throws IOException {
        String contentType = file.getContentType();
        String originalFilename = file.getOriginalFilename();
        
        if (contentType == null || originalFilename == null) {
            return false;
        }

        String extension = originalFilename.substring(
            originalFilename.lastIndexOf(".") + 1
        ).toLowerCase();

        if (!ALLOWED_DOCUMENT_EXTENSIONS.contains(extension)) {
            return false;
        }

        try (InputStream is = file.getInputStream()) {
            byte[] bytes = new byte[8];
            if (is.read(bytes) < 4) {
                return false;
            }

            switch (extension) {
                case "pdf":
                    return bytes[0] == '%' && bytes[1] == 'P' && 
                           bytes[2] == 'D' && bytes[3] == 'F';
                case "epub":
                    return bytes[0] == 'P' && bytes[1] == 'K' && 
                           bytes[2] == 0x03 && bytes[3] == 0x04;
                case "mobi":
                    return (bytes[0] == 'B' && bytes[1] == 'O' && 
                            bytes[2] == 'O' && bytes[3] == 'K') ||
                           (bytes[0] == 'T' && bytes[1] == 'E' && 
                            bytes[2] == 'X' && bytes[3] == 'T');
                default:
                    return false;
            }
        }
    }

    // Image signature validation methods
    private boolean isJpeg(byte[] bytes) {
        return bytes[0] == (byte) 0xFF && bytes[1] == (byte) 0xD8;
    }

    private boolean isPng(byte[] bytes) {
        return bytes[0] == (byte) 0x89 && bytes[1] == (byte) 0x50 &&
               bytes[2] == (byte) 0x4E && bytes[3] == (byte) 0x47;
    }

    private boolean isGif(byte[] bytes) {
        return bytes[0] == (byte) 0x47 && bytes[1] == (byte) 0x49 &&
               bytes[2] == (byte) 0x46;
    }

    private boolean isBmp(byte[] bytes) {
        return bytes[0] == (byte) 0x42 && bytes[1] == (byte) 0x4D;
    }

    private boolean isWebp(byte[] bytes) {
        return bytes[8] == (byte) 0x57 && bytes[9] == (byte) 0x45 &&
               bytes[10] == (byte) 0x42 && bytes[11] == (byte) 0x50;
    }
}