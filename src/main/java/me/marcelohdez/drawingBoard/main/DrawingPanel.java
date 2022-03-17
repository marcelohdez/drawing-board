package me.marcelohdez.drawingBoard.main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

public class DrawingPanel extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener {

    private final MainWindow parent; // Used to check if user is shifting/holding ctrl

    // Canvas data
    private BufferedImage canvas; // Current, to draw on
    private int canvasX;
    private int canvasY;

    // Brush data
    private int brushSize = 40;
    private boolean brushIsSolid = true;
    private Color brushFill = Color.BLACK;
    private Tool currentTool = Tool.BRUSH_CIRCLE;
    private Tool lastTool = currentTool; // For switching between picker and back

    // To draw greyed out version of current brush on cursor:
    int lastCursorX = -brushSize;
    int lastCursorY = -brushSize;
    final int PICKER_SIZE = 60; // Size of color picker window

    public DrawingPanel(MainWindow parent, int width, int height) {
        this.parent = parent;

        // Add mouse listeners for drawing
        addMouseListener(this);
        addMouseMotionListener(this);
        addMouseWheelListener(this);

        setPreferredSize(new Dimension(width, height)); // Does not allow this component to shrink when calling pack()
        canvas = createCanvas(width, height);
    }

    @Override
    public void paintComponent(Graphics gr) {
        super.paintComponent(gr); // Run JPanel's default paintComponent method which clears the screen etc
        Graphics2D g = (Graphics2D) gr;

        // Get the top left point of the canvas once it is centered:
        canvasX = (getWidth() / 2) - (canvas.getWidth() / 2);
        canvasY = (getHeight() / 2) - (canvas.getHeight() / 2);

        g.drawImage(canvas, canvasX, canvasY, this); // Draw our current image
        g.drawRect(canvasX - 1, canvasY - 1, canvas.getWidth() + 1, canvas.getHeight() + 1); // Outline

        // Draw outline of current tool:
        g.setColor(Color.GRAY);
        if (currentTool == Tool.COLOR_PICKER) {
            g.drawRect(lastCursorX + 4, lastCursorY - PICKER_SIZE - 6, PICKER_SIZE + 1, PICKER_SIZE + 1);
            g.setColor(pickColorAt(lastCursorX - canvasX, lastCursorY - canvasY));
            g.fillRect(lastCursorX + 5, lastCursorY - PICKER_SIZE - 5, PICKER_SIZE, PICKER_SIZE);
        } else g.draw(getChosenShape(lastCursorX, lastCursorY));
    }

    public void changeCanvasSize(Dimension d) {
        setPreferredSize(d);
        canvas = createCanvas(d.width, d.height);
        repaint();
    }

    public void clear() {
        canvas = createCanvas(canvas.getWidth(), canvas.getHeight());
        repaint();
    }

    public void setTool(Tool s) {
        lastTool = currentTool;
        currentTool = s;
        repaint();
    }

    public void changeBrushSizeBy(int amount) {
        brushSize += amount;
        repaint();
    }

    public void makeBrushSolid(boolean b) {
        brushIsSolid = b;
    }

    public void setBrushFillColor(Color color) {
        brushFill = color;
    }

    public int canvasWidth() {
        return canvas.getWidth();
    }

    public int canvasHeight() {
        return canvas.getHeight();
    }

    public Color brushFillColor() {
        return brushFill;
    }

    public BufferedImage getCanvas() {
        return canvas;
    }

    public int brushSize() {
        return brushSize;
    }

    private void drawAt(int x, int y) {
        Graphics2D g = (Graphics2D) canvas.getGraphics();

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (currentTool == Tool.ERASER) {
            g.setBackground(new Color(0, 0, 0, 0)); // Transparent
            g.clearRect(x - brushSize / 2, y - brushSize / 2, brushSize, brushSize);
        } else if (currentTool != Tool.COLOR_PICKER) {
            g.setColor(brushFill);
            if (brushIsSolid) {
                g.fill(getChosenShape(x, y));
            } else g.draw(getChosenShape(x, y));
        }

        g.dispose();
        repaintCursorArea();
    }

    private BufferedImage createCanvas(int w, int h) {
        return new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
    }

    private Color pickColorAt(int x, int y) {
        try {
            int color = canvas.getRGB(x, y);
            // Values gotten from: https://stackoverflow.com/a/25761560
            return new Color((color & 0xff0000) >> 16,
                    (color & 0xff00) >> 8,
                    color & 0xff,
                    (color & 0xff000000) >>> 24);
        } catch (ArrayIndexOutOfBoundsException e) {
            return Color.BLACK;
        }
    }

    private Shape getChosenShape(int x, int y) {
        switch (currentTool) {
            case BRUSH_CIRCLE -> {
                return new Ellipse2D.Float(x - brushSize / 2f, y - brushSize / 2f, brushSize, brushSize);
            }
            case BRUSH_ROUND_RECT -> {
                final float ARC_RADIUS = 24;

                return new RoundRectangle2D.Float(
                        x - brushSize / 2f,
                        y - brushSize / 2f,
                        brushSize,
                        brushSize,
                        ARC_RADIUS,
                        ARC_RADIUS
                );
            }
            default -> {
                return new Rectangle(x - brushSize / 2, y - brushSize / 2, brushSize, brushSize);
            }
        }
    }

    private void repaintCursorArea() {
        // Extra spacing to paint to not leave trails when moving cursor
        final int PICKER_BOX_BUFFER = 6;
        final int BRUSH_AREA_BUFFER = 3;

        if (currentTool == Tool.COLOR_PICKER) {
            repaint(
                    lastCursorX + 2,
                    lastCursorY - PICKER_SIZE - 8,
                    PICKER_SIZE + PICKER_BOX_BUFFER,
                    PICKER_SIZE + PICKER_BOX_BUFFER
            );
        } else {
            repaint(
                    lastCursorX - 1 - brushSize / 2,
                    lastCursorY - 1 - brushSize / 2,
                    brushSize + BRUSH_AREA_BUFFER,
                    brushSize + BRUSH_AREA_BUFFER
            );
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (currentTool == Tool.COLOR_PICKER) {
            brushFill = pickColorAt(e.getX() - canvasX, e.getY() - canvasY);
            repaintCursorArea(); // Get rid of color picker window
            currentTool = lastTool;
            repaintCursorArea(); // Paint new brush outline
        } else drawAt(e.getX() - canvasX, e.getY() - canvasY);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        drawAt(e.getX() - canvasX, e.getY() - canvasY);
        lastCursorX = e.getX();
        lastCursorY = e.getY();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        repaintCursorArea();
        lastCursorX = e.getX();
        lastCursorY = e.getY();
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        // e.getWheelRotation is -1 when scrolling up, and 1 when scrolling down,
        // so here we flip it to make it more readable
        int scrollDir = -(e.getWheelRotation());

        if (parent.isHoldingCtrl()) { // Change size by 10%
            changeBrushSizeBy((int) (scrollDir * (brushSize * 0.1)));
        } else if (parent.isShifting()) { // Change size by 1 point
            changeBrushSizeBy(scrollDir);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {}
    @Override
    public void mouseReleased(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}

}
