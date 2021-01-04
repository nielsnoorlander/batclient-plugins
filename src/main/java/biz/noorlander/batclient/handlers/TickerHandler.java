package biz.noorlander.batclient.handlers;

import biz.noorlander.batclient.timers.TickerTimerTask;
import biz.noorlander.batclient.ui.BatGauge;
import biz.noorlander.batclient.utils.ParsedResultUtil;
import com.mythicscape.batclient.interfaces.ClientGUI;
import com.mythicscape.batclient.interfaces.ParsedResult;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TickerHandler extends AbstractWindowedHandler {
    private Pattern shortScorePattern;
    private BatGauge resourcesTicker;
    private Timer timer;

    public TickerHandler(ClientGUI gui) {
        super(gui, "Ticker");
        shortScorePattern = Pattern.compile("^TICK: ([-+][0-9]+)?[/]([-+][0-9]+)?[/]([-+][0-9]+)?");
        resourcesTicker = new BatGauge(new Dimension(240, 50), 30, "s", BatGauge.GREEN);
        resourcesTicker.addBoundary(24, BatGauge.ORANGE);
        resourcesTicker.addBoundary(27, BatGauge.RED);
        createWindow("Ticker", "Ticker", resourcesTicker, "TickerPlugin");
        TimerTask tickTimer = new TickerTimerTask(resourcesTicker);
        timer = new Timer(true);
        timer.scheduleAtFixedRate(tickTimer, 0, 1000);
    }

    public void resetTicker() {
        resourcesTicker.setValue(0);
    }

    @Override
    public void destroyHandler() {
        timer.cancel();
        saveWindowsConfig();
    }

    @Override
    public ParsedResult handleOutputTriggers(ParsedResult parsedResult) {
        Matcher matcher = shortScorePattern.matcher(parsedResult.getStrippedText().trim());
        if (matcher.find()) {
            if (matcher.group(2) != null && !matcher.group(2).isEmpty()) {
                try {
                    int spTicked = Integer.parseInt(matcher.group(2));
                    if (spTicked > 24) {
                        resetTicker();
                    }
                } catch (NumberFormatException nfe) {
                    System.out.println("Ignored spTick of: " + matcher.group(2));
                }
            }
            return ParsedResultUtil.gag(parsedResult);
        }
        return null;
    }

    @Override
    public String handleCommandTriggers(String command) {
        return null;
    }
}
