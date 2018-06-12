package biz.noorlander.batclient.handlers;

import biz.noorlander.batclient.services.EventListener;
import biz.noorlander.batclient.services.events.ActionEvent;
import biz.noorlander.batclient.services.managers.EventServiceManager;
import com.mythicscape.batclient.interfaces.ClientGUI;

import java.util.LinkedList;

public class CommandQueueHandler implements EventListener<ActionEvent> {

    private LinkedList<String> commandQueue;
    private boolean executionAction = false;
    private String currentCommand;
    private ClientGUI gui;

    public CommandQueueHandler(EventServiceManager eventServiceManager, ClientGUI gui) {
        commandQueue = new LinkedList<>();
        this.gui = gui;
        eventServiceManager.getActionEventService().subscribe(this);
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
}
