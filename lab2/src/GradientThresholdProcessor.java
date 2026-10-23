import java.awt.image.BufferedImage;

public class GradientThresholdProcessor {

    private BufferedImage toGrayscale(BufferedImage img) {
        BufferedImage grayImg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        java.awt.Graphics g = grayImg.getGraphics();
        g.drawImage(img, 0, 0, null);
        g.dispose();
        return grayImg;
    }

    public BufferedImage process(BufferedImage img) {
        BufferedImage gray = toGrayscale(img);
        int width = gray.getWidth();
        int height = gray.getHeight();

        double numerator = 0;
        double denominator = 0;

        // Вычисление градиента G(x, y) и порога t
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int f = gray.getRaster().getSample(x, y, 0);

                // Ограничиваем координаты во избежание выхода за границы при x+1 и y+1
                int fx1 = (x + 1 < width) ? gray.getRaster().getSample(x + 1, y, 0) : f;
                int fy1 = (y + 1 < height) ? gray.getRaster().getSample(x, y + 1, 0) : f;

                // Градиенты Gx и Gy
                int gx = fx1 - f;
                int gy = fy1 - f;

                // G(x, y) = max(|Gx|, |Gy|)
                double gxy = Math.max(Math.abs(gx), Math.abs(gy));

                numerator += (double) f * gxy;
                denominator += gxy;
            }
        }

        // Вычисление итогового порогового значения t
        int threshold = (denominator == 0) ? 128 : (int) Math.round(numerator / denominator);

        // Бинаризация изображения
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int val = gray.getRaster().getSample(x, y, 0);
                int newVal = (val > threshold) ? 255 : 0;
                result.getRaster().setSample(x, y, 0, newVal);
            }
        }
        return result;
    }
}