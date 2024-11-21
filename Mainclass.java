package akatsuki;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;

public class Mainclass {

    private static LoginManager loginManager = new LoginManager();
    private static DatabaseManager DatabaseManager = new DatabaseManager();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> showLoginScreen());
    }

    private static void showLoginScreen() {
        JFrame loginFrame = new JFrame("Login");
        loginFrame.setSize(500, 300);  // Set size for the login frame
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setLocationRelativeTo(null);  // Center the window

        loginFrame.getContentPane().setBackground(new Color(30, 30, 30));  // Dark theme
        loginFrame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);  // Padding around components

        // Title Label
        JLabel titleLabel = new JLabel("Login");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.LEFT);
        gbc.gridx = 0;
        gbc.gridy = 0;
        loginFrame.add(titleLabel, gbc);

        // Username Label and Text Field
        JLabel userLabel = new JLabel("Username:");
        userLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 1;
        loginFrame.add(userLabel, gbc);

        JTextField userText = new JTextField(15);
        gbc.gridx = 1;
        loginFrame.add(userText, gbc);

        // Password Label and Text Field
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 2;
        loginFrame.add(passwordLabel, gbc);

        JPasswordField passwordText = new JPasswordField(15);
        gbc.gridx = 1;
        loginFrame.add(passwordText, gbc);

        // Login Button (styled)
        JButton loginButton = new JButton("Login");
        loginButton.setPreferredSize(new Dimension(120, 40));
        loginButton.setBackground(new Color(44, 93, 255)); // Blue color for the login button
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Arial", Font.PLAIN, 16));
        loginButton.setBorder(BorderFactory.createLineBorder(new Color(44, 93, 255), 2));
        loginButton.setFocusPainted(false);
        gbc.gridx = 1;
        gbc.gridy = 3;
        loginFrame.add(loginButton, gbc);

        // Action listener for the login button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = userText.getText();
                String password = new String(passwordText.getPassword());

                if (loginManager.authenticate(username, password)) {
                    JOptionPane.showMessageDialog(null, "Login successful!");
                    loginFrame.dispose();
                    showMainApplication();
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid username or password.");
                }
            }
        });

        loginFrame.setVisible(true);
    }

    private static void showMainApplication() {
        JFrame frame = new JFrame("Akatsuki");
        frame.setSize(800, 600);  // Set size for the main frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);  // Center the window
        frame.getContentPane().setBackground(new Color(20, 20, 20));  // Dark background

        frame.setLayout(new BorderLayout());

        // Create the panel with buttons (top part)
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 10));  // Horizontal layout for buttons
        buttonPanel.setBackground(new Color(30, 30, 30));  // Dark background for buttons

        JButton homeButton = createStyledButton("Home");
        JButton viewMediaButton = createStyledButton("View Media");
        JButton addMediaButton = createStyledButton("Add Media");
        JButton exportButton = createStyledButton("Test Database Connection");
        JButton userButton = createStyledButton("User");

        buttonPanel.add(homeButton);
        buttonPanel.add(viewMediaButton);
        buttonPanel.add(addMediaButton);
        buttonPanel.add(exportButton);
        buttonPanel.add(userButton);

        frame.add(buttonPanel, BorderLayout.NORTH);

        // Add actions to buttons
        exportButton.addActionListener(e -> testDatabaseConnection());

        frame.setVisible(true);
    }

    // Method to create styled buttons
    private static JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(120, 40));  // Slightly smaller buttons
        button.setBackground(new Color(44, 93, 255));  // Blue color for buttons
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        button.setBorder(BorderFactory.createLineBorder(new Color(44, 93, 255), 2));
        button.setFocusPainted(false);  // Removes the focus ring
        return button;
    }

    private static void testDatabaseConnection() {
        try (Connection connection = DatabaseManager.getConnection()) {
            if (connection != null && !connection.isClosed()) {
                JOptionPane.showMessageDialog(null, "Database connection successful!");
            } else {
                JOptionPane.showMessageDialog(null, "Failed to connect to the database.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Database error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}