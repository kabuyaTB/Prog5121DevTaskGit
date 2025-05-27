/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quickchatapplication;
import java.util.regex.Pattern;

/**
 * Login class to handle user registration and authentication
 */
public class Login {
    private String username;
    private String password;
    private String cellPhoneNumber;
    private String firstName;
    private String lastName;
    private boolean loggedIn = false;

    public Login(String username, String password, String cellPhoneNumber, String firstName, String lastName) {
        this.username = username;
        this.password = password;
        this.cellPhoneNumber = cellPhoneNumber;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    // Default constructor for Login class
    public Login() {
        this.username = "";
        this.password = "";
        this.cellPhoneNumber = "";
        this.firstName = "";
        this.lastName = "";
    }

    /**
     * Checks if the username is correctly formatted
     * @return true if username contains an underscore and is no more than 5 characters long
     */
    public boolean checkUserName() {
        return username.contains("_") && username.length() <= 5;
    }

    /**
     * Checks if the password meets complexity requirements
     * @return true if password is at least 8 characters, contains a capital letter, a number, and a special character
     */
    public boolean checkPasswordComplexity() {
        // Check for minimum length
        if (password.length() < 8) {
            return false;
        }
        
        // Check for capital letter
        if (!Pattern.compile("[A-Z]").matcher(password).find()) {
            return false;
        }
        
        // Check for number
        if (!Pattern.compile("[0-9]").matcher(password).find()) {
            return false;
        }
        
        // Check for special character
        if (!Pattern.compile("[^a-zA-Z0-9]").matcher(password).find()) {
            return false;
        }
        
        return true;
    }

    /**
     * Checks if the cell phone number is correctly formatted
     * @return true if the cell phone number starts with + and is no more than 13 characters long
     */
    public boolean checkCellPhoneNumber() {
        // Cell phone number should start with + (international code) and be at most 13 characters
        // South African numbers are typically +27 followed by 9 digits
        return cellPhoneNumber.startsWith("+") && 
               cellPhoneNumber.length() <= 13 && 
               Pattern.compile("^\\+\\d+$").matcher(cellPhoneNumber).matches();
        /*
        To help create a regular expression-based cellphone number checker in Java, I used OpenAI's ChatGPT (2025). I prompted it with:
        "Write a Java method using regex to validate South African cellphone numbers that start with +27 followed by number which are no mor than 9 digits long."
        The code provided was adapted and used for the assignment.
        */
    }

    /**
     * Registers a user checking username, password, and cell phone number
     * @return String message indicating registration status
     */
    public String registerUser() {
        if (!checkUserName()) {
            return "Username is not correctly formatted, please ensure that your username contains an underscore and is no more than five characters in length.";
        }
        
        if (!checkPasswordComplexity()) {
            return "Password is not correctly formatted, please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
        }
        
        if (!checkCellPhoneNumber()) {
            return "Cell number is incorrectly formatted or does not contain an international code, please correct the number and try again.";
        }
        
        return "Registration successful!";
    }

    /**
     * Attempts to log in a user with the provided credentials
     * @param enteredUsername the username entered by the user
     * @param enteredPassword the password entered by the user
     * @return true if login successful, false otherwise
     */
    public boolean loginUser(String enteredUsername, String enteredPassword) {
        if (enteredUsername.equals(username) && enteredPassword.equals(password)) {
            loggedIn = true;
            return true;
        }
        return false;
    }

    /**
     * Returns the login status message
     * @return String with welcome message or error message
     */
    public String returnLoginStatus() {
        if (loggedIn) {
            return "Welcome " + firstName + ", " + lastName + " it is great to see you again.";
        } else {
            return "Username or password incorrect, please try again.";
        }
    }

    // Getters and setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCellPhoneNumber() {
        return cellPhoneNumber;
    }

    public void setCellPhoneNumber(String cellPhoneNumber) {
        this.cellPhoneNumber = cellPhoneNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }
}