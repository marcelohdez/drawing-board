package me.marcelohdez.drawingBoard.dialogs;

import javax.swing.*;
import java.awt.*;

public class ResizeDialog extends JDialog {

    JSpinner widthSpinner, heightSpinner;
    int initWidth, initHeight;

    JButton finishButton = new JButton("Resize");

    public ResizeDialog(Component summoner, int initWidth, int initHeight) {
        this.initWidth = initWidth;
        this.initHeight = initHeight;

        setTitle("Change size");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setResizable(false);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        JPanel labelRow = new JPanel();
        labelRow.add(new JLabel("Choose new size:"));

        JPanel buttonPanel = new JPanel();
        finishButton.setEnabled(false);
        finishButton.addActionListener(e -> {
            setVisible(false);
            dispose();
        });
        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(e -> {
            widthSpinner.setValue(initWidth);
            heightSpinner.setValue(initHeight);
            setVisible(false);
            dispose();
        });
        buttonPanel.add(cancel);
        buttonPanel.add(finishButton);

        add(labelRow);
        add(createSpinners());
        add(buttonPanel);
        pack();
        setLocationRelativeTo(summoner); // Center on summoner

    }

    /**
     * Returns a Dimension with the user's chosen sizes, if cancelled it will return a
     * Dimension with the sizes given to this ResizeDialog's constructor
     */
    public Dimension response() {
        setModalityType(ModalityType.APPLICATION_MODAL);
        setVisible(true);

        return new Dimension((int) widthSpinner.getValue(), (int) heightSpinner.getValue());
    }

    private JPanel createSpinners() {
        JPanel spinnerRow = new JPanel();
        widthSpinner = new JSpinner(new SpinnerNumberModel(initWidth, 1, 99999, 1));
        heightSpinner = new JSpinner(new SpinnerNumberModel(initHeight, 1, 99999, 1));

        widthSpinner.addChangeListener(e -> finishButton.setEnabled(checkForChange()));
        heightSpinner.addChangeListener(e -> finishButton.setEnabled(checkForChange()));

        spinnerRow.add(widthSpinner);
        spinnerRow.add(new JLabel("x"));
        spinnerRow.add(heightSpinner);

        return spinnerRow;
    }

    private boolean checkForChange() {
        return (int) widthSpinner.getValue() != initWidth ||
                (int) heightSpinner.getValue() != initHeight;
    }

}
