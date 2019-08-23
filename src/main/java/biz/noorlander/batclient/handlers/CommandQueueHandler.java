package biz.noorlander.batclient.handlers;

import biz.noorlander.batclient.services.EventListener;
import biz.noorlander.batclient.services.events.ActionEvent;
import biz.noorlander.batclient.services.managers.EventServiceManager;
import com.mythicscape.batclient.interfaces.ClientGUI;

import java.util.LinkedList;
import java.util.Optional;
import java.util.regex.Pattern;

public class CommandQueueHandler implements EventListener<ActionEvent> {

    private LinkedList<String> commandQueue;
    private boolean executionAction = false;
    private String currentCommand;
    private String currentAction;
    private ClientGUI gui;
	private Optional<String> setCurrentActionParameters;

    public CommandQueueHandler(ClientGUI gui) {
        commandQueue = new LinkedList<>();
        this.gui = gui;
        EventServiceManager.getInstance().getActionEventService().subscribe(this);
    }

    @Override
    public void handleEvent(ActionEvent event) {
        switch (event.getType()) {
            case START:
                executionAction = true;
                break;
            case DONE:
                executionAction = false;
                nextAction();
                break;
            case INTERRUPTED:
                executionAction = false;
                nextAction();
        }
    }

    public void queueCommand(String command) {
        commandQueue.add(command);
        reportToGui("Queue", command);
        nextAction();
    }

    private void reportToGui(String action, String command) {
        gui.printText("Generic", "## " + action + ": ", "FF9933");
        gui.printText("Generic", command + "\n", "F0F0F0");
    }

    private void reportToGui(String action) {
        reportToGui(action, "");
    }

    private void nextAction() {
        if (!commandQueue.isEmpty() && !executionAction) {
            currentCommand = commandQueue.pop();
            this.gui.doCommand(currentCommand);
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
}
