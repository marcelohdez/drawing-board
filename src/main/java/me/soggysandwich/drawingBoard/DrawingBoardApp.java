package me.soggysandwich.drawingBoard;

import me.soggysandwich.drawingBoard.main.MainWindow;

import javax.swing.*;

public class DrawingBoardApp {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        new MainWindow();
    }

}
