import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame implements Observer {
    private final ColorModel model = new ColorModel();

    private final View rgbPanel;
    private final View cmykPanel;
    private final View hslPanel;
    private final JPanel previewPanel = new JPanel();

    public MainFrame() {
        setTitle("Лаб1");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // инициализация панелей
        rgbPanel = new View("RGB", new String[]{"R:", "G:", "B:"}, new int[]{255, 255, 255},
                v -> model.setRgb((int) v[0], (int) v[1], (int) v[2]));

        cmykPanel = new View("CMYK", new String[]{"C (%):", "M (%):", "Y (%):", "K (%):"}, new int[]{100, 100, 100, 100},
                v -> model.setCmyk(v[0], v[1], v[2], v[3]));

        hslPanel = new View("HSL", new String[]{"H (°):", "S (%):", "L (%):"}, new int[]{360, 100, 100},
                v -> model.setHsl(v[0], v[1], v[2]));

        // кнопка выбора цвета из палитры
        JButton colorPickerBtn = new JButton("Выбрать из палитры...");
        colorPickerBtn.addActionListener(e -> {
            Color c = JColorChooser.showDialog(this, "Выбор цвета",
                    new Color(model.getR(), model.getG(), model.getB()));
            if (c != null) model.setRgb(c.getRed(), c.getGreen(), c.getBlue());
        });

        // панель для цвета
        previewPanel.setPreferredSize(new Dimension(150, 60));

        JPanel gridPanel = new JPanel(new GridLayout(2, 2));
        gridPanel.add(rgbPanel);
        gridPanel.add(cmykPanel);
        gridPanel.add(hslPanel);
        gridPanel.add(colorPickerBtn);

        add(gridPanel, BorderLayout.CENTER);
        add(previewPanel, BorderLayout.SOUTH);

        // окно-подписчик модели
        model.subscribe(this);
        model.setRgb(255, 0, 0);

        pack();
        setLocationRelativeTo(null);
    }

    // изменение всех значений при изменении одного из
    @Override
    public void update() {
        int r = model.getR(), g = model.getG(), b = model.getB();

        previewPanel.setBackground(new Color(r, g, b));
        rgbPanel.setValues(new double[]{r, g, b});
        cmykPanel.setValues(ColorConverter.rgbToCmyk(r, g, b));
        hslPanel.setValues(ColorConverter.rgbToHsl(r, g, b));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}