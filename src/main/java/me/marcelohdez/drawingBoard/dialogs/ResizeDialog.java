package me.marcelohdez.drawingBoard.dialogs;

import javax.swing.*;
import java.awt.*;

/**
 * Modal JDialog which allows the user to select a width and a height that can then
 * be returned as a Dimension using the response() method
 */
public class ResizeDialog extends JDialog {

    JSpinner widthSpinner, heightSpinner;
    JButton finishButton = new JButton("Resize");

    /**
     * Creates a ResizeDialog with the given default values that centers itself on
     * the summoner Component.
     *
     * @param summoner Window which summoned this dialog, used to center this dialog
     * @param initWidth Value to default the width JSpinner to
     * @param initHeight Value to default the height JSpinner to
     */
    public ResizeDialog(Component summoner, int initWidth, int initHeight) {
        setTitle("Change size");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE); // Only close this window through "cancel" button
        setResizable(false);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        JPanel labelRow = new JPanel();
        labelRow.add(new JLabel("Choose new size:"));

        JPanel buttonPanel = new JPanel();
        finishButton.setEnabled(false); // Only enabled if values have changed
        JButton cancelButton = new JButton("Cancel");
        buttonPanel.add(cancelButton);
        buttonPanel.add(finishButton);

        finishButton.addActionListener(e -> {
            setVisible(false);
            dispose();
        });
        cancelButton.addActionListener(e -> {
            // Default spinners to initial values to be returned by the response() method
            widthSpinner.setValue(initWidth);
            heightSpinner.setValue(initHeight);

            setVisible(false);
            dispose();
        });

        add(labelRow);
        add(createSpinners(initWidth, initHeight));
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

    private JPanel createSpinners(int width, int height) {
        JPanel spinnerRow = new JPanel();
        widthSpinner = new JSpinner(new SpinnerNumberModel(width, 1, 99999, 1));
        heightSpinner = new JSpinner(new SpinnerNumberModel(height, 1, 99999, 1));

        widthSpinner.addChangeListener(e -> finishButton.setEnabled(checkForChange(width, height)));
        heightSpinner.addChangeListener(e -> finishButton.setEnabled(checkForChange(width, height)));

        spinnerRow.add(widthSpinner);
        spinnerRow.add(new JLabel("x"));
        spinnerRow.add(heightSpinner);

        return spinnerRow;
    }

    /**
     * Checks if either the widthSpinner or heightSpinner's value differs from the given
     * w and h values respectively
     */
    private boolean checkForChange(int w, int h) {
        return (int) widthSpinner.getValue() != w || (int) heightSpinner.getValue() != h;
    }

}
