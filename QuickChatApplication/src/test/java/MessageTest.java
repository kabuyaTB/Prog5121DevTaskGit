/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.mycompany.quickchatapplication.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Message class.
 * Tests all messaging functionality including validation, hash creation, and message handling.
 */
public class MessageTest {

    private Message message;

    @BeforeEach
    public void setUp() {
        message = new Message();
    }

    // ----------------------------
    // Validation Tests
    // ----------------------------

    @Test
    @DisplayName("Validate message length - success")
    public void testValidateMessageLength_Success() {
        String msg = "Hi Mike, can you join us for dinner tonight";
        String result = message.validateMessageLength(msg);
        assertEquals("Message ready to send.", result);
    }

    @Test
    @DisplayName("Validate message length - failure (over 250 characters)")
    public void testValidateMessageLength_Failure() {
        StringBuilder longMessage = new StringBuilder();
        for (int i = 0; i < 26; i++) {
            longMessage.append("This is a very long message that exceeds the limit. ");
        }
        String result = message.validateMessageLength(longMessage.toString());
        assertTrue(result.contains("Message exceeds 250 characters by"));
    }

    @Test
    @DisplayName("Validate recipient cell - success")
    public void testValidateRecipientCell_Success() {
        String result = message.validateRecipientCell("+27718693002");
        assertEquals("Cell phone number successfully captured.", result);
    }

    @Test
    @DisplayName("Validate recipient cell - failure")
    public void testValidateRecipientCell_Failure() {
        String result = message.validateRecipientCell("08575975889");
        assertEquals("Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.", result);
    }

    @Test
    @DisplayName("Check recipient cell - valid")
    public void testCheckRecipientCell_Valid() {
        int result = message.checkRecipientCell("+27718693002");
        assertEquals(1, result);
    }

    @Test
    @DisplayName("Check recipient cell - invalid")
    public void testCheckRecipientCell_Invalid() {
        int result = message.checkRecipientCell("08575975889");
        assertEquals(0, result);
    }

    // ----------------------------
    // Message ID Tests
    // ----------------------------

    @Test
    @DisplayName("Check message ID - valid 10 chars")
    public void testCheckMessageID_Valid() {
        assertTrue(message.checkMessageID("1234567890"));
    }

    @Test
    @DisplayName("Check message ID - invalid too long")
    public void testCheckMessageID_Invalid() {
        assertFalse(message.checkMessageID("12345678901"));
    }

    @Test
    @DisplayName("Check message ID - valid short")
    public void testCheckMessageID_ValidShorter() {
        assertTrue(message.checkMessageID("12345"));
    }

    @Test
    @DisplayName("Generate message ID")
    public void testGenerateMessageId() {
        String id = message.generateMessageId();
        assertNotNull(id);
        assertEquals(10, id.length());
        assertTrue(id.matches("\\d{10}"));
        System.out.println("Generated ID: " + id);
    }

    // ----------------------------
    // Message Hash Creation Tests
    // ----------------------------

    @Test
    public void testCreateMessageHash_TestData1() {
        String hash = message.createMessageHash("1234567890", 0, "Hi Mike, can you join us for dinner tonight");
        assertEquals("12:0:HITONIGHT", hash);
    }

    @Test
    public void testCreateMessageHash_TestData2() {
        String hash = message.createMessageHash("9876543210", 1, "Hi Keegan, did you receive the payment?");
        assertEquals("98:1:HIPAYMENT", hash);
    }

    @Test
    public void testCreateMessageHash_SingleWord() {
        String hash = message.createMessageHash("1111111111", 2, "Hello");
        assertEquals("11:2:HELLOHELLO", hash);
    }

    @Test
    public void testCreateMessageHash_EdgeCases() {
        assertEquals("12:0:HIYOU", message.createMessageHash("1234567890", 0, "Hi! How are you?"));
        assertEquals("12:1:HELLOWORLD", message.createMessageHash("1234567890", 1, "  Hello   world  "));
    }

    @Test
    public void testCreateMessageHash_MultipleScenarios() {
        String[][] data = {
            {"1234567890", "0", "Hi Mike, can you join us for dinner tonight", "12:0:HITONIGHT"},
            {"9876543210", "1", "Hi Keegan, did you receive the payment?", "98:1:HIPAYMENT"},
            {"1111111111", "2", "Hello world", "11:2:HELLOWORLD"},
            {"2222222222", "3", "Testing", "22:3:TESTINGTESTING"}
        };
        for (String[] row : data) {
            String hash = message.createMessageHash(row[0], Integer.parseInt(row[1]), row[2]);
            assertEquals(row[3], hash, "Failed for: " + row[2]);
        }
    }

    // ----------------------------
    // Message Sending Tests
    // ----------------------------

    @Test
    public void testSentMessage_SendMessage() {
        String result = message.sentMessage(1, "1234567890", "+27718693002", "Test message");
        assertEquals("Message successfully sent.", result);
        assertEquals(1, message.returnTotalMessages());
    }

    @Test
    public void testSentMessage_DisregardMessage() {
        String result = message.sentMessage(2, "1234567890", "+27718693002", "Test message");
        assertEquals("Press 0 to delete message.", result);
        assertEquals(0, message.returnTotalMessages());
    }

    @Test
    public void testSentMessage_StoreMessage() {
        String result = message.sentMessage(3, "1234567890", "+27718693002", "Test message");
        assertEquals("Message successfully stored.", result);
        assertEquals(0, message.returnTotalMessages());
    }

    @Test
    public void testPrintMessages_NoMessages() {
        String result = message.printMessages();
        assertEquals("No messages sent yet.", result);
    }

    @Test
    public void testPrintMessages_WithMessages() {
        message.sentMessage(1, "1234567890", "+27718693002", "Test message");
        String output = message.printMessages();
        assertTrue(output.contains("Message ID: 1234567890"));
        assertTrue(output.contains("Recipient: +27718693002"));
        assertTrue(output.contains("Message: Test message"));
    }

    @Test
    public void testReturnTotalMessages() {
        assertEquals(0, message.returnTotalMessages());
        message.sentMessage(1, "1111111111", "+27111111111", "Hello");
        message.sentMessage(1, "2222222222", "+27222222222", "World");
        assertEquals(2, message.returnTotalMessages());
    }

    // ----------------------------
    // Full Flow Test
    // ----------------------------

    @Test
    public void testCompleteMessageFlow_TestData() {
        message.resetMessages();

        // Message 1
        String id1 = "1234567890";
        String to1 = "+27718693002";
        String text1 = "Hi Mike, can you join us for dinner tonight";

        assertEquals("Cell phone number successfully captured.", message.validateRecipientCell(to1));
        assertEquals("Message ready to send.", message.validateMessageLength(text1));
        assertEquals("12:1:HITONIGHT", message.createMessageHash(id1, 1, text1));
        assertEquals("Message successfully sent.", message.sentMessage(1, id1, to1, text1));

        // Message 2 with invalid number
        String to2 = "08575975889";
        assertEquals("Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.", message.validateRecipientCell(to2));
    }
}