package me.marcelohdez.drawingBoard.dialogs;

import javax.swing.*;
import java.awt.*;

public class ChoiceDialog extends JDialog {

    private String response;

    public ChoiceDialog(Component summoner, String desc, String choice, String... otherChoices) {
        setTitle("Select");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setResizable(false);

        JPanel buttonRow = new JPanel();
        addButtonTo(buttonRow, "Cancel");
        addButtonTo(buttonRow, choice);
        for (String s : otherChoices) addButtonTo(buttonRow, s);

        JLabel label = new JLabel(desc);
        label.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        add(label, BorderLayout.NORTH);
        add(buttonRow, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(summoner);
    }

    private void addButtonTo(JPanel to, String buttonText) {
        JButton button = new JButton(buttonText);
        button.addActionListener(e -> {
            response = buttonText;
            setVisible(false);
            dispose();
        });
        to.add(button);
    }

    public String response() {
        setModalityType(ModalityType.APPLICATION_MODAL);
        setVisible(true);

        return response;
    }

}
