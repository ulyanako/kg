import java.util.ArrayList;
import java.util.List;

public class RasterAlgorithms {

    public static class ExecutionResult {
        private final List<Point2D> points;
        private final long executionTimeNs;

        public ExecutionResult(List<Point2D> points, long executionTimeNs) {
            this.points = points;
            this.executionTimeNs = executionTimeNs;
        }

        public List<Point2D> getPoints() { return points; }
        public long getExecutionTimeNs() { return executionTimeNs; }
    }

    //Пошаговый алгоритм
    public static ExecutionResult stepByStep(int x0, int y0, int x1, int y1) {
        long startTime = System.nanoTime();
        List<Point2D> points = new ArrayList<>();

        int dx = x1 - x0;
        int dy = y1 - y0;

        if (Math.abs(dx) >= Math.abs(dy)) {
            if (x0 > x1) {
                int tx = x0; x0 = x1; x1 = tx;
                int ty = y0; y0 = y1; y1 = ty;
            }
            float k = (float) dy / (dx == 0 ? 1 : dx);
            float y = y0;
            for (int x = x0; x <= x1; x++) {
                points.add(new Point2D(x, Math.round(y)));
                y += k;
            }
        } else {
            if (y0 > y1) {
                int tx = x0; x0 = x1; x1 = tx;
                int ty = y0; y0 = y1; y1 = ty;
            }
            float k = (float) dx / (dy == 0 ? 1 : dy);
            float x = x0;
            for (int y = y0; y <= y1; y++) {
                points.add(new Point2D(Math.round(x), y));
                x += k;
            }
        }

        long endTime = System.nanoTime();
        return new ExecutionResult(points, endTime - startTime);
    }

    // Алгоритм ЦДА
    public static ExecutionResult dda(int x0, int y0, int x1, int y1) {
        long startTime = System.nanoTime();
        List<Point2D> points = new ArrayList<>();

        int dx = x1 - x0;
        int dy = y1 - y0;
        int steps = Math.max(Math.abs(dx), Math.abs(dy));

        if (steps == 0) {
            points.add(new Point2D(x0, y0));
            return new ExecutionResult(points, System.nanoTime() - startTime);
        }

        float xInc = (float) dx / steps;
        float yInc = (float) dy / steps;
        float x = x0;
        float y = y0;

        for (int i = 0; i <= steps; i++) {
            points.add(new Point2D(Math.round(x), Math.round(y)));
            x += xInc;
            y += yInc;
        }

        long endTime = System.nanoTime();
        return new ExecutionResult(points, endTime - startTime);
    }

    // Алгоритм Брезенхема (отрезок)
    public static ExecutionResult bresenhamLine(int x0, int y0, int x1, int y1) {
        long startTime = System.nanoTime();
        List<Point2D> points = new ArrayList<>();

        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);
        int sx = x0 < x1 ? 1 : -1;
        int sy = y0 < y1 ? 1 : -1;
        int err = dx - dy;

        while (true) {
            points.add(new Point2D(x0, y0));
            if (x0 == x1 && y0 == y1) break;
            int e2 = 2 * err;
            if (e2 > -dy) {
                err -= dy;
                x0 += sx;
            }
            if (e2 < dx) {
                err += dx;
                y0 += sy;
            }
        }

        long endTime = System.nanoTime();
        return new ExecutionResult(points, endTime - startTime);
    }

    //Алгоритм Брезенхема (окружность)
    public static ExecutionResult bresenhamCircle(int xc, int yc, int r) {
        long startTime = System.nanoTime();
        List<Point2D> points = new ArrayList<>();

        int x = 0;
        int y = r;
        int d = 3 - 2 * r;

        addCirclePoints(points, xc, yc, x, y);

        while (y >= x) {
            x++;
            if (d > 0) {
                y--;
                d = d + 4 * (x - y) + 10;
            } else {
                d = d + 4 * x + 6;
            }
            addCirclePoints(points, xc, yc, x, y);
        }

        long endTime = System.nanoTime();
        return new ExecutionResult(points, endTime - startTime);
    }

    private static void addCirclePoints(List<Point2D> points, int xc, int yc, int x, int y) {
        points.add(new Point2D(xc + x, yc + y));
        points.add(new Point2D(xc - x, yc + y));
        points.add(new Point2D(xc + x, yc - y));
        points.add(new Point2D(xc - x, yc - y));
        points.add(new Point2D(xc + y, yc + x));
        points.add(new Point2D(xc - y, yc + x));
        points.add(new Point2D(xc + y, yc - x));
        points.add(new Point2D(xc - y, yc - x));
    }
}