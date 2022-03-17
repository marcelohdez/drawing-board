package me.marcelohdez.drawingBoard;

import me.marcelohdez.drawingBoard.main.MainWindow;

import javax.swing.*;

public class DrawingBoardApp {
    public static void main(String[] args) {
        try { // Set the system look and feel, if it is not available the cross-platform one will be used
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        new MainWindow();
    }
}
