package com.media.ui;

import com.media.auth.AuthManager;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        AuthManager authManager = new AuthManager();

        boolean running = true;

        while (running) {
            System.out.println("\nWelcome to Media Stream App");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter username: ");
                    String regUsername = scanner.nextLine();
                    System.out.print("Enter password: ");
                    String regPassword = scanner.nextLine();
                    System.out.print("Enter email: ");
                    String regEmail = scanner.nextLine();

                    boolean registrationSuccess = authManager.registerUser(regUsername, regPassword, regEmail);
                    if (registrationSuccess) {
                        System.out.println("Registration successful! Please proceed to login.");
                    } else {
                        System.out.println("Registration failed. Username may already exist.");
                    }
                    break;

                case 2:
                    System.out.print("Enter username: ");
                    String loginUsername = scanner.nextLine();
                    System.out.print("Enter password: ");
                    String loginPassword = scanner.nextLine();

                    boolean loginSuccess = authManager.loginUser(loginUsername, loginPassword);
                    if (loginSuccess) {
                        System.out.println("Login successful! Welcome to Media Stream App.");
                        // Here you might want to direct to the main app functionality
                    } else {
                        System.out.println("Invalid credentials. Please try again.");
                    }
                    break;

                case 3:
                    running = false;
                    System.out.println("Exiting... Goodbye!");
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        scanner.close();
    }
}
