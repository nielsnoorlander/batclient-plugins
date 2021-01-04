package biz.noorlander.batclient.handlers;

import biz.noorlander.batclient.utils.CommonPatterns;
import com.mythicscape.batclient.interfaces.ClientGUI;
import com.mythicscape.batclient.interfaces.ParsedResult;

import java.awt.*;
import java.awt.font.TextAttribute;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NPCHealthHandler extends AbstractHandler {
    Pattern shapePattern;
    private final Map<String, Color> shapeColors = new HashMap<>();;
    private final Map<String, Color> stunColors = new HashMap<>();;

    public NPCHealthHandler(ClientGUI clientGUI) {
        super(clientGUI, "NPCHEALTH");
        this.shapePattern = Pattern.compile("^[A-Z]" + CommonPatterns.PATTERN_NPC_NAME
                + " is (in )?(excellent shape|a good shape|slightly hurt|noticeably hurt|not in a good shape|bad shape|very bad shape|near death)" +
                "( [(][0-9]+[%][)])?( and (stunned|Stunned|STUNNED|[*]STUNNED[*]))?\\.$");
        this.shapeColors.put("excellent shape", new Color(20, 253, 0));
        this.shapeColors.put("a good shape", new Color(54, 169, 19));
        this.shapeColors.put("slightly hurt", new Color(58, 107, 46));
        this.shapeColors.put("noticeably hurt", new Color(220, 198, 70));
        this.shapeColors.put("not in a good shape", new Color(186, 134, 42));
        this.shapeColors.put("bad shape", new Color(255, 165, 53));
        this.shapeColors.put("very bad shape", new Color(255, 67, 36));
        this.shapeColors.put("near death", new Color(255, 0, 32));
        this.stunColors.put("stunned", new Color(164, 86, 255));
        this.stunColors.put("Stunned", new Color(207, 101, 255));
        this.stunColors.put("STUNNED", new Color(212, 60, 255));
        this.stunColors.put("*STUNNED*", new Color(255, 62, 255));
    }

    @Override
    public void destroyHandler() {

    }

    @Override
    public ParsedResult handleOutputTriggers(ParsedResult parsedResult) {
        String text = parsedResult.getStrippedText().trim();
        Matcher shapeMatcher = shapePattern.matcher(text);
        if (shapeMatcher.find()) {
            System.out.println("SHAPE: " + shapeMatcher.group(2) + ", start: " + shapeMatcher.start(2) + ", end: " + shapeMatcher.end(2));
            parsedResult.getAttributes().clear();
            parsedResult.addAttribute(TextAttribute.FOREGROUND, getShapeColor(shapeMatcher.group(2)), shapeMatcher.start(2), shapeMatcher.end(2));
            if (shapeMatcher.group(5) != null) {
                parsedResult.addAttribute(TextAttribute.FOREGROUND, getStunColor(shapeMatcher.group(5)), shapeMatcher.start(5), shapeMatcher.end(5));
            }
            return parsedResult;
        } else {
            return null;
        }
    }

    @Override
    public String handleCommandTriggers(String command) {
        return null;
    }

    private Color getShapeColor(String shapeText) {
        return shapeColors.get(shapeText);
    }

    private Color getStunColor(String stunText) {
        return stunColors.get(stunText);
    }

}
