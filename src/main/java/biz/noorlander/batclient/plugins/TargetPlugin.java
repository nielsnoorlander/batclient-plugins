package biz.noorlander.batclient.plugins;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mythicscape.batclient.interfaces.BatClientPlugin;
import com.mythicscape.batclient.interfaces.BatClientPluginTrigger;
import com.mythicscape.batclient.interfaces.BatClientPluginUtil;
import com.mythicscape.batclient.interfaces.ParsedResult;

import biz.noorlander.batclient.handlers.TargetHandler;
import biz.noorlander.batclient.utils.ParsedResultUtil;

public class TargetPlugin extends BatClientPlugin implements BatClientPluginTrigger, BatClientPluginUtil {

	private Pattern targetCommandPattern;
	private Pattern targetSelectedPattern;
	private Pattern considerResultPattern;
	private TargetHandler targetHandler;
	
	@Override
	public void loadPlugin() {
		// target human
		// consider human
		// The final estimation is that Shilia doesn't look very dangerous.
		// You are now targetting Shilia.

		this.targetCommandPattern = Pattern.compile("^target ([-A-z 0-9',]+)$"	);
		this.considerResultPattern = Pattern.compile("The final estimation is that .+ (really easy[.]|doesn't look very dangerous[.]|reasonably competent[.]|weaker than a butterfly[.]|fair opponent[.]|quite skilled, beware[.]|nearly equal in power[.] Damn[.]|much stronger than you, beware[.]|overwhelms your mind! You PANIC!!|reduce you to minced meat, so run for your life[.])$");
		this.targetSelectedPattern = Pattern.compile("^You are now targetting ([-A-z 0-9',]+)\\.$");
		this.targetHandler = new TargetHandler(getClientGUI());
	}

	@Override
	public ParsedResult trigger(ParsedResult output) {
        String text = output.getStrippedText().trim();
        if (text.startsWith("consider ") || (text.startsWith("You take a close look at ") && text.endsWith("in comparison to yourself."))) {
        	return ParsedResultUtil.gag(output);
        } else if (text.equals("It is an inferior opponent.")) {
        	targetHandler.isInferior(true);
        } else if (text.startsWith("target ")) {
        	Matcher tcMatcher = targetCommandPattern.matcher(text);
        	if (tcMatcher.find()) {
        		targetHandler.setTargetCommand(tcMatcher.group(1));
        	}
        } else if (text.startsWith("You are now targetting ")) {
        	Matcher tsMatcher = targetSelectedPattern.matcher(text);
        	if (tsMatcher.find()) {
        		targetHandler.handleTarget(tsMatcher.group(1));
        	}
        } else if (text.startsWith("The final estimation is")) {
        	Matcher crMatcher = considerResultPattern.matcher(text);
        	if (crMatcher.find()) {
        		targetHandler.setConsiderResult(crMatcher.group(1));
        	}        	
        } else {
        	return null;
        }
    	return ParsedResultUtil.gag(output);
	}

	@Override
	public String getName() {
		return this.getClass().getSimpleName();
	}

	@Override
	public void clientExit() {
		this.targetHandler.destroyHandler();
	}


}
