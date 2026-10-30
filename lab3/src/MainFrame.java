import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private final RasterPanel rasterPanel;
    private final JComboBox<String> algorithmComboBox;
    private final JTextField tfX0, tfY0, tfX1, tfY1;
    private final JLabel lblTime;

    public MainFrame() {
        setTitle("Лаб3");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        rasterPanel = new RasterPanel();
        add(rasterPanel, BorderLayout.CENTER);

        // Панель управления снизу
        JPanel controlPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Первая строка управления (Выбор алгоритма и поля ввода)
        JPanel inputsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));

        algorithmComboBox = new JComboBox<>(new String[]{
                "Пошаговый алгоритм",
                "Алгоритм ЦДА",
                "Брезенхем (отрезок)",
                "Брезенхем (окружность: x0,y0 - центр, x1 - радиус)"
        });

        tfX0 = new JTextField("0", 4);
        tfY0 = new JTextField("0", 4);
        tfX1 = new JTextField("15", 4);
        tfY1 = new JTextField("10", 4);

        JButton btnRun = new JButton("Выполнить");

        inputsPanel.add(new JLabel("Алгоритм:"));
        inputsPanel.add(algorithmComboBox);
        inputsPanel.add(new JLabel("X0/Xc:")); inputsPanel.add(tfX0);
        inputsPanel.add(new JLabel("Y0/Yc:")); inputsPanel.add(tfY0);
        inputsPanel.add(new JLabel("X1/Радиус:")); inputsPanel.add(tfX1);
        inputsPanel.add(new JLabel("Y1:")); inputsPanel.add(tfY1);
        inputsPanel.add(btnRun);

        // Вторая строка управления (Время выполнения и подсказки)
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        lblTime = new JLabel("Время выполнения: -");
        infoPanel.add(lblTime);

        controlPanel.add(inputsPanel);
        controlPanel.add(infoPanel);

        add(controlPanel, BorderLayout.SOUTH);

        // Обработка нажатия кнопки
        btnRun.addActionListener(e -> executeAlgorithm());
    }

    private void executeAlgorithm() {
        try {
            int x0 = Integer.parseInt(tfX0.getText().trim());
            int y0 = Integer.parseInt(tfY0.getText().trim());
            int x1 = Integer.parseInt(tfX1.getText().trim());
            int y1 = Integer.parseInt(tfY1.getText().trim());

            int selectedIndex = algorithmComboBox.getSelectedIndex();
            RasterAlgorithms.ExecutionResult result = null;

            switch (selectedIndex) {
                case 0:
                    result = RasterAlgorithms.stepByStep(x0, y0, x1, y1);
                    break;
                case 1:
                    result = RasterAlgorithms.dda(x0, y0, x1, y1);
                    break;
                case 2:
                    result = RasterAlgorithms.bresenhamLine(x0, y0, x1, y1);
                    break;
                case 3:
                    // Для окружности x1 трактуется как радиус
                    result = RasterAlgorithms.bresenhamCircle(x0, y0, Math.abs(x1));
                    break;
            }

            if (result != null) {
                rasterPanel.setPoints(result.getPoints());
                lblTime.setText(String.format("Время выполнения: %.4f мс",
                        result.getExecutionTimeNs() / 1_000_000.0));
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Пожалуйста, введите корректные целочисленные координаты.",
                    "Ошибка ввода", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }
}