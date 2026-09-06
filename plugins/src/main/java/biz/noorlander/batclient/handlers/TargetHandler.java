package biz.noorlander.batclient.handlers;

import biz.noorlander.batclient.utils.ParsedResultUtil;
import com.mythicscape.batclient.interfaces.ClientGUI;
import com.mythicscape.batclient.interfaces.ParsedResult;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TargetHandler extends AbstractHandler {
	private Pattern targetCommandPattern;
	private Pattern targetSelectedPattern;
	private Pattern considerResultPattern;


	private String targetCommand = "none";
	private String selectedTarget = "none";
	private String considerResult = "unknown";
	private boolean inferior = false;
	
	public TargetHandler(ClientGUI gui) {
		super(gui, "TARGET");
		this.targetCommandPattern = Pattern.compile("^target ([-A-z 0-9',]+)$"	);
		this.considerResultPattern = Pattern.compile("The final estimation is that .+ (really easy[.]|doesn't look very dangerous[.]|reasonably competent[.]|weaker than a butterfly[.]|fair opponent[.]|quite skilled, beware[.]|nearly equal in power[.] Damn[.]|much stronger than you, beware[.]|overwhelms your mind! You PANIC!!|reduce you to minced meat, so run for your life[.])$");
		this.targetSelectedPattern = Pattern.compile("^You are now targetting ([-A-z 0-9',]+)\\.$");
	}

	public void handleTarget(String selectedTarget) {
		setSelectedTarget(selectedTarget);
		command("@party report Target -> " + this.selectedTarget + " (" + this.targetCommand + ") = " 
				+ this.considerResult + (this.inferior ? " #inferior" : ""));
	}
	
	public String getTargetCommand() {
		return targetCommand;
	}

	public void setTargetCommand(String targetCommand) {
		this.inferior = false;
		this.targetCommand = targetCommand;
	}

	public String getSelectedTarget() {
		return selectedTarget;
	}

	public void setSelectedTarget(String selectedTarget) {
		this.selectedTarget = selectedTarget;
	}

	public void isInferior(boolean inferior) {
		this.inferior = inferior;
	}
	
	public String getConsiderResult() {
		return considerResult;
	}

	public void setConsiderResult(String considerResult) {
		this.considerResult = considerResult;
	}

	@Override
	public void destroyHandler() {
	}

	@Override
	public ParsedResult handleOutputTriggers(ParsedResult parsedResult) {
		String text = parsedResult.getStrippedText().trim();
		if (text.startsWith("consider ") || (text.startsWith("You take a close look at ") && text.endsWith("in comparison to yourself."))) {
			return ParsedResultUtil.gag(parsedResult);
		} else if (text.equals("It is an inferior opponent.")) {
			isInferior(true);
		} else if (text.startsWith("target ")) {
			Matcher tcMatcher = targetCommandPattern.matcher(text);
			if (tcMatcher.find()) {
				setTargetCommand(tcMatcher.group(1));
			}
		} else if (text.startsWith("You are now targetting ")) {
			Matcher tsMatcher = targetSelectedPattern.matcher(text);
			if (tsMatcher.find()) {
				handleTarget(tsMatcher.group(1));
			}
		} else if (text.startsWith("The final estimation is")) {
			Matcher crMatcher = considerResultPattern.matcher(text);
			if (crMatcher.find()) {
				setConsiderResult(crMatcher.group(1));
			}
		} else {
			return null;
		}
		return ParsedResultUtil.gag(parsedResult);
	}

	@Override
	public String handleCommandTriggers(String command) {
		return null;
	}
}
