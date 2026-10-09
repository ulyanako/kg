import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class ColorModelPanel extends JPanel {

    public interface ColorChangeListener {
        void onColorChanged(ColorModelPanel source);
    }

    private final String[] labels;
    private final int[] maxValues;
    private final JTextField[] textFields;
    private final JSlider[] sliders;

    private ColorChangeListener listener;
    private boolean isSelfUpdating = false;

    public ColorModelPanel(String[] labels, int[] maxValues) {
        this.labels = labels;
        this.maxValues = maxValues;

        setLayout(new GridBagLayout());

        textFields = new JTextField[labels.length];
        sliders = new JSlider[labels.length];

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0; gbc.gridy = i;
            gbc.weightx = 0.0;
            add(new JLabel(labels[i]), gbc);

            textFields[i] = new JTextField("0", 4);
            gbc.gridx = 1;
            add(textFields[i], gbc);

            sliders[i] = new JSlider(0, maxValues[i], 0);
            sliders[i].setPreferredSize(new Dimension(130, 20));
            gbc.gridx = 2;
            gbc.weightx = 1.0;
            add(sliders[i], gbc);

            final int index = i;

            sliders[i].addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    if (isSelfUpdating) return;
                    isSelfUpdating = true;
                    textFields[index].setText(String.valueOf(sliders[index].getValue()));
                    isSelfUpdating = false;
                    notifyListener();
                }
            });

            ActionListener textAction = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    processTextFieldInput(index);
                }
            };
            textFields[i].addActionListener(textAction);
            textFields[i].addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    processTextFieldInput(index);
                }
            });
        }
    }

    private void processTextFieldInput(int index) {
        if (isSelfUpdating) return;
        try {
            int val = Integer.parseInt(textFields[index].getText().trim());
            val = Math.max(0, Math.min(maxValues[index], val));

            isSelfUpdating = true;
            sliders[index].setValue(val);
            textFields[index].setText(String.valueOf(val));
            isSelfUpdating = false;

            notifyListener();
        } catch (NumberFormatException ex) {
            textFields[index].setText(String.valueOf(sliders[index].getValue()));
        }
    }

    public void setValues(double[] values) {
        isSelfUpdating = true;
        for (int i = 0; i < values.length; i++) {
            int val = (int) Math.round(values[i]);
            val = Math.max(0, Math.min(maxValues[i], val));
            sliders[i].setValue(val);
            textFields[i].setText(String.valueOf(val));
        }
        isSelfUpdating = false;
    }

    public double[] getValues() {
        double[] vals = new double[labels.length];
        for (int i = 0; i < labels.length; i++) {
            vals[i] = sliders[i].getValue();
        }
        return vals;
    }

    public void setColorChangeListener(ColorChangeListener listener) {
        this.listener = listener;
    }

    private void notifyListener() {
        if (listener != null && !isSelfUpdating) {
            listener.onColorChanged(this);
        }
    }
}