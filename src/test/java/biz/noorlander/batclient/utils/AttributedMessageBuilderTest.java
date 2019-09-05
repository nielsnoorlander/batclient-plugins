package biz.noorlander.batclient.utils;

import com.mythicscape.batclient.interfaces.ParsedAttribute;
import com.mythicscape.batclient.interfaces.ParsedResult;
import javafx.util.Pair;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.font.TextAttribute;
import java.text.AttributedCharacterIterator;

import static org.junit.jupiter.api.Assertions.*;

class AttributedMessageBuilderTest {

    @Test
    void testTextOnly() {
        ParsedResult result = AttributedMessageBuilder.create().append("test").build();
        assertEquals("test", result.getStrippedText());
        assertTrue(result.getAttributes().isEmpty());
    }

    @Test
    void testSingleAttributeOnly() {
        ParsedResult result = AttributedMessageBuilder.create().append("test", new Pair<>(TextAttribute.FOREGROUND, Color.GREEN)).build();
        assertEquals("test", result.getStrippedText());
        assertEquals(1, result.getAttributes().size());
        ParsedAttribute parsedAttribute = result.getAttributes().get(0);
        assertEquals(0, parsedAttribute.getStart());
        assertEquals(4, parsedAttribute.getEnd());
        assertEquals(TextAttribute.FOREGROUND, parsedAttribute.getAttribute());
        assertEquals(Color.GREEN, parsedAttribute.getValue());
    }

    @Test
    void testTextPlusSingleAttributedText() {
        ParsedResult result = AttributedMessageBuilder.create().append("bla ").append("test", new Pair<>(TextAttribute.BACKGROUND, Color.RED)).build();
        assertEquals("bla test", result.getStrippedText());
        assertEquals(1, result.getAttributes().size());
        ParsedAttribute parsedAttribute = result.getAttributes().get(0);
        assertEquals(4, parsedAttribute.getStart());
        assertEquals(8, parsedAttribute.getEnd());
        assertEquals(TextAttribute.BACKGROUND, parsedAttribute.getAttribute());
        assertEquals(Color.RED, parsedAttribute.getValue());
    }

    @Test
    void testTextPlusDoubleAttributedText() {
        ParsedResult result = AttributedMessageBuilder.create()
                .append("bla ")
                .append("test", new Pair<>(TextAttribute.FOREGROUND, Color.GREEN), new Pair<>(TextAttribute.BACKGROUND, Color.RED))
                .append(" foo!")
                .build();
        assertEquals("bla test foo!", result.getStrippedText());
        assertEquals(2, result.getAttributes().size());
        ParsedAttribute parsedAttribute = result.getAttributes().get(0);
        assertEquals(4, parsedAttribute.getStart());
        assertEquals(8, parsedAttribute.getEnd());
        assertEquals(TextAttribute.FOREGROUND, parsedAttribute.getAttribute());
        assertEquals(Color.GREEN, parsedAttribute.getValue());
        parsedAttribute = result.getAttributes().get(1);
        assertEquals(4, parsedAttribute.getStart());
        assertEquals(8, parsedAttribute.getEnd());
        assertEquals(TextAttribute.BACKGROUND, parsedAttribute.getAttribute());
        assertEquals(Color.RED, parsedAttribute.getValue());
    }

    @Test
    void testMutlipleAttributedTexts() {
        ParsedResult result = AttributedMessageBuilder.create()
                .append("bla ")
                .append("test", new Pair<>(TextAttribute.FOREGROUND, Color.GREEN), new Pair<>(TextAttribute.BACKGROUND, Color.RED))
                .append(" foo ")
                .append("test2", new Pair<>(TextAttribute.BACKGROUND, Color.BLUE))
                .build();
        assertEquals("bla test foo test2", result.getStrippedText());
        assertEquals(3, result.getAttributes().size());
        ParsedAttribute parsedAttribute = result.getAttributes().get(0);
        assertEquals(4, parsedAttribute.getStart());
        assertEquals(8, parsedAttribute.getEnd());
        assertEquals(TextAttribute.FOREGROUND, parsedAttribute.getAttribute());
        assertEquals(Color.GREEN, parsedAttribute.getValue());
        parsedAttribute = result.getAttributes().get(1);
        assertEquals(4, parsedAttribute.getStart());
        assertEquals(8, parsedAttribute.getEnd());
        assertEquals(TextAttribute.BACKGROUND, parsedAttribute.getAttribute());
        assertEquals(Color.RED, parsedAttribute.getValue());
        parsedAttribute = result.getAttributes().get(2);
        assertEquals(13, parsedAttribute.getStart());
        assertEquals(18, parsedAttribute.getEnd());
        assertEquals(TextAttribute.BACKGROUND, parsedAttribute.getAttribute());
        assertEquals(Color.BLUE, parsedAttribute.getValue());
    }

}