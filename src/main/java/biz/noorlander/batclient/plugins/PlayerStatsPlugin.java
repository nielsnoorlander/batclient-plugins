package biz.noorlander.batclient.plugins;

import biz.noorlander.batclient.handlers.PlayerStatsHandler;
import biz.noorlander.batclient.model.WindowsConfig;
import biz.noorlander.batclient.timers.TickerTimerTask;
import biz.noorlander.batclient.ui.BatGauge;
import biz.noorlander.batclient.utils.ConfigService;
import biz.noorlander.batclient.utils.ParsedResultUtil;
import com.mythicscape.batclient.interfaces.*;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlayerStatsPlugin extends BatClientPlugin implements BatClientPluginTrigger, BatClientPluginUtil {
    private Pattern shortScorePattern;
    private PlayerStatsHandler playerStatsHandler;

    public void loadPlugin() {
        shortScorePattern = Pattern.compile("^EQ:([a-z0-9]+) STATS: ([A-Z][a-z]+)/([0-9]+)/([0-9]+)/([0-9]+)/([0-9]+)/([0-9]+)/([0-9]+)/([0-9]+)/([0-9]+)/([a-z ]+)/([0-9]+)/([a-z ]+)/([0-9]+)/([ A-z',]*)/([_a-z]*)/");
        WindowsConfig playerStatsConfig = ConfigService.getInstance().getWindowsConfig(this);
        playerStatsHandler = new PlayerStatsHandler(this.getClientGUI(), playerStatsConfig);
    }

    public String getName() {
        return this.getClass().getSimpleName();
    }

    public ParsedResult trigger(ParsedResult parsedResult) {
        String text = parsedResult.getStrippedText().trim();
        Matcher matcher = shortScorePattern.matcher(text);
        if (matcher.find()) {
            if ("dam".equals(matcher.group(1))) {
                playerStatsHandler.updateEqSet("combat");
            } else if ("spr".equals(matcher.group(1)) || "hpr".equals(matcher.group(1))) {
                playerStatsHandler.updateEqSet("regen");
            } else if ("wis".equals(matcher.group(1)) || "dex".equals(matcher.group(1))) {
                playerStatsHandler.updateEqSet("buff");
            } else if ("0".equals(matcher.group(1))) {
                playerStatsHandler.updateEqSet("none");
            }
            return playerStatsHandler.updatePlayerStats(parsedResult);
        }
        return null;
    }

    public void clientExit() {
        System.out.println("--- Exit PlayerStatus ---\n");
        ConfigService.getInstance().saveWindowsConfig(this, playerStatsHandler.getUpdatedWindowsConfig());
    }
}
