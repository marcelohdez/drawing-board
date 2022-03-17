package me.marcelohdez.drawingBoard.dialogs;

import javax.swing.*;
import java.awt.*;

/**
 * Modal JDialog with a description, a cancel button, a choice button, and optionally
 * endless other choice buttons. All these buttons have their text returned by the
 * response() method when clicked.
 */
public class ChoiceDialog extends JDialog {

    private String response;

    public ChoiceDialog(Component summoner, String desc, String choice, String... otherChoices) {
        setTitle("Select");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setResizable(false);

        JPanel buttonRow = new JPanel();
        newButtonIn(buttonRow, "Cancel"); // Cancel button
        newButtonIn(buttonRow, choice); // Button which choice text
        for (String text : otherChoices) newButtonIn(buttonRow, text); // All other choice buttons

        JTextArea descTextArea = new JTextArea(desc); // New text area with desired description text
        descTextArea.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        descTextArea.setMargin(new Insets(6, 6, 6, 6)); // Padding to not be right on window's edge
        descTextArea.setEditable(false);

        add(descTextArea, BorderLayout.NORTH);
        add(buttonRow, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(summoner);
    }

    /** Creates a new button with the given text in the given JPanel */
    private void newButtonIn(JPanel pnl, String buttonText) {
        JButton button = new JButton(buttonText);

        button.addActionListener(e -> {
            response = buttonText;
            setVisible(false);
            dispose();
        });

        pnl.add(button);
    }

    /** Will return the selected button's text */
    public String response() {
        setModalityType(ModalityType.APPLICATION_MODAL);
        setVisible(true);

        return response;
    }

}
