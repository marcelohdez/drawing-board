package me.soggysandwich.drawingBoard.main;

import me.soggysandwich.drawingBoard.dialogs.ChoiceDialog;
import me.soggysandwich.drawingBoard.dialogs.ResizeDialog;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.Objects;

public class MainWindow extends JFrame implements KeyListener {

    DrawingPanel mainPnl = new DrawingPanel(this, 600, 600);
    JFileChooser fileChooser; // For exporting our images

    // Action keys
    private boolean isShifting, isHoldingCtrl;

    public MainWindow() {

        setTitle("Drawing Board v0.2-dev");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JScrollPane scrollPane = new JScrollPane(mainPnl);
        add(createBrushPanel(), BorderLayout.WEST);
        add(scrollPane, BorderLayout.CENTER);
        add(createSidePanel(), BorderLayout.EAST);

        pack();
        setMinimumSize(new Dimension((int) (getWidth() * 0.7), (int) (getHeight() * 0.7)));
        setLocationRelativeTo(null);
        setVisible(true);

        SwingUtilities.invokeLater(() -> fileChooser = new JFileChooser());

    }

    /** Create left-side panel which contains our brushes and tools. */
    private JScrollPane createBrushPanel() {

        JPanel brushPanel = new JPanel();
        brushPanel.setLayout(new BoxLayout(brushPanel, BoxLayout.Y_AXIS));
        // Brushes
        addLabelTo(brushPanel, "Brush Types");
        addButtonTo(brushPanel, "Rectangle", e -> mainPnl.setTool(Tool.BRUSH_RECTANGLE));
        addButtonTo(brushPanel, "Round-Rect", e -> mainPnl.setTool(Tool.BRUSH_ROUND_RECT));
        addButtonTo(brushPanel, "Circle", e -> mainPnl.setTool(Tool.BRUSH_CIRCLE));

        JCheckBox fill = new JCheckBox("Solid");
        fill.setSelected(true);
        fill.addActionListener(e -> mainPnl.makeBrushSolid(fill.isSelected()));
        brushPanel.add(fill);
        // Tools
        brushPanel.add(new JSeparator());
        addLabelTo(brushPanel, "Tools");
        addButtonTo(brushPanel, "Eraser", e -> mainPnl.setTool(Tool.ERASER));
        addButtonTo(brushPanel, "Picker", e -> mainPnl.setTool(Tool.COLOR_PICKER));

        brushPanel.add(new JSeparator());
        addButtonTo(brushPanel, "Set Color",
                e -> mainPnl.setBrushFillColor(JColorChooser.showDialog(
                        mainPnl, "New color", mainPnl.brushFillColor())));

        return new JScrollPane(brushPanel);

    }

    /** Create right-side panel which contains functions like clear resize and export. */
    private JScrollPane createSidePanel() {

        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));

        addButtonTo(sidePanel, "Clear", e -> mainPnl.clear());
        //addButtonTo( sidePanel, "Undo", e -> canvas.undo());
        addButtonTo(sidePanel, "Resize", e -> resizeCanvas());

        sidePanel.add(new JSeparator());
        addButtonTo(sidePanel, "Export", e -> exportCanvas());

        return new JScrollPane(sidePanel);

    }

    private void addButtonTo(JPanel to, String text, ActionListener al) {
        JButton button = new JButton(text);
        button.addKeyListener(this);
        button.addActionListener(al);
        button.setMargin(new Insets(20, 10, 20, 10));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        to.add(button);
    }

    private void addLabelTo(JPanel to, String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        to.add(label);
    }

    private void resizeCanvas() {
        Dimension newSize = new ResizeDialog(this, mainPnl.canvasWidth(), mainPnl.canvasHeight()).response();
        // If it is a different size, then resize canvas
        if (!Objects.equals(newSize, new Dimension(mainPnl.canvasWidth(), mainPnl.canvasHeight()))) {
            mainPnl.changeCanvasSize(newSize);
        }
    }

    private void exportCanvas() {
        String choice = new ChoiceDialog(this, "Export as...", "PNG", "JPEG").response();

        if (fileChooser != null && !Objects.equals(choice, "Cancel")) {
            BufferedImage image = mainPnl.getCanvas();
            if (Objects.equals(choice, "JPEG")) { // Get rid of transparency by converting to RGB from ARGB
                image = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
                image.getGraphics().drawImage(mainPnl.getCanvas(), 0, 0, null);
            }

            fileChooser.setFileFilter(new FileNameExtensionFilter("Images", "png", "jpeg", "jpg"));
            if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                try {
                    ImageIO.write(image,
                            choice,
                            checkExtension(fileChooser.getSelectedFile(), choice));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /** Checks if the given file has the given extension, if not, add it. */
    private File checkExtension(File f, String extension) {
        extension = "." + extension.toLowerCase(Locale.ROOT);

        if (!f.getName().endsWith(extension)) {
            return new File(f.getPath() + extension);
        } else return f;
    }

    protected boolean isShifting() {
        return isShifting;
    }

    protected boolean isHoldingCtrl() {
        return isHoldingCtrl;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            // ======== Action keys ========
            case KeyEvent.VK_SHIFT -> {
                if (isHoldingCtrl) isHoldingCtrl = false; // User must use one or the other for shortcuts
                isShifting = true;
            }
            // Command (meta) on macOS will be considered as ctrl for our shortcuts
            case KeyEvent.VK_CONTROL, KeyEvent.VK_META -> {
                if (isShifting) isShifting = false; // User must use one or the other for shortcuts
                isHoldingCtrl = true;
            }
            // ======== Regular keys ========
            case KeyEvent.VK_UP -> { // Increase brush size
                if (isHoldingCtrl) {
                    mainPnl.changeBrushSizeBy((int) (mainPnl.brushSize() * 0.1));
                } else if (isShifting) mainPnl.changeBrushSizeBy(1);
            }
            case KeyEvent.VK_DOWN -> { // Decrease brush size
                if (isHoldingCtrl) {
                    mainPnl.changeBrushSizeBy((int) -(mainPnl.brushSize() * 0.1));
                } else if (isShifting) mainPnl.changeBrushSizeBy(-1);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Disable action key values
        switch (e.getKeyCode()) {
            case KeyEvent.VK_SHIFT -> isShifting = false;
            // Command (meta) on macOS will be considered as ctrl for our shortcuts
            case KeyEvent.VK_CONTROL, KeyEvent.VK_META -> isHoldingCtrl = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

}
