package biz.noorlander.batclient.plugins;

import biz.noorlander.batclient.BatmudPlugin;
import biz.noorlander.batclient.handlers.PlayerStatsHandler;
import com.mythicscape.batclient.interfaces.BatClientPlugin;
import com.mythicscape.batclient.interfaces.BatClientPluginTrigger;
import com.mythicscape.batclient.interfaces.BatClientPluginUtil;
import com.mythicscape.batclient.interfaces.ParsedResult;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlayerStatsPlugin extends BatmudPlugin<PlayerStatsHandler> {

    @Override
    public PlayerStatsHandler createHandler() {
        return new PlayerStatsHandler(this.getClientGUI());
    }

}
