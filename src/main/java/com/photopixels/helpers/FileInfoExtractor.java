package com.photopixels.helpers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class FileInfoExtractor {

    public Map<String, String> extractFileInfo(Path path) {
        File file = path.toFile();
        if (!file.exists()) {
            throw new RuntimeException("!!! File not found: " + path);
        }

        try {
            String fileExtension = getFileExtension(file);
            String fileName = file.getName();
            String fileHash = getFileHash(file);
            long fileSize = file.length();

            Map<String, String> fileInfo = new HashMap<>();
            fileInfo.put("fileExtension", base64(fileExtension));
            fileInfo.put("fileName", base64(fileName));
            fileInfo.put("fileHash", fileHash);
            fileInfo.put("fileHashBase64", base64(fileHash));
            fileInfo.put("fileSize", base64(Long.toString(fileSize)));
            fileInfo.put("fileSizeBytes", Long.toString(fileSize));

            return fileInfo;
        } catch (IOException | NoSuchAlgorithmException e) {
            throw new RuntimeException("Error extracting file info", e);
        }
    }

    private static String getFileExtension(File file) {
        String fileName = file.getName();
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
    }

    private static String getFileHash(File file) throws NoSuchAlgorithmException, IOException {
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        FileInputStream fis = new FileInputStream(file);
        byte[] buffer = new byte[8192];
        int bytesRead;

        while ((bytesRead = fis.read(buffer)) != -1) {
            digest.update(buffer, 0, bytesRead);
        }

        fis.close();

        byte[] hashBytes = digest.digest();
        return Base64.getEncoder().encodeToString(hashBytes);
    }

    private static String base64(String input) {
        return Base64.getEncoder().encodeToString(input.getBytes());
    }

    public long getFileSize(Path file) {
        try {
            return Files.size(file);
        } catch (IOException e) {
            throw new RuntimeException("Failed to get file size for: " + file, e);
        }
    }

}
