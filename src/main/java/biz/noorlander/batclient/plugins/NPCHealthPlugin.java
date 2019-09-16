package biz.noorlander.batclient.plugins;

import biz.noorlander.batclient.utils.CommonPatterns;
import biz.noorlander.batclient.utils.ParsedResultUtil;
import com.mythicscape.batclient.interfaces.BatClientPlugin;
import com.mythicscape.batclient.interfaces.BatClientPluginTrigger;
import com.mythicscape.batclient.interfaces.ParsedResult;

import java.awt.*;
import java.awt.font.TextAttribute;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NPCHealthPlugin extends BatClientPlugin implements BatClientPluginTrigger {

	Pattern shapePattern;
	private Map<String, Color> shapeColors = new HashMap<>();;
	private Map<String, Color> stunColors = new HashMap<>();;

	@Override
	public void loadPlugin() {
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
	public ParsedResult trigger(ParsedResult output) {
        String text = output.getStrippedText().trim();
        Matcher shapeMatcher = shapePattern.matcher(text);
        if (shapeMatcher.find()) {
        	System.out.println("SHAPE: " + shapeMatcher.group(2) + ", start: " + shapeMatcher.start(2) + ", end: " + shapeMatcher.end(2));
			output.getAttributes().clear();
			output.addAttribute(TextAttribute.FOREGROUND, getShapeColor(shapeMatcher.group(2)), shapeMatcher.start(2), shapeMatcher.end(2));
			if (shapeMatcher.group(5) != null) {
				output.addAttribute(TextAttribute.FOREGROUND, getStunColor(shapeMatcher.group(5)), shapeMatcher.start(5), shapeMatcher.end(5));
			}
        	return output;
		} else {
			return null;
		}
	}

	private Color getShapeColor(String shapeText) {
		return shapeColors.get(shapeText);
	}

	private Color getStunColor(String stunText) {
		return stunColors.get(stunText);
	}

	@Override
	public String getName() {
		return this.getClass().getSimpleName();
	}

}
