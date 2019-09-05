package biz.noorlander.batclient.utils;

import com.mythicscape.batclient.interfaces.ParsedAttribute;
import com.mythicscape.batclient.interfaces.ParsedResult;
import javafx.util.Pair;

import java.text.AttributedCharacterIterator;
import java.util.ArrayList;

public final class AttributedMessageBuilder {
    private StringBuilder message;
    private ArrayList<ParsedAttribute> textAttributes;

    private AttributedMessageBuilder() {
        message = new StringBuilder();
        textAttributes = new ArrayList<>();
    }

    public static AttributedMessageBuilder create() {
        return new AttributedMessageBuilder();
    }

    public final AttributedMessageBuilder append(String text) {
        message.append(text);
        return this;
    }

    @SafeVarargs
    public final AttributedMessageBuilder append(String text, Pair<AttributedCharacterIterator.Attribute, Object>... attributes) {
        int currentPos = message.length();
        message.append(text);
        int newPos = message.length();

        for (Pair<AttributedCharacterIterator.Attribute,Object> attribute : attributes) {
            ParsedAttribute textAttribute = new ParsedAttribute(attribute.getKey(), attribute.getValue(), currentPos, newPos);
            textAttributes.add(textAttribute);
        }
        return this;
    }

    public ParsedResult build() {
        ParsedResult result = new ParsedResult(message.toString());
        result.setAttributes(textAttributes);
        return result;
    }
}
