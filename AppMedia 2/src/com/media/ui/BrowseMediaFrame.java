package com.media.ui;

import com.media.db.DatabaseConnection;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement; // Import for Statement class

public class BrowseMediaFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    protected String username; // Store the username of the logged-in user
    protected JFrame parentFrame; // Reference to the previous frame
    private DefaultTableModel tableModel;
    private JComboBox<String> yearFilterComboBox;
    private JComboBox<String> actorFilterComboBox;
    private JComboBox<String> categoryFilterComboBox;
    private JTextField searchField;

    public BrowseMediaFrame(String username, JFrame parentFrame) {
        this.username = username;
        this.parentFrame = parentFrame;

        // Set up JFrame properties
        setTitle("Browse Media - Welcome, " + username);
        setSize(800, 600); // Updated size to fit more content
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Make the window fullscreen
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Top panel for search and back button
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        // Back button
        JButton backButton = new JButton("Back");

        // Action listener for back button
        backButton.addActionListener(e -> {
            if (parentFrame != null) {
                parentFrame.setVisible(true); // Show the previous frame
            }
            dispose(); // Close current frame
        });

        // Search field and button
        searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");

        // Adding components to top panel
        topPanel.add(backButton);
        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setForeground(Color.BLACK);
        topPanel.add(searchLabel);
        topPanel.add(searchField);
        topPanel.add(searchButton);

        // Add filter components (Year, Actor, Category)
        yearFilterComboBox = new JComboBox<>();
        yearFilterComboBox.addItem("All Years");
        loadYears(); // Load available years into the combo box
        topPanel.add(new JLabel("Year:"));
        topPanel.add(yearFilterComboBox);

        actorFilterComboBox = new JComboBox<>();
        actorFilterComboBox.addItem("All Actors");
        loadActors(); // Load available actors into the combo box
        topPanel.add(new JLabel("Actor:"));
        topPanel.add(actorFilterComboBox);

        categoryFilterComboBox = new JComboBox<>();
        categoryFilterComboBox.addItem("All Categories");
        loadCategories(); // Load available categories into the combo box
        topPanel.add(new JLabel("Category:"));
        topPanel.add(categoryFilterComboBox);

        add(topPanel, BorderLayout.NORTH);

        // Table model and table for displaying movies (with film_id)
        tableModel = new DefaultTableModel(new String[]{"ID", "Title", "Description"}, 0) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable movieTable = new JTable(tableModel);
        movieTable.removeColumn(movieTable.getColumnModel().getColumn(0)); // Hide the ID column from the user
        JScrollPane scrollPane = new JScrollPane(movieTable); // Make the table scrollable
        add(scrollPane, BorderLayout.CENTER);

        // Fetch all movies from the database initially
        fetchMovies();

        // Add action listener for search button
        searchButton.addActionListener(e -> fetchMovies());

        // Add action listener for filter combo boxes
        yearFilterComboBox.addActionListener(e -> fetchMovies());
        actorFilterComboBox.addActionListener(e -> fetchMovies());
        categoryFilterComboBox.addActionListener(e -> fetchMovies());

        // Add mouse listener for double-clicking a row
        movieTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedRow = movieTable.getSelectedRow();
                    if (selectedRow != -1) {
                        int filmId = (int) tableModel.getValueAt(selectedRow, 0); // Get the hidden film_id
                        String title = (String) tableModel.getValueAt(selectedRow, 1);
                        String description = (String) tableModel.getValueAt(selectedRow, 2);
                        showMovieDetails(filmId, title, description);
                    }
                }
            }
        });

        setVisible(true);
    }

    // Fetch all movies from the database with the selected filters
    private void fetchMovies() {
        tableModel.setRowCount(0);

        String searchQuery = searchField.getText().trim();
        String selectedYear = (String) yearFilterComboBox.getSelectedItem();
        String selectedActor = (String) actorFilterComboBox.getSelectedItem();
        String selectedCategory = (String) categoryFilterComboBox.getSelectedItem();

        StringBuilder query = new StringBuilder("SELECT DISTINCT film.film_id, film.title, film.description FROM film ");
        query.append("LEFT JOIN film_actor ON film.film_id = film_actor.film_id ");
        query.append("LEFT JOIN actor ON film_actor.actor_id = actor.actor_id ");
        query.append("LEFT JOIN film_category ON film.film_id = film_category.film_id ");
        query.append("LEFT JOIN category ON film_category.category_id = category.category_id ");

        query.append("WHERE 1=1 "); // Dummy condition to simplify appending 'AND'

        if (!searchQuery.isEmpty()) {
            query.append("AND film.title LIKE ? ");
        }
        if (!"All Years".equals(selectedYear)) {
            query.append("AND film.release_year = ? ");
        }
        if (!"All Actors".equals(selectedActor)) {
            query.append("AND CONCAT(actor.first_name, ' ', actor.last_name) = ? ");
        }
        if (!"All Categories".equals(selectedCategory)) {
            query.append("AND category.name = ? ");
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query.toString())) {

            int paramIndex = 1;

            if (!searchQuery.isEmpty()) {
                stmt.setString(paramIndex++, "%" + searchQuery + "%");
            }
            if (!"All Years".equals(selectedYear)) {
                stmt.setString(paramIndex++, selectedYear);
            }
            if (!"All Actors".equals(selectedActor)) {
                stmt.setString(paramIndex++, selectedActor);
            }
            if (!"All Categories".equals(selectedCategory)) {
                stmt.setString(paramIndex++, selectedCategory);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int filmId = rs.getInt("film_id");
                    String title = rs.getString("title");
                    String description = rs.getString("description");
                    tableModel.addRow(new Object[]{filmId, title, description});
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error fetching movies: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Load years into the year combo box from the database
    private void loadYears() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT DISTINCT release_year FROM film ORDER BY release_year")) {

            while (rs.next()) {
                yearFilterComboBox.addItem(rs.getString("release_year"));
            }

        } catch (SQLException e) {
            System.err.println("Error loading years: " + e.getMessage());
        }
    }

    // Load actors into the actor combo box from the database
    private void loadActors() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT DISTINCT first_name, last_name FROM actor ORDER BY first_name, last_name")) {

            while (rs.next()) {
                String actorName = rs.getString("first_name") + " " + rs.getString("last_name");
                actorFilterComboBox.addItem(actorName);
            }

        } catch (SQLException e) {
            System.err.println("Error loading actors: " + e.getMessage());
        }
    }

    // Load categories into the category combo box from the database
    private void loadCategories() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT DISTINCT name FROM category ORDER BY name")) {

            while (rs.next()) {
                categoryFilterComboBox.addItem(rs.getString("name"));
            }

        } catch (SQLException e) {
            System.err.println("Error loading categories: " + e.getMessage());
        }
    }

    private void showMovieDetails(int filmId, String title, String description) {
        new MovieDetailFrame(filmId, username); // Pass the filmId and username to MovieDetailFrame
    }
}
