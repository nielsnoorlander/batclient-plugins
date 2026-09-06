package biz.noorlander.batclient.plugins;

import biz.noorlander.batclient.BatmudPlugin;
import biz.noorlander.batclient.handlers.PluginManagementHandler;

public class PluginManagementPlugin extends BatmudPlugin<PluginManagementHandler> {
    @Override
    public PluginManagementHandler createHandler() {
        return new PluginManagementHandler(this.getClientGUI());
    }
}
