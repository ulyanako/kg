import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.List;

public class RasterPanel extends JPanel {
    private int cellSize = 20; // размер ячейки в пикселях (масштаб)
    private int offsetX = 0;   // смещение центра сетки по X
    private int offsetY = 0;   // смещение центра сетки по Y
    private Point mouseClickPos;

    private List<Point2D> currentPoints = new ArrayList<>();

    public RasterPanel() {
        setBackground(Color.WHITE);
        // Обработка перетаскивания и масштабирования сетки мышью
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mouseClickPos = e.getPoint();
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (mouseClickPos != null) {
                    offsetX += e.getX() - mouseClickPos.x;
                    offsetY += e.getY() - mouseClickPos.y;
                    mouseClickPos = e.getPoint();
                    repaint();
                }
            }
        });

        addMouseWheelListener(new MouseWheelEventPredicate() {
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (e.getWheelRotation() < 0) {
                    cellSize = Math.min(cellSize + 2, 60);
                } else {
                    cellSize = Math.max(cellSize - 2, 6);
                }
                repaint();
            }
        });
    }

    // Пустой адаптер под колесико мыши Java
    private abstract static class MouseWheelEventPredicate extends MouseAdapter {
        @Override
        public abstract void mouseWheelMoved(MouseWheelEvent e);
    }

    public void setPoints(List<Point2D> points) {
        this.currentPoints = points;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        int centerX = width / 2 + offsetX;
        int centerY = height / 2 + offsetY;

        // Рисуем сетку
        g2d.setColor(new Color(230, 230, 230));
        for (int x = centerX % cellSize; x < width; x += cellSize) {
            g2d.drawLine(x, 0, x, height);
        }
        for (int y = centerY % cellSize; y < height; y += cellSize) {
            g2d.drawLine(0, y, width, y);
        }

        // Рисуем оси координат
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(1.5f));
        g2d.drawLine(0, centerY, width, centerY); // Ось X
        g2d.drawLine(centerX, 0, centerX, height); // Ось Y

        // Подписи осей и координатная сетка
        g2d.setFont(new Font("Arial", Font.PLAIN, 10));
        for (int x = centerX % cellSize; x < width; x += cellSize) {
            int gridX = (x - centerX) / cellSize;
            if (gridX != 0) {
                g2d.drawString(String.valueOf(gridX), x - 6, centerY + 12);
            }
        }
        for (int y = centerY % cellSize; y < height; y += cellSize) {
            int gridY = (centerY - y) / cellSize;
            if (gridY != 0) {
                g2d.drawString(String.valueOf(gridY), centerX + 4, y + 4);
            }
        }

        // Отрисовка пикселей алгоритма
        g2d.setColor(new Color(220, 20, 60));
        for (Point2D p : currentPoints) {
            int screenX = centerX + p.getX() * cellSize;
            int screenY = centerY - p.getY() * cellSize; // инверсия по оси Y для декартовой системы

            // Рисуем заполненный квадрат пикселя
            g2d.fillRect(screenX + 1, screenY - cellSize + 1, cellSize - 1, cellSize - 1);
        }
    }
}