package biz.noorlander.batclient.plugins;

import biz.noorlander.batclient.BatmudPlugin;
import biz.noorlander.batclient.handlers.TickerHandler;
import biz.noorlander.batclient.utils.ParsedResultUtil;
import com.mythicscape.batclient.interfaces.BatClientPlugin;
import com.mythicscape.batclient.interfaces.BatClientPluginTrigger;
import com.mythicscape.batclient.interfaces.BatClientPluginUtil;
import com.mythicscape.batclient.interfaces.ParsedResult;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TickerPlugin extends BatmudPlugin<TickerHandler> {

    @Override
    public TickerHandler createHandler() {
        return new TickerHandler(this.getClientGUI());
    }
}
