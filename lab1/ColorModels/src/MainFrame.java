import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private double currentR = 1.0;
    private double currentG = 0.0;
    private double currentB = 0.0;

    private boolean isUpdating = false;

    private final JPanel previewPanel;
    private final JButton btnPalette;

    private final ColorModelPanel cmykPanel;
    private final ColorModelPanel rgbPanel;
    private final ColorModelPanel hlsPanel;

    public MainFrame() {
        setTitle("Лаб1");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(950, 480);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel centerPanel = new JPanel(new GridLayout(2, 2));

        cmykPanel = new ColorModelPanel(new String[]{"C (%):", "M (%):", "Y (%):", "K (%):"},
                new int[]{100, 100, 100, 100});

        rgbPanel = new ColorModelPanel(new String[]{"R:", "G:", "B:"},
                new int[]{255, 255, 255});

        hlsPanel = new ColorModelPanel(new String[]{"H (°):", "L (%):", "S (%):"},
                new int[]{360, 100, 100});

        btnPalette = new JButton("Выбрать из палитры...");

        centerPanel.add(cmykPanel);
        centerPanel.add(rgbPanel);
        centerPanel.add(hlsPanel);
        centerPanel.add(btnPalette);

        add(centerPanel, BorderLayout.CENTER);
        previewPanel = new JPanel();
        previewPanel.setPreferredSize(new Dimension(0, 50));
        //previewPanel.setOpaque(true);
        add(previewPanel, BorderLayout.SOUTH);

        setupEvents();

        updateFromRgb(1.0, 0.0, 0.0, null);
    }

    private void setupEvents() {
        btnPalette.addActionListener(e -> {
            Color initial = new Color((float) currentR, (float) currentG, (float) currentB);
            Color chosen = JColorChooser.showDialog(this, "Выберите цвет из палитры", initial);
            if (chosen != null) {
                updateFromRgb(chosen.getRed() / 255.0,
                        chosen.getGreen() / 255.0,
                        chosen.getBlue() / 255.0, null);
            }
        });

        ColorModelPanel.ColorChangeListener changeListener = source -> {
            if (isUpdating) return;

            if (source == cmykPanel) {
                double[] vals = cmykPanel.getValues();
                double[] rgb = ColorUtils.cmykToRgb(
                        vals[0] / 100.0, vals[1] / 100.0, vals[2] / 100.0, vals[3] / 100.0);
                updateFromRgb(rgb[0], rgb[1], rgb[2], source);
            } else if (source == rgbPanel) {
                double[] vals = rgbPanel.getValues();
                updateFromRgb(vals[0] / 255.0, vals[1] / 255.0, vals[2] / 255.0, source);
            } else if (source == hlsPanel) {
                double[] vals = hlsPanel.getValues();
                double[] rgb = ColorUtils.hlsToRgb(vals[0], vals[1] / 100.0, vals[2] / 100.0);
                updateFromRgb(rgb[0], rgb[1], rgb[2], source);
            }
        };

        cmykPanel.setColorChangeListener(changeListener);
        rgbPanel.setColorChangeListener(changeListener);
        hlsPanel.setColorChangeListener(changeListener);
    }

    private void updateFromRgb(double r, double g, double b, ColorModelPanel source) {
        this.currentR = Math.min(1.0, Math.max(0.0, r));
        this.currentG = Math.min(1.0, Math.max(0.0, g));
        this.currentB = Math.min(1.0, Math.max(0.0, b));

        previewPanel.setBackground(new Color((float) currentR, (float) currentG, (float) currentB));

        isUpdating = true;

        if (source != cmykPanel) {
            double[] cmyk = ColorUtils.rgbToCmyk(currentR, currentG, currentB);
            cmykPanel.setValues(new double[]{
                    cmyk[0] * 100.0, cmyk[1] * 100.0, cmyk[2] * 100.0, cmyk[3] * 100.0});
        }
        if (source != rgbPanel) {
            rgbPanel.setValues(new double[]{
                    currentR * 255.0, currentG * 255.0, currentB * 255.0});
        }
        if (source != hlsPanel) {
            double[] hls = ColorUtils.rgbToHls(currentR, currentG, currentB);
            hlsPanel.setValues(new double[]{
                    hls[0], hls[1] * 100.0, hls[2] * 100.0});
        }

        isUpdating = false;
    }
}