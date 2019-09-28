package biz.noorlander.batclient.plugins;

import biz.noorlander.batclient.ui.BatGauge;
import biz.noorlander.batclient.utils.ConfigService;
import biz.noorlander.batclient.model.WindowsConfig;
import biz.noorlander.batclient.timers.TickerTimerTask;
import biz.noorlander.batclient.utils.ParsedResultUtil;
import com.mythicscape.batclient.interfaces.*;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TickerPlugin extends BatClientPlugin implements BatClientPluginTrigger, BatClientPluginUtil {
    private Color green = new Color(0, 153, 0);
    private Color red = new Color(153, 32, 24);
    private Color orange = new Color(163, 89, 27);

    private Pattern shortScorePattern;
    private BatWindow clientWin;
    private BatGauge resourcesTicker;
    private Timer timer;

    public void loadPlugin() {
        shortScorePattern = Pattern.compile("^TICK: ([-+][0-9]+)?[/]([-+][0-9]+)?[/]([-+][0-9]+)?");
        System.out.println("--- Loading TriggerPlugin ---");
        if (clientWin != null) {
            clientWin.close();
        }
        WindowsConfig tickerConfig = ConfigService.getInstance().getWindowsConfig(this);
        clientWin = this.getClientGUI().createBatWindow( "Ticker", tickerConfig.getLeft(), tickerConfig.getTop(), 240, 50 );
        clientWin.removeTabAt( 0 );

        resourcesTicker = new BatGauge(new Dimension(240, 50), 30, "s", green);
        resourcesTicker.addBoundary(24, orange);
        resourcesTicker.addBoundary(27, red);
        clientWin.newTab( "Ticker", resourcesTicker );
        clientWin.setVisible( true );
        TimerTask tickTimer = new TickerTimerTask(resourcesTicker);
        timer = new Timer(true);
        timer.scheduleAtFixedRate(tickTimer, 0, 1000);
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
                        resourcesTicker.setValue(0);
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
        System.out.println("Ticker at: x = " + clientWin.getLocation().x + ", y = " + clientWin.getLocation().y);
        ConfigService.getInstance().saveWindowsConfig(this, new WindowsConfig(this, clientWin));
        timer.cancel();
    }
}
