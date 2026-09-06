package biz.noorlander.batclient.plugins;

import biz.noorlander.batclient.BatmudPlugin;
import biz.noorlander.batclient.handlers.CommandQueueHandler;

public class CommandQueuePlugin extends BatmudPlugin<CommandQueueHandler> {

    @Override
    public CommandQueueHandler createHandler() {
        return new CommandQueueHandler(this.getClientGUI());
    }
}
