package com.media.ui;

import com.media.auth.AuthManager; // Import statement added
import com.media.db.DatabaseConnection; // Import to test the database connection
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class MainMenuFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    protected String username;
    private boolean isDarkMode = true; // Default to dark mode

    public MainMenuFrame(String username) {
        this.username = username;

        // Set up JFrame properties for maximized window
        setTitle("Media Stream App - Main Menu - Logged in as " + username);
        setSize(800, 600); // Set a default size before maximizing
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);  // Make the frame maximized
        setLayout(new GridLayout(7, 1)); // Updated to accommodate the new button layout

        // Set up a dark theme color scheme initially
        applyTheme();

        // Welcome message
        JLabel welcomeLabel = new JLabel("Welcome, " + username + "!", SwingConstants.CENTER);
        welcomeLabel.setForeground(getTextColor());
        add(welcomeLabel);

        // Browse Media button
        JButton browseMediaButton = new JButton("Browse Media");
        styleButton(browseMediaButton);
        add(browseMediaButton);

        // Manage Account button
        JButton manageAccountButton = new JButton("Manage Account");
        styleButton(manageAccountButton);
        add(manageAccountButton);

        // View Favorites button
        JButton viewFavoritesButton = new JButton("View Favorites");
        styleButton(viewFavoritesButton);
        add(viewFavoritesButton);

        // Test Database button
        JButton testDatabaseButton = new JButton("Test Database");
        styleButton(testDatabaseButton);
        add(testDatabaseButton);

        // Logout button
        JButton logoutButton = new JButton("Logout");
        styleButton(logoutButton);
        add(logoutButton);

        // Theme switch button
        JButton switchThemeButton = new JButton("Switch to Light Mode");
        styleButton(switchThemeButton);
        add(switchThemeButton);

        // Set up button actions
        browseMediaButton.addActionListener(e -> {
            dispose();
            new BrowseMediaFrame(username, this);
        });

        manageAccountButton.addActionListener(e -> {
            dispose();
            new UserProfileFrame(username, new AuthManager());
        });

        viewFavoritesButton.addActionListener(e -> {
            dispose();
            new FavoritesFrame(username, this);
        });

        testDatabaseButton.addActionListener(e -> testDatabaseConnection());

        logoutButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Goodbye, " + username + "!");
            dispose();
            new LoginFrame(new AuthManager());
        });

        switchThemeButton.addActionListener(e -> {
            isDarkMode = !isDarkMode;
            applyTheme();
            switchThemeButton.setText(isDarkMode ? "Switch to Light Mode" : "Switch to Dark Mode");
        });

        setVisible(true);
    }

    // Method to apply the selected theme
    private void applyTheme() {
        Color backgroundColor = isDarkMode ? new Color(30, 30, 30) : new Color(255, 255, 255);
        Color textColor = isDarkMode ? new Color(255, 255, 255) : new Color(0, 0, 0);

        getContentPane().setBackground(backgroundColor);

        // Iterate through all components and apply the theme
        for (Component component : getContentPane().getComponents()) {
            if (component instanceof JButton) {
                styleButton((JButton) component);
            } else if (component instanceof JLabel) {
                component.setForeground(textColor);
            }
        }
    }

    // Method to style a button with current theme colors
    private void styleButton(JButton button) {
        Color buttonColor = isDarkMode ? new Color(60, 60, 60) : new Color(200, 200, 200);
        Color textColor = isDarkMode ? new Color(255, 255, 255) : new Color(0, 0, 0);
        button.setBackground(buttonColor);
        button.setForeground(textColor);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.PLAIN, 16));
    }

    // Helper method to get text color based on the current theme
    private Color getTextColor() {
        return isDarkMode ? new Color(255, 255, 255) : new Color(0, 0, 0);
    }

    // Method to test the database connection
    private void testDatabaseConnection() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            if (connection != null) {
                try (Statement statement = connection.createStatement()) {
                    statement.execute("SELECT 1"); // Execute a simple query to test the connection
                    JOptionPane.showMessageDialog(this, "Database connection successful!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Failed to establish a connection to the database.", "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database connection failed: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
