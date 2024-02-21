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

    // Test to ensure an empty string is handled correctly
    @Test
    public void testNextTokenWithEmptyString() throws IOException, ImagingException {
        // Arrange
        String inputString = "";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputString.getBytes());
        BasicCParser parser = new BasicCParser(inputStream);

        // Act and Assert
        assertNull(parser.nextToken(), "Should return null for an empty string");
    }

    // Test to ensure an unterminated string throws ImagingException
    @Test
    public void testNextTokenWithUnterminatedString() {
        // Arrange
        String inputString = "\"Unterminated String";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputString.getBytes());
        BasicCParser parser = new BasicCParser(inputStream);

        // Act and Assert
        assertThrows(ImagingException.class, () -> parser.nextToken(), "Should throw ImagingException for unterminated string");
    }

    // Covers following line in nextToken() function: 
    //else { throw new ImagingException("Unhandled/invalid character '" + (char) c + "' found in XPM file");}
    @Test
    public void testNextTokenWithInvalidCharacter() {
        // Arrange
        String inputString = "!";  // An invalid character
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputString.getBytes());
        BasicCParser parser = new BasicCParser(inputStream);

        // Act and Assert
        assertThrows(ImagingException.class, parser::nextToken,
                "Should throw ImagingException for unhandled/invalid character '!' in XPM file");
    }
}