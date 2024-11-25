package com.media.ui;

import com.media.auth.AuthManager;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    private AuthManager authManager;

    public RegisterFrame(AuthManager authManager) {
        this.authManager = authManager;
        useAuthManager(); // Explicit use of authManager to resolve the warning

        // Set up JFrame properties
        setTitle("Register");
        setSize(300, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);

        // Username field
        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(10, 20, 80, 25);
        add(userLabel);

        JTextField userText = new JTextField(20);
        userText.setBounds(100, 20, 165, 25);
        add(userText);

        // Password field
        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(10, 60, 80, 25);
        add(passLabel);

        JPasswordField passText = new JPasswordField(20);
        passText.setBounds(100, 60, 165, 25);
        add(passText);

        // Email field (optional)
        JLabel emailLabel = new JLabel("Email (optional):");
        emailLabel.setBounds(10, 100, 150, 25);
        add(emailLabel);

        JTextField emailText = new JTextField(20);
        emailText.setBounds(100, 130, 165, 25);
        add(emailText);

        // Register button
        JButton registerButton = new JButton("Register");
        registerButton.setBounds(100, 180, 165, 25);
        add(registerButton);

        // Go Back button
        JButton goBackButton = new JButton("Go Back");
        goBackButton.setBounds(100, 220, 165, 25);
        add(goBackButton);

        // Register button action listener
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = userText.getText();
                String password = new String(passText.getPassword());
                String email = emailText.getText().trim();

                if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Username and password are required fields.", "Registration Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    boolean registrationSuccess = authManager.registerUser(username, password, email.isEmpty() ? null : email);
                    if (registrationSuccess) {
                        JOptionPane.showMessageDialog(null, "Registration successful!");
                        dispose(); // Close registration window
                        new LoginFrame(authManager); // Open login window
                    } else {
                        JOptionPane.showMessageDialog(null, "Registration failed. Username may already exist.", "Registration Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        // Go Back button action listener
        goBackButton.addActionListener(e -> {
            dispose(); // Close registration window
            new LoginFrame(authManager); // Go back to the login window
        });

        // Set JFrame visibility
        setVisible(true);
    }

    // Dummy method to make use of authManager explicitly
    private void useAuthManager() {
        if (authManager == null) {
            throw new IllegalStateException("AuthManager not initialized properly");
        }
    }
}
