package biz.noorlander.batclient.utils;

import java.awt.*;
import java.awt.font.TextAttribute;

public class Attribute {
    private TextAttribute attribute;
    private Object value;

    private Attribute(TextAttribute attribute, Object value) {
        this.attribute = attribute;
        this.value = value;
    }

    public TextAttribute getAttribute() {
        return attribute;
    }

    public Object getValue() {
        return value;
    }

    public static Attribute build(TextAttribute attribute, Object value) {
        return new Attribute(attribute, value);
    }
    public static Attribute fgColor(Color color) { return new Attribute(TextAttribute.FOREGROUND, color); }
    public static Attribute bgColor(Color color) { return new Attribute(TextAttribute.BACKGROUND, color); }
}
