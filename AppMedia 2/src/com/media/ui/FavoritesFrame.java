package com.media.ui;

import com.media.db.DatabaseConnection;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FavoritesFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    protected String username; // Marked as protected for broader access
    protected JFrame parentFrame; // Marked as protected to ensure better usage recognition
    private DefaultTableModel tableModel;
    private JTable favoritesTable;

    public FavoritesFrame(String username, JFrame parentFrame) {
        this.username = username;
        this.parentFrame = parentFrame;

        // Set up JFrame properties
        setTitle("Your Favorites - Welcome, " + username);
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Top panel with a back button
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        // Back button
        JButton backButton = new JButton("Back");

        backButton.addActionListener(e -> {
            if (parentFrame != null) {
                parentFrame.setVisible(true);  // Show the previous frame
            }
            dispose();  // Close current frame
        });

        // Adding the username label to make it visually part of the UI
        JLabel usernameLabel = new JLabel("Logged in as: " + username);
        usernameLabel.setForeground(Color.BLACK);
        topPanel.add(usernameLabel);

        topPanel.add(backButton);
        add(topPanel, BorderLayout.NORTH);

        // Table model for displaying favorites
        tableModel = new DefaultTableModel(new String[]{"Title", "Description"}, 0) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;  // Disable editing cells
            }
        };

        favoritesTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(favoritesTable);
        add(scrollPane, BorderLayout.CENTER);

        // Remove Favorite button
        JButton removeFavoriteButton = new JButton("Remove from Favorites");
        add(removeFavoriteButton, BorderLayout.SOUTH);

        // Action listener for Remove button
        removeFavoriteButton.addActionListener(e -> removeFromFavorites());

        // Fetch favorite movies for the user
        fetchFavorites();

        // Set JFrame visibility
        setVisible(true);
    }

    private void fetchFavorites() {
        tableModel.setRowCount(0);

        String query = "SELECT film.film_id, film.title, film.description FROM favorites JOIN film ON favorites.film_id = film.film_id WHERE favorites.username = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String title = rs.getString("title");
                String description = rs.getString("description");
                tableModel.addRow(new Object[]{title, description});
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error fetching favorites: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removeFromFavorites() {
        int selectedRow = favoritesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a movie to remove.");
            return;
        }

        String title = (String) tableModel.getValueAt(selectedRow, 0);

        // Get the film_id based on the title from the database
        String queryGetFilmId = "SELECT film_id FROM film WHERE title = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmtGetFilmId = conn.prepareStatement(queryGetFilmId)) {

            stmtGetFilmId.setString(1, title);
            ResultSet rs = stmtGetFilmId.executeQuery();

            if (rs.next()) {
                int filmId = rs.getInt("film_id");

                // Remove favorite based on username and filmId
                String deleteQuery = "DELETE FROM favorites WHERE username = ? AND film_id = ?";
                try (PreparedStatement stmtDelete = conn.prepareStatement(deleteQuery)) {
                    stmtDelete.setString(1, username);
                    stmtDelete.setInt(2, filmId);
                    stmtDelete.executeUpdate();

                    JOptionPane.showMessageDialog(this, "Movie removed from favorites!");
                    fetchFavorites(); // Refresh the favorites list
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error removing from favorites: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
