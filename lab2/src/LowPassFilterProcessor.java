import java.awt.image.BufferedImage;

public class LowPassFilterProcessor {

    public BufferedImage process(BufferedImage img) {
        int width = img.getWidth();
        int height = img.getHeight();
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        // Сначала копируем всё изображение, чтобы края не были черными
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                result.setRGB(x, y, img.getRGB(x, y));
            }
        }
        // Применяем фильтр для внутренней части
        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                int sumR = 0, sumG = 0, sumB = 0;

                for (int ky = -1; ky <= 1; ky++) {
                    for (int kx = -1; kx <= 1; kx++) {
                        int rgb = img.getRGB(x + kx, y + ky);
                        sumR += (rgb >> 16) & 0xFF;
                        sumG += (rgb >> 8) & 0xFF;
                        sumB += rgb & 0xFF;
                    }
                }

                int avgR = sumR / 9;
                int avgG = sumG / 9;
                int avgB = sumB / 9;

                int newRgb = (avgR << 16) | (avgG << 8) | avgB;
                result.setRGB(x, y, newRgb);
            }
        }
        return result;
    }
}