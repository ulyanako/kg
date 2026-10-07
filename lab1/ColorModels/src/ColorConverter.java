public class ColorConverter {

    // перевод из RGB в CMYK
    public static double[] rgbToCmyk(int r, int g, int b) {
        double rN = r / 255.0, gN = g / 255.0, bN = b / 255.0;
        double k = 1.0 - Math.max(rN, Math.max(gN, bN));
        if (k == 1.0) return new double[]{0, 0, 0, 100};

        double c = (1.0 - rN - k) / (1.0 - k);
        double m = (1.0 - gN - k) / (1.0 - k);
        double y = (1.0 - bN - k) / (1.0 - k);

        return new double[]{c * 100, m * 100, y * 100, k * 100};
    }

    // перевод из CMYK в RGB
    public static int[] cmykToRgb(double c, double m, double y, double k) {
        c /= 100.0; m /= 100.0; y /= 100.0; k /= 100.0;
        int r = (int) Math.round(255 * (1 - c) * (1 - k));
        int g = (int) Math.round(255 * (1 - m) * (1 - k));
        int b = (int) Math.round(255 * (1 - y) * (1 - k));
        return new int[]{clamp(r), clamp(g), clamp(b)};
    }

    // перевод из RGB в HSL
    public static double[] rgbToHsl(int r, int g, int b) {
        double rN = r / 255.0, gN = g / 255.0, bN = b / 255.0;
        double max = Math.max(rN, Math.max(gN, bN));
        double min = Math.min(rN, Math.min(gN, bN));
        double h = 0, s = 0, l = (max + min) / 2.0;

        if (max != min) {
            double d = max - min;
            s = l > 0.5 ? d / (2.0 - max - min) : d / (max + min);

            if (max == rN) h = (gN - bN) / d + (gN < bN ? 6 : 0);
            else if (max == gN) h = (bN - rN) / d + 2;
            else if (max == bN) h = (rN - gN) / d + 4;
            h /= 6.0;
        }
        return new double[]{h * 360, s * 100, l * 100};
    }

    // перевод из HSL в RGB
    public static int[] hslToRgb(double h, double s, double l) {
        h /= 360.0; s /= 100.0; l /= 100.0;
        double r, g, b;

        if (s == 0) {
            r = g = b = l;
        } else {
            double q = l < 0.5 ? l * (1 + s) : l + s - l * s;
            double p = 2 * l - q;
            r = hueToRgb(p, q, h + 1.0 / 3.0);
            g = hueToRgb(p, q, h);
            b = hueToRgb(p, q, h - 1.0 / 3.0);
        }
        return new int[]{
                clamp((int) Math.round(r * 255)),
                clamp((int) Math.round(g * 255)),
                clamp((int) Math.round(b * 255))
        };
    }

    //вспомогательная функция
    private static double hueToRgb(double p, double q, double t) {
        if (t < 0) t += 1;
        if (t > 1) t -= 1;
        if (t < 1.0 / 6.0) return p + (q - p) * 6 * t;
        if (t < 1.0 / 2.0) return q;
        if (t < 2.0 / 3.0) return p + (q - p) * (2.0 / 3.0 - t) * 6;
        return p;
    }

    //значения каждого цветового канала в RGB не выйдут за пределы
    private static int clamp(int val) {
        return Math.max(0, Math.min(255, val));
    }
}