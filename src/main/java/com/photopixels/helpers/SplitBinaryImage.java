package com.photopixels.helpers;

import java.io.*;

public class SplitBinaryImage {

    /**
     * Splits a binary file (like an image) into two parts.
     *
     * @param inputPath   the path to the original image
     * @param outputPath1 the path to save the first half
     * @param outputPath2 the path to save the second half
     * @throws IOException if reading/writing fails
     */
    public void splitBinaryFile(String inputPath, String outputPath1, String outputPath2) {
        try {
            File file = new File(inputPath);
            if (!file.exists()) {
                throw new FileNotFoundException("File not found at: " + inputPath);
            }

            byte[] fileData = new byte[(int) file.length()];
            try (FileInputStream fis = new FileInputStream(file)) {
                int readBytes = fis.read(fileData);
                if (readBytes != fileData.length) {
                    throw new IOException("Incomplete file read.");
                }
            }

            // Split into two parts
            int mid = fileData.length / 2;
            byte[] part1 = new byte[mid];
            byte[] part2 = new byte[fileData.length - mid];

            System.arraycopy(fileData, 0, part1, 0, mid);
            System.arraycopy(fileData, mid, part2, 0, part2.length);

            // Write both parts to separate files
            try (FileOutputStream out1 = new FileOutputStream(outputPath1);
                 FileOutputStream out2 = new FileOutputStream(outputPath2)) {
                out1.write(part1);
                out2.write(part2);
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to split binary file: " + e.getMessage(), e);
        }
    }
}