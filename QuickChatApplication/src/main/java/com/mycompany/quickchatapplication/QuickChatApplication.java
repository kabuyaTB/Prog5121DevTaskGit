/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.quickchatapplication;
import javax.swing.JOptionPane;

/**
 * QuickChat Application - Main class that combines login and messaging functionality
 */
public class QuickChatApplication {
    
    public static void main(String[] args) {
        Login login = new Login();
        Message messageSystem = new Message();
        
        // Registration process (using your existing logic)
        JOptionPane.showMessageDialog(null, "=== User Registration and Login System ===\nPlease register a new account:");
        
        // Username validation loop
        boolean validUsername = false;
        while (!validUsername) {
            String username = JOptionPane.showInputDialog("Enter username:");
            if (username == null) return; // User cancelled
            
            login.setUsername(username);
            
            if (login.checkUserName()) {
                JOptionPane.showMessageDialog(null, "Username successfully captured.");
                validUsername = true;
            } else {
                JOptionPane.showMessageDialog(null, "Username is not correctly formatted, please ensure that your username contains an underscore and is no more than five characters in length.");
            }
        }
        
        // Password validation loop
        boolean validPassword = false;
        while (!validPassword) {
            String password = JOptionPane.showInputDialog("Enter password:");
            if (password == null) return; // User cancelled
            
            login.setPassword(password);
            
            if (login.checkPasswordComplexity()) {
                JOptionPane.showMessageDialog(null, "Password successfully captured.");
                validPassword = true;
            } else {
                JOptionPane.showMessageDialog(null, "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.");
            }
        }
        
        // Cell phone validation loop
        boolean validCellPhone = false;
        while (!validCellPhone) {
            String cellPhone = JOptionPane.showInputDialog("Enter South African cell phone number:");
            if (cellPhone == null) return; // User cancelled
            
            login.setCellPhoneNumber(cellPhone);
            
            if (login.checkCellPhoneNumber()) {
                JOptionPane.showMessageDialog(null, "Cell phone number successfully added.");
                validCellPhone = true;
            } else {
                JOptionPane.showMessageDialog(null, "Cell phone number incorrectly formatted or does not contain the international code.");
            }
        }
        
        // First name validation loop
        boolean validFirstName = false;
        while (!validFirstName) {
            String firstName = JOptionPane.showInputDialog("Enter your first name:");
            if (firstName == null) return; // User cancelled
            
            if (firstName != null && !firstName.trim().isEmpty()) {
                login.setFirstName(firstName);
                JOptionPane.showMessageDialog(null, "First name successfully captured.");
                validFirstName = true;
            } else {
                JOptionPane.showMessageDialog(null, "The first name does not respect the requirement. Please enter again.");
            }
        }
        
        // Last name validation loop
        boolean validLastName = false;
        while (!validLastName) {
            String lastName = JOptionPane.showInputDialog("Enter your last name:");
            if (lastName == null) return; // User cancelled
            
            if (lastName != null && !lastName.trim().isEmpty()) {
                login.setLastName(lastName);
                JOptionPane.showMessageDialog(null, "Last name successfully captured.");
                validLastName = true;
            } else {
                JOptionPane.showMessageDialog(null, "The last name does not respect the requirement. Please enter again.");
            }
        }
        
        // Register the user and display registration status
        String registerStatus = login.registerUser();
        JOptionPane.showMessageDialog(null, "Registration Status: " + registerStatus);
        
        if (!registerStatus.equals("Registration successful!")) {
            return; // Exit if registration failed
        }
        
        // Login process
        JOptionPane.showMessageDialog(null, "=== Login ===");
        String loginUsername = JOptionPane.showInputDialog("Enter username:");
        if (loginUsername == null) return;
        
        String loginPassword = JOptionPane.showInputDialog("Enter password:");
        if (loginPassword == null) return;
        
        boolean loginSuccess = login.loginUser(loginUsername, loginPassword);
        JOptionPane.showMessageDialog(null, "Login Status: " + login.returnLoginStatus());
        
        if (!loginSuccess) {
            return; // Exit if login failed - users can only send messages if logged in successfully
        }
        
        // Welcome to QuickChat - requirement 2
        JOptionPane.showMessageDialog(null, "Welcome to QuickChat.");
        
        // Get number of messages user wants to send - requirement 5
        String numMessagesStr = JOptionPane.showInputDialog("How many messages would you like to send?");
        if (numMessagesStr == null) return;
        
        int numMessages;
        try {
            numMessages = Integer.parseInt(numMessagesStr);
            if (numMessages <= 0) {
                JOptionPane.showMessageDialog(null, "Please enter a positive number.");
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Please enter a valid number.");
            return;
        }
        
        // Main application loop - requirement 4: runs until user selects quit
        boolean running = true;
        int messagesCreated = 0;
        
        while (running) {
            // Display numeric menu - requirement 3
            String menuMessage = "Choose an option:\n1. Send Messages\n2. Show recently sent messages\n3. Quit\n\nEnter your choice (1-3):";
            String choiceStr = JOptionPane.showInputDialog(menuMessage);
            
            if (choiceStr == null) {
                running = false;
                continue;
            }
            
            int choice;
            try {
                choice = Integer.parseInt(choiceStr);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Please enter a valid number (1-3).");
                continue;
            }
            
            switch (choice) {
                case 1: // Send Messages
                    if (messagesCreated < numMessages) {
                        sendMessage(messageSystem, messagesCreated + 1);
                        messagesCreated++;
                    } else {
                        JOptionPane.showMessageDialog(null, "You have reached your message limit of " + numMessages + " messages.");
                    }
                    break;
                    
                case 2: // Show recently sent messages - requirement 3b
                    JOptionPane.showMessageDialog(null, "Coming Soon.");
                    break;
                    
                case 3: // Quit - requirement 3c
                    running = false;
                    // Display total messages sent - requirement 8
                    JOptionPane.showMessageDialog(null, "Total messages sent: " + messageSystem.returnTotalMessages());
                    break;
                    
                default:
                    JOptionPane.showMessageDialog(null, "Invalid choice. Please select 1, 2, or 3.");
                    break;
            }
        }
    }
    
    /**
     * Handles the process of sending a single message
     * @param messageSystem the message system instance
     * @param messageNumber the current message number
     */
    private static void sendMessage(Message messageSystem, int messageNumber) {
        // Generate unique message ID - requirement 6
        String messageId = messageSystem.generateMessageId();
        JOptionPane.showMessageDialog(null, "Message ID generated: " + messageId);
        
        // Get recipient with validation - requirement 6
        String recipient;
        boolean validRecipient = false;
        do {
            recipient = JOptionPane.showInputDialog("Enter recipient cell number (Message " + messageNumber + "):");
            if (recipient == null) return; // User cancelled
            
            String validationResult = messageSystem.validateRecipientCell(recipient);
            if (validationResult.equals("Cell phone number successfully captured.")) {
                validRecipient = true;
                JOptionPane.showMessageDialog(null, validationResult);
            } else {
                JOptionPane.showMessageDialog(null, validationResult);
            }
        } while (!validRecipient);
        
        // Get message content with validation - requirement 6
        String messageContent;
        boolean validMessage = false;
        do {
            messageContent = JOptionPane.showInputDialog("Enter your message (max 250 characters):");
            if (messageContent == null) return; // User cancelled
            
            String validationResult = messageSystem.validateMessageLength(messageContent);
            if (validationResult.equals("Message ready to send.")) {
                validMessage = true;
                JOptionPane.showMessageDialog(null, "Message sent");
            } else {
                // Check if it's the specific error for exceeding 50 characters (as per requirement)
                if (messageContent.length() > 50) {
                    JOptionPane.showMessageDialog(null, "Please enter a message of less than 50 characters.");
                } else {
                    JOptionPane.showMessageDialog(null, validationResult);
                }
            }
        } while (!validMessage);
        
        // Generate and display message hash - requirement 6
        String messageHash = messageSystem.createMessageHash(messageId, messageNumber, messageContent);
        JOptionPane.showMessageDialog(null, "Message Hash: " + messageHash);
        
        // Ask user what to do with the message - requirement 6
        String sendMenuMessage = "What would you like to do with this message?\n1. Send Message\n2. Disregard Message\n3. Store Message to send later\n\nEnter your choice (1-3):";
        String sendChoiceStr = JOptionPane.showInputDialog(sendMenuMessage);
        
        if (sendChoiceStr == null) return; // User cancelled
        
        int sendChoice;
        try {
            sendChoice = Integer.parseInt(sendChoiceStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid choice. Message discarded.");
            return;
        }
        
        String result = messageSystem.sentMessage(sendChoice, messageId, recipient, messageContent);
        JOptionPane.showMessageDialog(null, result);
        
        // If message was sent, display full details using JOptionPane - requirement 7
        if (sendChoice == 1) { // Send Message was selected
            String messageDetails = "Message Details:\n\n" +
                "Message ID: " + messageId + "\n" +
                "Message Hash: " + messageHash + "\n" +
                "Recipient: " + recipient + "\n" +
                "Message: " + messageContent;
            
            JOptionPane.showMessageDialog(null, messageDetails);
        }
    }
}