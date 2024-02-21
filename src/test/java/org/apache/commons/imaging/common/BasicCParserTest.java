package org.apache.commons.imaging.common;

import org.apache.commons.imaging.ImagingException;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;

public class BasicCParserTest {

    // Test for the nextToken() method when parsing a simple string
    @Test
    public void testNextTokenWithSimpleString() throws IOException, ImagingException {
        // Arrange
        String inputString = "Hello";
        
        // Create an input stream from the input string
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputString.getBytes());
        // Create an input stream from the input string
        BasicCParser parser = new BasicCParser(inputStream);

        // Act: Call the nextToken() function
        String result = parser.nextToken();

        // Assert
        assertEquals("Hello", result, "Should correctly parse a simple string");
    }

    // Test for the nextToken() method when parsing special characters
    @Test
    public void testNextTokenWithSpecialCharacters() throws IOException, ImagingException {
        // Arrange
        String inputString = "{ * [ ] } , = ;";
        
        // Create an input stream from the input string
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputString.getBytes());
        
        // Create an instance of BasicCParser using the input stream
        BasicCParser parser = new BasicCParser(inputStream);

        // Act and Assert: call the nextToken() function
        assertEquals("{", parser.nextToken(), "Should correctly parse '{'");
        assertEquals("*", parser.nextToken(), "Should correctly parse '*'");
        assertEquals("[", parser.nextToken(), "Should correctly parse '['");
        assertEquals("]", parser.nextToken(), "Should correctly parse ']'");
        assertEquals("}", parser.nextToken(), "Should correctly parse '}'");
        assertEquals(",", parser.nextToken(), "Should correctly parse ','");
        assertEquals("=", parser.nextToken(), "Should correctly parse '='");
        assertEquals(";", parser.nextToken(), "Should correctly parse ';'");
    }
}