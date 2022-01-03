package me.soggysandwich.drawingBoard;

import me.soggysandwich.drawingBoard.main.MainWindow;

import javax.swing.*;

public class DrawingBoardApp {

    public static void main(String[] args) {
        try { // Try to set system's look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            try { // If not available, try to set the cross-platform look and feel
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception ex) { // Finally, if that fails then print the error.
                ex.printStackTrace();
            }
        }

        new MainWindow();
    }

}
