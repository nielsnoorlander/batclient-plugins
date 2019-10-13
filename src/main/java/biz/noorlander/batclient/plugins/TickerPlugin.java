package biz.noorlander.batclient.plugins;

import biz.noorlander.batclient.handlers.TickerHandler;
import biz.noorlander.batclient.utils.ParsedResultUtil;
import com.mythicscape.batclient.interfaces.BatClientPlugin;
import com.mythicscape.batclient.interfaces.BatClientPluginTrigger;
import com.mythicscape.batclient.interfaces.BatClientPluginUtil;
import com.mythicscape.batclient.interfaces.ParsedResult;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TickerPlugin extends BatClientPlugin implements BatClientPluginTrigger, BatClientPluginUtil {
    private Pattern shortScorePattern;
    private TickerHandler tickerHandler;

    public void loadPlugin() {
        shortScorePattern = Pattern.compile("^TICK: ([-+][0-9]+)?[/]([-+][0-9]+)?[/]([-+][0-9]+)?");
        tickerHandler = new TickerHandler(this.getClientGUI());
        System.out.println("--- Loading TriggerPlugin ---");
    }

    public String getName() {
        return "TickerPlugin";
    }

    public ParsedResult trigger(ParsedResult parsedResult) {
        Matcher matcher = shortScorePattern.matcher(parsedResult.getStrippedText().trim());
        if (matcher.find()) {
            if (matcher.group(2) != null && !matcher.group(2).isEmpty()) {
                try {
                    Integer spTicked = Integer.valueOf(matcher.group(2));
                    if (spTicked > 24) {
                        tickerHandler.resetTicker();
                    }
                } catch (NumberFormatException nfe) {
                    System.out.println("Ignored spTick of: " + matcher.group(2));
                }
            }
            return ParsedResultUtil.gag(parsedResult);
        }
        return null;
    }

    public void clientExit() {
        System.out.println("--- Exit TriggerPlugin ---\n");
        tickerHandler.destroyHandler();
    }
}
