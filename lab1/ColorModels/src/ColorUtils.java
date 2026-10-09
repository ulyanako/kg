public class ColorUtils {
    public static double[] rgbToCmyk(double r, double g, double b) {
        double k = 1.0 - Math.max(r, Math.max(g, b));
        if (k == 1.0) {
            return new double[]{0, 0, 0, 1.0};
        }
        double c = (1.0 - r - k) / (1.0 - k);
        double m = (1.0 - g - k) / (1.0 - k);
        double y = (1.0 - b - k) / (1.0 - k);
        return new double[]{c, m, y, k};
    }

    public static double[] cmykToRgb(double c, double m, double y, double k) {
        double r = (1.0 - c) * (1.0 - k);
        double g = (1.0 - m) * (1.0 - k);
        double b = (1.0 - y) * (1.0 - k);
        return new double[]{r, g, b};
    }

    public static double[] rgbToHls(double r, double g, double b) {
        double max = Math.max(r, Math.max(g, b));
        double min = Math.min(r, Math.min(g, b));
        double delta = max - min;

        double l = (max + min) / 2.0;
        double h = 0;
        double s = 0;

        if (delta != 0) {
            s = (l <= 0.5) ? (delta / (max + min)) : (delta / (2.0 - max - min));

            if (max == r) {
                h = 60 * (((g - b) / delta) % 6);
            } else if (max == g) {
                h = 60 * (((b - r) / delta) + 2);
            } else {
                h = 60 * (((r - g) / delta) + 4);
            }
        }
        if (h < 0) h += 360;

        return new double[]{h, l, s};
    }

    public static double[] hlsToRgb(double h, double l, double s) {
        double c = (1 - Math.abs(2 * l - 1)) * s;
        double x = c * (1 - Math.abs((h / 60.0) % 2 - 1));
        double m = l - c / 2.0;

        double r1 = 0, g1 = 0, b1 = 0;
        if (h >= 0 && h < 60) { r1 = c; g1 = x; b1 = 0; }
        else if (h >= 60 && h < 120) { r1 = x; g1 = c; b1 = 0; }
        else if (h >= 120 && h < 180) { r1 = 0; g1 = c; b1 = x; }
        else if (h >= 180 && h < 240) { r1 = 0; g1 = x; b1 = c; }
        else if (h >= 240 && h < 300) { r1 = x; g1 = 0; b1 = c; }
        else if (h >= 300 && h < 360) { r1 = c; g1 = 0; b1 = x; }

        return new double[]{r1 + m, g1 + m, b1 + m};
    }
}