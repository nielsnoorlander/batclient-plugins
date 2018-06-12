package biz.noorlander.batclient.plugins;

import biz.noorlander.batclient.handlers.CommandQueueHandler;
import biz.noorlander.batclient.services.events.ActionEvent;
import biz.noorlander.batclient.services.managers.EventServiceManager;
import com.mythicscape.batclient.interfaces.*;

public class CommandQueuePlugin extends BatClientPlugin implements BatClientPluginTrigger, BatClientPluginCommandTrigger, BatClientPluginUtil {

    private EventServiceManager eventServiceManager;
    private CommandQueueHandler commandQueueHandler;

    public void loadPlugin() {
        System.out.println("--- Loading CommandQueuePlugin ---");
        eventServiceManager = new EventServiceManager();
        commandQueueHandler = new CommandQueueHandler(eventServiceManager, this.getClientGUI());
    }

    public String getName() {
        return "CommandQueuePlugin";
    }

    public ParsedResult trigger(ParsedResult parsedResult) {
        String text = parsedResult.getStrippedText().trim();
        if (actionDone(text)) {
            ActionEvent event = new ActionEvent(ActionEvent.Type.DONE);
            eventServiceManager.getActionEventService().raiseEvent(event);
        } else if (startingAction(text)) {
            ActionEvent event = new ActionEvent(ActionEvent.Type.START);
            eventServiceManager.getActionEventService().raiseEvent(event);
        }
        return parsedResult;
    }

    private boolean startingAction(String strippedText) {
        return "You start concentrating on the skill.".equals(strippedText) ||
                "You start chanting.".equals(strippedText) ||
                "You start to channel the forces magical by chanting the runes.".equals(strippedText);
    }

    private boolean actionDone(String strippedText) {
        boolean actionDone = "You are prepared to cast the spell.".equals(strippedText) ||
                "You are prepared to do the skill.".equals(strippedText) ||
                "You are done with the chant.".equals(strippedText) ||
                strippedText.startsWith("You declare with a booming voice");
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
