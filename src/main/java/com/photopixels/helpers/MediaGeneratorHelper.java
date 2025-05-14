package com.photopixels.helpers;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class MediaGeneratorHelper {
    private final String filePath;
    private final int width;
    private final int height;

    public MediaGeneratorHelper(String filePath, int width, int height) {
        this.filePath = filePath;
        this.width = width;
        this.height = height;
    }

    public void createLargeImage() throws IOException {
        File directory = new File(filePath).getParentFile();
        if (!directory.exists()) {
            directory.mkdirs();
        }

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Random random = new Random();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int r = random.nextInt(256);
                int g = random.nextInt(256);
                int b = random.nextInt(256);
                image.setRGB(x, y, (255 << 24) | (r << 16) | (g << 8) | b); // Random RGB with full opacity
            }
        }

        File output = new File(filePath);
        ImageIO.write(image, "png", output);
        long fileSize = output.length();
        if (fileSize < 1_182_079_795L) {
            throw new IOException("Generated image is too small: " + fileSize + " bytes");
        }
    }

    public void deleteLargeImage() {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
    }

    public String getFilePath() {
        return new File(filePath).getAbsolutePath();
    }
}