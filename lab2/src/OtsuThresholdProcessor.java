import java.awt.image.BufferedImage;

public class OtsuThresholdProcessor {

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
        int totalPixels = width * height;

        // Построение гистограммы
        int[] hist = new int[256];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                hist[gray.getRaster().getSample(x, y, 0)]++;
            }
        }

        // Построение нормализованной гистограммы h(i), где сумма h(i) = 1
        double[] p = new double[256];
        for (int i = 0; i < 256; i++) {
            p[i] = (double) hist[i] / totalPixels;
        }

        double maxVariance = -1;
        int optimalT = 0;

        // 2. Вычисление межклассовой дисперсии S(T) для каждого порога T
        for (int T = 0; T < 256; T++) {
            double p1 = 0;
            for (int i = 0; i <= T; i++) {
                p1 += p[i];
            }
            double p2 = 1.0 - p1;

            if (p1 == 0 || p2 == 0) continue;

            double m1 = 0;
            for (int i = 0; i <= T; i++) {
                m1 += i * p[i];
            }
            m1 /= p1;

            double m2 = 0;
            for (int i = T + 1; i < 256; i++) {
                m2 += i * p[i];
            }
            m2 /= p2;

            // Межклассовая дисперсия S(T)
            double variance = p1 * p2 * Math.pow(m1 - m2, 2);
            if (variance > maxVariance) {
                maxVariance = variance;
                optimalT = T; // Максимизация S(T)
            }
        }

        // Бинаризация изображения по найденному оптимальному порогу
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int val = gray.getRaster().getSample(x, y, 0);
                int newVal = (val > optimalT) ? 255 : 0;
                result.getRaster().setSample(x, y, 0, newVal);
            }
        }
        return result;
    }
}