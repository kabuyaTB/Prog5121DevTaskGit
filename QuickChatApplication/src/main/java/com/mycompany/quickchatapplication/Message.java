/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quickchatapplication;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

/**
 * Message class to handle messaging functionality
 * Allows users to send, store, and manage messages
 */
public class Message {
    private List<MessageData> sentMessages;
    private int totalMessagesSent;
    private Random random;

    public Message() {
        this.sentMessages = new ArrayList<>();
        this.totalMessagesSent = 0;
        this.random = new Random();
    }

    /**
     * Inner class to store message data
     */
    public static class MessageData {
        private String messageId;
        private int messageNumber;
        private String recipient;
        private String messageContent;
        private String messageHash;

        public MessageData(String messageId, int messageNumber, String recipient, 
                          String messageContent, String messageHash) {
            this.messageId = messageId;
            this.messageNumber = messageNumber;
            this.recipient = recipient;
            this.messageContent = messageContent;
            this.messageHash = messageHash;
        }

        // Getters
        public String getMessageId() { return messageId; }
        public int getMessageNumber() { return messageNumber; }
        public String getRecipient() { return recipient; }
        public String getMessageContent() { return messageContent; }
        public String getMessageHash() { return messageHash; }
    }

    /**
     * Checks if the message ID is not more than ten characters
     * @param messageId the message ID to check
     * @return true if message ID is valid (10 characters or less)
     */
    public boolean checkMessageID(String messageId) {
        return messageId != null && messageId.length() <= 10;
    }

    /**
     * Checks if the recipient cell number is correctly formatted
     * @param recipientCell the cell number to check
     * @return 1 if valid, 0 if invalid
     */
    public int checkRecipientCell(String recipientCell) {
        // Check if number starts with + and is no more than 13 characters
        if (recipientCell != null && 
            recipientCell.startsWith("+") && 
            recipientCell.length() <= 13 && 
            Pattern.compile("^\\+\\d+$").matcher(recipientCell).matches()) {
            return 1; // Valid
        }
        return 0; // Invalid
    }

    /**
     * Creates and returns the Message Hash
     * Format: first two digits of message ID : message number : first and last words in caps
     * @param messageId the message ID
     * @param messageNumber the message number
     * @param messageContent the message content
     * @return the generated hash
     */
    public String createMessageHash(String messageId, int messageNumber, String messageContent) {
        if (messageId == null || messageContent == null || messageId.length() < 2) {
            return "";
        }

        // Get first two digits of message ID
        String firstTwoDigits = messageId.substring(0, 2);
        
        // Split message into words and remove punctuation
        String[] words = messageContent.trim().split("\\s+");
        String firstWord = words.length > 0 ? words[0].replaceAll("[^a-zA-Z]", "") : "";
        String lastWord = words.length > 1 ? words[words.length - 1].replaceAll("[^a-zA-Z]", "") : firstWord;
        
        // Create hash in format: XX:Y:FIRSTLAST
        return (firstTwoDigits + ":" + messageNumber + ":" + firstWord.toUpperCase() + lastWord.toUpperCase());
    }

    /**
     * Generates a random 10-digit message ID
     * @return a random 10-digit string
     */
    public String generateMessageId() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    /**
     * Validates message length (should not exceed 250 characters)
     * @param message the message to validate
     * @return validation message
     */
    public String validateMessageLength(String message) {
        if (message == null) {
            return "Message cannot be null.";
        }
        
        if (message.length() <= 250) {
            return "Message ready to send.";
        } else {
            int excess = message.length() - 250;
            return "Message exceeds 250 characters by " + excess + ", please reduce size.";
        }
    }

    /**
     * Validates recipient cell number and returns appropriate message
     * @param recipientCell the cell number to validate
     * @return validation message
     */
    public String validateRecipientCell(String recipientCell) {
        if (checkRecipientCell(recipientCell) == 1) {
            return "Cell phone number successfully captured.";
        } else {
            return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
        }
    }

    /**
     * Handles sending message based on user choice
     * @param choice user's choice (1=Send, 2=Disregard, 3=Store)
     * @param messageId the message ID
     * @param recipient the recipient
     * @param messageContent the message content
     * @return result message
     */
    public String sentMessage(int choice, String messageId, String recipient, String messageContent) {
        switch (choice) {
            case 1: // Send Message
                totalMessagesSent++;
                String hash = createMessageHash(messageId, totalMessagesSent, messageContent);
                MessageData messageData = new MessageData(messageId, totalMessagesSent, recipient, messageContent, hash);
                sentMessages.add(messageData);
                return "Message successfully sent.";
                
            case 2: // Disregard Message
                return "Press 0 to delete message.";
                
            case 3: // Store Message
                storeMessage(messageId, recipient, messageContent);
                return "Message successfully stored.";
                
            default:
                return "Invalid choice.";
        }
    }

    /**
     * Stores message in JSON format
     * Uses ChatGPT assistance for JSON file creation
     * @param messageId the message ID
     * @param recipient the recipient
     * @param messageContent the message content
     */
    public void storeMessage(String messageId, String recipient, String messageContent) {
        /*
         * JSON storage functionality created with assistance from ChatGPT (2025).
         * Prompt: "Create a Java method to store message data in JSON format including messageId, recipient, and content"
         * The code was adapted for this messaging application.
         */
        try (FileWriter writer = new FileWriter("stored_messages.json", true)) {
            String hash = createMessageHash(messageId, totalMessagesSent + 1, messageContent);
            
            writer.write("{\n");
            writer.write("  \"messageId\": \"" + messageId + "\",\n");
            writer.write("  \"messageNumber\": " + (totalMessagesSent + 1) + ",\n");
            writer.write("  \"recipient\": \"" + recipient + "\",\n");
            writer.write("  \"messageContent\": \"" + messageContent + "\",\n");
            writer.write("  \"messageHash\": \"" + hash + "\",\n");
            writer.write("  \"timestamp\": \"" + System.currentTimeMillis() + "\"\n");
            writer.write("},\n");
            
        } catch (IOException e) {
            System.err.println("Error storing message: " + e.getMessage());
        }
    }

    /**
     * Returns a formatted string of all sent messages
     * @return string containing all message details
     */
    public String printMessages() {
        if (sentMessages.isEmpty()) {
            return "No messages sent yet.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("=== SENT MESSAGES ===\n");
        
        for (MessageData msg : sentMessages) {
            sb.append("Message ID: ").append(msg.getMessageId()).append("\n");
            sb.append("Message Hash: ").append(msg.getMessageHash()).append("\n");
            sb.append("Recipient: ").append(msg.getRecipient()).append("\n");
            sb.append("Message: ").append(msg.getMessageContent()).append("\n");
            sb.append("------------------------\n");
        }
        
        return sb.toString();
    }

    /**
     * Returns the total number of messages sent
     * @return total count of sent messages
     */
    public int returnTotalMessages() {
        return totalMessagesSent;
    }

    /**
     * Gets the list of sent messages (for testing purposes)
     * @return list of sent messages
     */
    public List<MessageData> getSentMessages() {
        return new ArrayList<>(sentMessages);
    }

    /**
     * Resets the message counter and clears sent messages (for testing)
     */
    public void resetMessages() {
        sentMessages.clear();
        totalMessagesSent = 0;
    }
}