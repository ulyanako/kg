import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class View extends JPanel {
    private final JSlider[] sliders;
    private final JTextField[] fields;
    private final String[] labels;
    private boolean isUpdating = false;

    public View(String title, String[] labels, int[] maxValues, Consumer<double[]> onChange) {
        this.labels = labels;
        int count = labels.length;
        sliders = new JSlider[count];
        fields = new JTextField[count];

        setLayout(new GridBagLayout());

        for (int i = 0; i < count; i++) {
            final int index = i;
            sliders[i] = new JSlider(0, maxValues[i], 0);
            fields[i] = new JTextField(3);

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(4, 4, 4, 4);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            gbc.gridx = 0; gbc.gridy = i; gbc.weightx = 0;
            add(new JLabel(labels[i]), gbc);

            gbc.gridx = 1; gbc.weightx = 1.0;
            add(sliders[i], gbc);

            gbc.gridx = 2; gbc.weightx = 0;
            add(fields[i], gbc);

            // добавляем слушаетеля на слайдеры
            sliders[i].addChangeListener(e -> {
                if (!isUpdating) {
                    fields[index].setText(String.valueOf(sliders[index].getValue()));
                    triggerChange(onChange);
                }
            });

            // обработка текстовых полей
            fields[i].addActionListener(e -> triggerChange(onChange));
        }
    }

    private void triggerChange(Consumer<double[]> onChange) {
        if (isUpdating) return;
        try {
            double[] values = new double[fields.length];
            for (int i = 0; i < fields.length; i++) {
                values[i] = Double.parseDouble(fields[i].getText());
            }
            onChange.accept(values);
        } catch (NumberFormatException ignored) {}
    }

    public void setValues(double[] values) {
        isUpdating = true;
        for (int i = 0; i < values.length; i++) {
            int val = (int) Math.round(values[i]);
            sliders[i].setValue(val);
            fields[i].setText(String.valueOf(val));
        }
        isUpdating = false;
    }
}