package com.media.ui;

import com.media.auth.AuthManager; // Import statement added
import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    private AuthManager authManager; // Reference to AuthManager

    // Default constructor that initializes AuthManager internally
    public LoginFrame() {
        this(new AuthManager());
    }

    // Constructor with AuthManager as a parameter
    public LoginFrame(AuthManager authManager) {
        this.authManager = authManager; // Assign AuthManager
        useAuthManager(); // Explicit use of authManager to resolve the warning

        // Set up JFrame properties
        setTitle("Login");
        setSize(300, 300);  // Increase the size to accommodate new Register button
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(4, 1));  // Updated to 4 rows to fit Register button

        // Username field
        JTextField userText = new JTextField(15);
        JLabel userLabel = new JLabel("Username:");
        JPanel userPanel = new JPanel();
        userPanel.add(userLabel);
        userPanel.add(userText);
        add(userPanel);

        // Password field
        JPasswordField passText = new JPasswordField(15);
        JLabel passLabel = new JLabel("Password:");
        JPanel passPanel = new JPanel();
        passPanel.add(passLabel);
        passPanel.add(passText);
        add(passPanel);

        // Login button
        JButton loginButton = new JButton("Login");
        add(loginButton);

        // Register button
        JButton registerButton = new JButton("Register");
        add(registerButton);

        // Action listener for the login button
        loginButton.addActionListener(e -> {
            String username = userText.getText();
            String password = new String(passText.getPassword());

            // Use authManager to validate user credentials
            boolean isAuthenticated = authManager.loginUser(username, password);
            if (isAuthenticated) {
                JOptionPane.showMessageDialog(null, "Login successful!");
                dispose();  // Close the login window
                new MainMenuFrame(username);  // Open main menu window
            } else {
                JOptionPane.showMessageDialog(null, "Invalid credentials. Please try again.");
            }
        });

        // Action listener for the register button
        registerButton.addActionListener(e -> {
            dispose(); // Close login window
            new RegisterFrame(authManager); // Open registration window
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame());
    }
}
