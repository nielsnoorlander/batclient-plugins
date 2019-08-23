package biz.noorlander.batclient.plugins;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mythicscape.batclient.interfaces.BatClientPlugin;
import com.mythicscape.batclient.interfaces.BatClientPluginCommandTrigger;
import com.mythicscape.batclient.interfaces.BatClientPluginTrigger;
import com.mythicscape.batclient.interfaces.BatClientPluginUtil;
import com.mythicscape.batclient.interfaces.ParsedResult;

import biz.noorlander.batclient.handlers.CommandQueueHandler;
import biz.noorlander.batclient.utils.ParsedResultUtil;

public class CommandQueuePlugin extends BatClientPlugin implements BatClientPluginTrigger, BatClientPluginCommandTrigger, BatClientPluginUtil {

    private CommandQueueHandler commandQueueHandler;
    private Pattern actionPattern;

    public void loadPlugin() {
        System.out.println("--- Loading CommandQueuePlugin ---");
        commandQueueHandler = new CommandQueueHandler(this.getClientGUI());
        this.actionPattern = Pattern.compile("^(use|cast|chant|sing) [']?([-A-z ]+?)[']?([ ]+at.+)?$", Pattern.CASE_INSENSITIVE);
    }

    public String getName() {
        return "CommandQueuePlugin";
    }

    public ParsedResult trigger(ParsedResult parsedResult) {
        String text = parsedResult.getStrippedText().trim();
        if (actionDone(text)) {
        	commandQueueHandler.actionDone();
        } else if (startingAction(text)) {
        	commandQueueHandler.actionStarts();
        } else if (actionInterrupted(text)) {
        	commandQueueHandler.actionInterrupted();
        } else if (actionCommandEcho(text)){
        	ParsedResultUtil.gag(parsedResult);
        } else {
        	return null;
        }
        return parsedResult;
    }

    private boolean actionCommandEcho(String text) {
		Matcher actionMatcher = actionPattern.matcher(text);
		if (actionMatcher.find()) {
			commandQueueHandler.setCurrentAction(actionMatcher.group(2));
			commandQueueHandler.setCurrentActionParameters(actionMatcher.group(3));
			return true;
		} else { return false; }
	}

	private boolean actionInterrupted(String strippedText) {
		return "You interrupt the spell.".equals(strippedText) ||
				"Your movement prevents you from doing the skill.".equals(strippedText) ||
				"Your movement prevents you from casting the spell.".equals(strippedText) ||
				"You lose your concentration and cannot cast the spell.".equals(strippedText) ||
				"You lose your concentration and cannot do the skill.".equals(strippedText) ||
				"You break your skill attempt.".equals(strippedText) ||
				"You stop tunneling.".equals(strippedText);
	}

	private boolean startingAction(String strippedText) {
        return "You start concentrating on the skill.".equals(strippedText) ||
                "You start chanting.".equals(strippedText) ||
                "You start to channel the forces magical by chanting the runes.".equals(strippedText) ||
				"You start preparing for the tunneling.".equals(strippedText) ||
				"You start examining the support and try to figure out how to enhance them.".equals(strippedText);
    }

    private boolean actionDone(String strippedText) {
        boolean actionDone = "You are prepared to cast the spell.".equals(strippedText) ||
                "You are prepared to do the skill.".equals(strippedText) ||
                "You are done with the chant.".equals(strippedText) ||
                strippedText.startsWith("You declare with a booming voice") ||
                "You dig through the rubble and create a new extension to your mine.".equals(strippedText) ||
				"You chant the command words to drain life energy from the corpses.".equals(strippedText);
        return actionDone;
    }

    public String trigger(String command) {
        if (command.startsWith("q ")) {
            commandQueueHandler.queueCommand(command.substring(2));
            return "";
        } else if (command.equals("qr")) {
            commandQueueHandler.reportQueue();
            return "";
        }
        return command;
    }
    
    public void clientExit() {
        System.out.println("--- Exit CommandQueuePlugin ---\n");

    }
}
