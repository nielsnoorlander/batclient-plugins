package biz.noorlander.batclient.handlers;

import biz.noorlander.batclient.services.EventListener;
import biz.noorlander.batclient.services.events.ActionEvent;
import biz.noorlander.batclient.services.managers.EventServiceManager;
import biz.noorlander.batclient.utils.Attribute;
import biz.noorlander.batclient.utils.AttributedMessageBuilder;
import biz.noorlander.batclient.utils.ParsedResultUtil;
import com.google.common.collect.Lists;
import com.mythicscape.batclient.interfaces.ClientGUI;
import com.mythicscape.batclient.interfaces.ParsedResult;

import java.awt.*;
import java.util.LinkedList;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandQueueHandler extends AbstractHandler implements EventListener<ActionEvent> {

    private LinkedList<String> commandQueue;
    private boolean executionAction = false;
    private String currentCommand;
    private String currentAction;
	private Optional<String> setCurrentActionParameters;
    private Pattern actionPattern;

    public CommandQueueHandler(ClientGUI gui) {
        super(gui, "QUEUE");
        commandQueue = new LinkedList<>();
        EventServiceManager.getInstance().getActionEventService().subscribe(this);
        this.actionPattern = Pattern.compile("^(use|cast|chant|sing) [']?([-A-z ]+?)[']?([ ]+at.+)?$", Pattern.CASE_INSENSITIVE);
    }

    @Override
    public void handleEvent(ActionEvent event) {
        switch (event.getType()) {
            case START:
                executionAction = true;
                break;
            case DONE:
            case INTERRUPTED:
                executionAction = false;
                nextAction();
                break;
        }
    }
    private boolean actionCommandEcho(String text) {
        Matcher actionMatcher = actionPattern.matcher(text);
        if (actionMatcher.find()) {
            setCurrentAction(actionMatcher.group(2));
            setCurrentActionParameters(actionMatcher.group(3));
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


    private void reportToGui(String action, String command) {
        ParsedResult message = AttributedMessageBuilder.create()
                .append("## " + action + ": ", Lists.newArrayList(Attribute.fgColor(new Color(0xFF, 0x99, 0x33))))
                .append(command, Lists.newArrayList(Attribute.fgColor(new Color(0xF0, 0xF0, 0xF0))))
                .build();
        reportToGui(message);
    }



    public void queueCommand(String command) {
        commandQueue.add(command);
        reportToGui("Queue", command);
        nextAction();
    }

    private void reportToGui(String action) {
        reportToGui(action, "");
    }

    private void nextAction() {
        if (!commandQueue.isEmpty() && !executionAction) {
            currentCommand = commandQueue.pop();
            command(currentCommand);
        }
    }

    public void reportQueue() {
        int index = 1;
        for (String command : commandQueue) {
            if (index == 1) {
                reportToGui("Queued commands");
            }
            reportToGui(String.valueOf(index++), command);
        }
        if (index == 1) {
            reportToGui("Empty");
        }
    }

	public void actionDone() {
        ActionEvent event = new ActionEvent(ActionEvent.Type.DONE);
        EventServiceManager.getInstance().getActionEventService().raiseEvent(event);
	}

	public void actionStarts() {
        ActionEvent event = new ActionEvent(ActionEvent.Type.START);
        EventServiceManager.getInstance().getActionEventService().raiseEvent(event);
	}

	public void actionInterrupted() {
        ActionEvent event = new ActionEvent(ActionEvent.Type.INTERRUPTED);
        EventServiceManager.getInstance().getActionEventService().raiseEvent(event);
	}

	public void setCurrentAction(String action) {
		this.currentAction = action;
	}
	
	public void setCurrentActionParameters(String parameters) {
		this.setCurrentActionParameters = Optional.ofNullable(parameters);
	}

	@Override
	public void destroyHandler() {
		// TODO Auto-generated method stub
		
	}

    @Override
    public ParsedResult handleOutputTriggers(ParsedResult parsedResult) {
        String text = parsedResult.getStrippedText().trim();
        if (actionDone(text)) {
            actionDone();
        } else if (startingAction(text)) {
            actionStarts();
        } else if (actionInterrupted(text)) {
            actionInterrupted();
        } else if (actionCommandEcho(text)){
            ParsedResultUtil.gag(parsedResult);
        }
        return null;
    }

    @Override
    public String handleCommandTriggers(String command) {
        if (command.startsWith("q ")) {
            queueCommand(command.substring(2));
            return "";
        } else if (command.equals("qr")) {
            reportQueue();
            return "";
        }
        return null;
    }
}
