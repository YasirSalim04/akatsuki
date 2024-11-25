package com.media.ui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WelcomeFrame extends JFrame {
    private static final long serialVersionUID = 1L;  // Add this line to remove the warning

    public WelcomeFrame() {
        // Set up JFrame
        setTitle("Welcome to Media Stream App");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        // Welcome Label
        JLabel welcomeLabel = new JLabel("Welcome to Media Stream App");
        welcomeLabel.setBounds(100, 30, 200, 30);
        add(welcomeLabel);

        // Continue Button
        JButton continueButton = new JButton("Continue");
        continueButton.setBounds(150, 100, 100, 30);
        add(continueButton);

        // Continue Button Listener
        continueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();  // Close the welcome window
                new LoginFrame();  // Open login window (no parameter needed)
            }
        });

        // Set JFrame visibility
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new WelcomeFrame());
    }
}
