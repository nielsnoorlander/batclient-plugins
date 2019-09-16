package biz.noorlander.batclient.handlers;

import com.mythicscape.batclient.interfaces.ClientGUI;
import com.mythicscape.batclient.interfaces.ParsedResult;

public abstract class AbstractHandler {
    private ClientGUI gui;
    private String pluginLabel;

    AbstractHandler(ClientGUI gui, String pluginLabel) {
        this.gui = gui;
        this.pluginLabel = pluginLabel;
        initHandler();
    }

    public abstract void initHandler();
    public abstract void destroyHandler();

    void reportToGui(ParsedResult message) {
        this.gui.printAttributedStringToWindow("Generic", message);
    }

    void command(String command) {
        this.gui.doCommand(command);
    }
}
