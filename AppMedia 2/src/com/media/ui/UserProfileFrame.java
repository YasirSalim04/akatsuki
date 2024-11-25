package com.media.ui;

import com.media.auth.AuthManager; // Import statement added
import javax.swing.*;
import java.awt.*;

public class UserProfileFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    private final String username;   // Username of the logged-in user
    private final AuthManager authManager;  // AuthManager instance for handling authentication

    public UserProfileFrame(String username, AuthManager authManager) {
        this.username = username;
        this.authManager = authManager;

        // Print to debug usage of fields
        debugPrint();

        // Set up JFrame properties
        setTitle("User Profile - Welcome, " + username);
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(6, 1));

        // Set up UI components
        JLabel welcomeLabel = new JLabel("Manage Your Profile, " + username, SwingConstants.CENTER);
        add(welcomeLabel);

        // Display current username
        JLabel currentUsernameLabel = new JLabel("Current Username: " + username, SwingConstants.CENTER);
        add(currentUsernameLabel);

        // Button to update username
        JButton updateUsernameButton = new JButton("Update Username");
        add(updateUsernameButton);

        // Button to update password
        JButton updatePasswordButton = new JButton("Update Password");
        add(updatePasswordButton);

        // Back button to go back to main menu
        JButton backButton = new JButton("Back");
        add(backButton);

        // Action listener for update username button
        updateUsernameButton.addActionListener(e -> {
            String newUsername = JOptionPane.showInputDialog("Enter new username:");
            if (newUsername != null && !newUsername.trim().isEmpty()) {
                boolean success = authManager.updateUsername(username, newUsername);
                if (success) {
                    JOptionPane.showMessageDialog(UserProfileFrame.this, "Username updated successfully!");
                } else {
                    JOptionPane.showMessageDialog(UserProfileFrame.this, "Failed to update username.");
                }
            }
        });

        // Action listener for update password button
        updatePasswordButton.addActionListener(e -> {
            String newPassword = JOptionPane.showInputDialog("Enter new password:");
            if (newPassword != null && !newPassword.trim().isEmpty()) {
                boolean success = authManager.updatePassword(username, newPassword);
                if (success) {
                    JOptionPane.showMessageDialog(UserProfileFrame.this, "Password updated successfully!");
                } else {
                    JOptionPane.showMessageDialog(UserProfileFrame.this, "Failed to update password.");
                }
            }
        });

        // Action listener for back button
        backButton.addActionListener(e -> {
            dispose(); // Close the current frame
            new MainMenuFrame(username); // Open the main menu again
        });

        // Set JFrame visibility
        setVisible(true);
    }

    private void debugPrint() {
        System.out.println("Username is used: " + username);
        System.out.println("AuthManager is used: " + authManager);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UserProfileFrame("testUser", new AuthManager()));
    }
}
