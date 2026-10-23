import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Frame extends JFrame {
    private JLabel originalImageLabel;
    private JLabel processedImageLabel;
    private JLabel statusLabel;
    private BufferedImage currentImage;
    private BufferedImage processedImage;

    // Экземпляры классов-обработчиков
    private final OtsuThresholdProcessor otsuProcessor = new OtsuThresholdProcessor();
    private final GradientThresholdProcessor gradientProcessor = new GradientThresholdProcessor();
    private final LowPassFilterProcessor smoothProcessor = new LowPassFilterProcessor();

    public Frame() {
        setTitle("Лаб2");
        setSize(1200, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Верхняя панель с кнопками управления
        JPanel controlPanel = new JPanel();
        JButton loadButton = new JButton("Загрузить изображение");
        loadButton.setFont(new Font("Arial", Font.PLAIN, 28));
        JButton gradientButton = new JButton("Порог по градиенту");
        gradientButton.setFont(new Font("Arial", Font.PLAIN, 28));
        JButton otsuButton = new JButton("Метод Оцу");
        otsuButton.setFont(new Font("Arial", Font.PLAIN, 28));
        JButton smoothButton = new JButton("Низкочастотный фильтр");
        smoothButton.setFont(new Font("Arial", Font.PLAIN, 28));

        controlPanel.add(loadButton);
        controlPanel.add(gradientButton);
        controlPanel.add(otsuButton);
        controlPanel.add(smoothButton);
        add(controlPanel, BorderLayout.NORTH);

        // Центральная панель для отображения изображений
        JPanel imagePanel = new JPanel(new GridLayout(1, 2, 10, 10));
        originalImageLabel = new JLabel("Исходное изображение", JLabel.CENTER);
        originalImageLabel.setFont(new Font("Arial", Font.PLAIN, 28));
        processedImageLabel = new JLabel("Обработанное изображение", JLabel.CENTER);
        processedImageLabel.setFont(new Font("Arial", Font.PLAIN, 28));

        originalImageLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        processedImageLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        imagePanel.add(new JScrollPane(originalImageLabel));
        imagePanel.add(new JScrollPane(processedImageLabel));
        add(imagePanel, BorderLayout.CENTER);

        // Нижняя панель для статуса
        statusLabel = new JLabel("Ожидание загрузки изображения...", JLabel.LEFT);
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 28));
        add(statusLabel, BorderLayout.SOUTH);

        //Навешиваю слушателей на кнопки
        loadButton.addActionListener(new ActionListener() {
             @Override
             public void actionPerformed(ActionEvent e) {
                 loadImage();
                 originalImageLabel.setText("");
                 processedImageLabel.setText("");
             }
         }
        );

        gradientButton.addActionListener(new ActionListener() {
            @Override
             public void actionPerformed(ActionEvent e) {
                if (currentImage != null) {
                    processedImage = gradientProcessor.process(currentImage);
                    processedImageLabel.setIcon(new ImageIcon(processedImage));
                    statusLabel.setText("Применен метод выбора порога на основе градиента");
                } else {
                    JOptionPane.showMessageDialog(Frame.this, "Сначала загрузите изображение!");
                }
            }
        });

        otsuButton.addActionListener(new ActionListener() {
            @Override
             public void actionPerformed(ActionEvent e) {
                if (currentImage != null) {
                    processedImage = otsuProcessor.process(currentImage);
                    processedImageLabel.setIcon(new ImageIcon(processedImage));
                    statusLabel.setText("Применен метод Отсу");
                } else {
                    JOptionPane.showMessageDialog(Frame.this, "Сначала загрузите изображение!");
                }
            }
        });

        smoothButton.addActionListener(new ActionListener() {
            @Override
             public void actionPerformed(ActionEvent e) {
                if (currentImage != null) {
                    processedImage = smoothProcessor.process(currentImage);
                    processedImageLabel.setIcon(new ImageIcon(processedImage));
                    statusLabel.setText("Применен сглаживающий низкочастотный фильтр");
                } else {
                    JOptionPane.showMessageDialog(Frame.this, "Сначала загрузите изображение!");
                }
            }
        });
    }


    private void loadImage() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                currentImage = ImageIO.read(file);
                originalImageLabel.setIcon(new ImageIcon(currentImage));
                processedImageLabel.setIcon(null);
                statusLabel.setText("Загружено" + file.getName());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Ошибка при загрузке файла!");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Frame().setVisible(true);
        });
    }
}