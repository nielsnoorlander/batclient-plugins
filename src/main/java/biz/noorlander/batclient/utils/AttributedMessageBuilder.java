package biz.noorlander.batclient.utils;

import java.awt.Color;
import java.awt.font.TextAttribute;
import java.util.ArrayList;
import java.util.Optional;

import com.mythicscape.batclient.interfaces.ParsedAttribute;
import com.mythicscape.batclient.interfaces.ParsedResult;

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

    public final AttributedMessageBuilder append(String text, Optional<Color> foreground, Optional<Color> background) {
        int currentPos = message.length();
        message.append(text);
        int newPos = message.length();
        foreground.ifPresent(color -> textAttributes.add(buildForegroundAttribute(color, currentPos, newPos)));
        background.ifPresent(color -> textAttributes.add(buildBackgroundAttribute(color, currentPos, newPos)));
        return this;
    }

	private ParsedAttribute buildForegroundAttribute(Color foreground, int currentPos, int newPos) {
		return new ParsedAttribute(TextAttribute.FOREGROUND, foreground, currentPos, newPos);
	}

	private ParsedAttribute buildBackgroundAttribute(Color foreground, int currentPos, int newPos) {
		return new ParsedAttribute(TextAttribute.BACKGROUND, foreground, currentPos, newPos);
	}

    public ParsedResult build() {
    	message.append(System.lineSeparator());
        ParsedResult result = new ParsedResult(message.toString());
        result.setAttributes(textAttributes);
        return result;
    }
}
