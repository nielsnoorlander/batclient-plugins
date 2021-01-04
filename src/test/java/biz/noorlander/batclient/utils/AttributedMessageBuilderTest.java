package biz.noorlander.batclient.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Color;
import java.awt.font.TextAttribute;

import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;

import com.mythicscape.batclient.interfaces.ParsedAttribute;
import com.mythicscape.batclient.interfaces.ParsedResult;

class AttributedMessageBuilderTest {

    @Test
    void testTextOnly() {
        ParsedResult result = AttributedMessageBuilder.create().append("test").build();
        assertEquals("test" + System.lineSeparator(), result.getStrippedText());
        assertTrue(result.getAttributes().isEmpty());
    }

    @Test
    void testSingleAttributeOnly() {
        ParsedResult result = AttributedMessageBuilder.create().append("test", Lists.newArrayList(Attribute.fgColor(Color.GREEN))).build();
        assertEquals("test" + System.lineSeparator(), result.getStrippedText());
        assertEquals(1, result.getAttributes().size());
        ParsedAttribute parsedAttribute = result.getAttributes().get(0);
        assertEquals(0, parsedAttribute.getStart());
        assertEquals(4, parsedAttribute.getEnd());
        assertEquals(TextAttribute.FOREGROUND, parsedAttribute.getAttribute());
        assertEquals(Color.GREEN, parsedAttribute.getValue());
    }

    @Test
    void testTextPlusSingleAttributedText() {
        ParsedResult result = AttributedMessageBuilder.create().append("bla ").append("test", Lists.newArrayList(Attribute.bgColor(Color.RED))).build();
        assertEquals("bla test" + System.lineSeparator(), result.getStrippedText());
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
                .append("test", Lists.newArrayList(Attribute.fgColor(Color.GREEN), Attribute.bgColor(Color.RED)))
                .append(" foo!")
                .build();
        assertEquals("bla test foo!" + System.lineSeparator(), result.getStrippedText());
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
                .append("test", Lists.newArrayList(Attribute.fgColor(Color.GREEN), Attribute.bgColor(Color.RED)))
                .append(" foo ")
                .append("test2", Lists.newArrayList(Attribute.bgColor(Color.BLUE)))
                .build();
        assertEquals("bla test foo test2" + System.lineSeparator(), result.getStrippedText());
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