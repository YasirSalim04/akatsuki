package com.media.ui;

import com.media.db.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MovieDetailFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    private final int filmId;
    private final String username;

    public MovieDetailFrame(int filmId, String username) {
        this.filmId = filmId;
        this.username = username;

        // Set up JFrame properties
        setTitle("Movie Details");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Detail panel to show movie information
        JPanel detailPanel = new JPanel();
        detailPanel.setLayout(new GridLayout(0, 1));
        add(detailPanel, BorderLayout.CENTER);

        // Fetch movie details from the database
        loadMovieDetails(detailPanel);

        // Add Favorite Button
        JButton favoriteButton = new JButton("Add to Favorites");
        add(favoriteButton, BorderLayout.SOUTH);

        // Add to favorites action listener
        favoriteButton.addActionListener(e -> addToFavorites());

        // Set JFrame visibility
        setVisible(true);
    }

    private void loadMovieDetails(JPanel detailPanel) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT title, description, release_year, rating FROM film WHERE film_id = ?")) {

            stmt.setInt(1, filmId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String title = rs.getString("title");
                String description = rs.getString("description");
                String releaseYear = rs.getString("release_year");
                String rating = rs.getString("rating");

                detailPanel.add(new JLabel("Title: " + title));
                detailPanel.add(new JLabel("Description: " + description));
                detailPanel.add(new JLabel("Release Year: " + releaseYear));
                detailPanel.add(new JLabel("Rating: " + rating));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error fetching movie details: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace(); // Log the error for debugging purposes
        }
    }

    private void addToFavorites() {
        System.out.println("Attempting to add movie to favorites."); // Debug message
        String query = "INSERT INTO favorites (username, film_id) VALUES (?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setInt(2, filmId);
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("Movie successfully added to favorites."); // Debug message
                JOptionPane.showMessageDialog(this, "Movie added to favorites!");
            } else {
                System.out.println("No rows affected."); // Debug message in case no rows are affected
                JOptionPane.showMessageDialog(this, "Failed to add movie to favorites.");
            }

        } catch (SQLException e) {
            if (e.getSQLState().equals("23000")) { // Assuming 23000 is the SQL state for unique constraint violation
                JOptionPane.showMessageDialog(this, "Movie is already in your favorites!", "Warning", JOptionPane.WARNING_MESSAGE);
                System.out.println("Movie already in favorites: " + e.getMessage()); // Debug message for duplicates
            } else {
                JOptionPane.showMessageDialog(this, "Error adding to favorites: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace(); // Log the error for debugging purposes
            }
        }
    }
}
