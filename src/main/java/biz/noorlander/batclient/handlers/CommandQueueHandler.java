package biz.noorlander.batclient.handlers;

import java.awt.Color;
import java.util.LinkedList;
import java.util.Optional;

import com.mythicscape.batclient.interfaces.ClientGUI;
import com.mythicscape.batclient.interfaces.ParsedResult;

import biz.noorlander.batclient.services.EventListener;
import biz.noorlander.batclient.services.events.ActionEvent;
import biz.noorlander.batclient.services.managers.EventServiceManager;
import biz.noorlander.batclient.utils.AttributedMessageBuilder;

public class CommandQueueHandler extends AbstractHandler implements EventListener<ActionEvent> {

    private LinkedList<String> commandQueue;
    private boolean executionAction = false;
    private String currentCommand;
    private String currentAction;
	private Optional<String> setCurrentActionParameters;

    public CommandQueueHandler(ClientGUI gui) {
        super(gui, "QUEUE");
        commandQueue = new LinkedList<>();
        EventServiceManager.getInstance().getActionEventService().subscribe(this);
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

    private void reportToGui(String action, String command) {
        ParsedResult message = AttributedMessageBuilder.create()
                .append("## " + action + ": ", Optional.of(new Color(0xFF, 0x99, 0x33)), Optional.empty())
                .append(command, Optional.of(new Color(0xF0, 0xF0, 0xF0)), Optional.empty())
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
}
