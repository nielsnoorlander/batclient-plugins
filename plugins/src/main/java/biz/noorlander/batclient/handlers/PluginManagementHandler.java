package biz.noorlander.batclient.handlers;

import biz.noorlander.batclient.PluginRepository;
import biz.noorlander.batclient.utils.Attribute;
import biz.noorlander.batclient.utils.AttributedMessageBuilder;
import com.google.common.collect.Lists;
import com.mythicscape.batclient.interfaces.ClientGUI;
import com.mythicscape.batclient.interfaces.ParsedResult;

import java.awt.*;

public class PluginManagementHandler extends AbstractHandler {

    public PluginManagementHandler(ClientGUI gui) {
        super(gui, "PLUGINMANAGEMENT");
    }

    @Override
    public void destroyHandler() {

    }

    @Override
    public ParsedResult handleOutputTriggers(ParsedResult parsedResult) {
        return null;
    }

    @Override
    public String handleCommandTriggers(String command) {
        if (command.startsWith("plugin")) {
            String[] params = command.substring(6).trim().split("[ ]+");
            if (params[0].length() == 0 || params[0].equals("help")) {
                echoPluginUsage();
            } else {
                if (params[0].equals("list")) {
                    echoPluginList();
                } else {
                    reportToGui(AttributedMessageBuilder.create()
                            .append("Unknown parameter for plugin command: ", Lists.newArrayList(Attribute.fgColor(Color.RED)))
                            .append(params[0]).append("\n")
                            .build());
                }
            }
            return "";
        }
        return null;
    }

    private void echoPluginList() {
        AttributedMessageBuilder messageBuilder = AttributedMessageBuilder.create()
                .append("Loaded plugins (* = enabled):\n", Lists.newArrayList(Attribute.fgColor(Color.GREEN)));
        PluginRepository.getInstance().list().forEach(plugin -> {
            messageBuilder.append(plugin.isEnabled() ? "* " : "  ").append(plugin.getName()).append("\n");
        });
        reportToGui(messageBuilder.append("\n").build());
    }

    private void echoPluginUsage() {
        reportToGui(AttributedMessageBuilder.create()
            .append("Plugin management usage:\n")
                .append("  enable <plugin name>", Lists.newArrayList(Attribute.fgColor(Color.WHITE))).append(" - Enable named plugin\n")
                .build());
    }

    private void echoPluginCommandUsage(AttributedMessageBuilder builder, String command, String description) {

    }
}
