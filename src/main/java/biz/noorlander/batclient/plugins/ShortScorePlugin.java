package biz.noorlander.batclient.plugins;

import biz.noorlander.batclient.ui.BatGauge;
import biz.noorlander.batclient.utils.ConfigService;
import biz.noorlander.batclient.model.WindowsConfig;
import biz.noorlander.batclient.timers.TickerTimerTask;
import com.mythicscape.batclient.interfaces.*;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShortScorePlugin extends BatClientPlugin implements BatClientPluginTrigger, BatClientPluginUtil {
    private Color green = new Color(0, 153, 0);
    private Color red = new Color(153, 32, 24);
    private Color orange = new Color(163, 89, 27);

    private Pattern shortScorePattern;
    private BatWindow clientWin;
    private BatGauge resourcesTicker;
    private Timer timer;

    public void loadPlugin() {
        shortScorePattern = Pattern.compile("^EQ:([a-z0-9]+)[ ]+H:([-]?[0-9]+)[/]([-]?[0-9]+)[ ]+([-|+]?[0-9]*)[ ]+S:([-]?[0-9]+)[/]([-]?[0-9]+)[ ]+([-|+]?[0-9]*)[ ]+E:([-]?[0-9]+)[/]([-]?[0-9]+)[ ]+[$][:]([0-9.]+)[ ]+([-]?[0-9]*)[ ]+exp:([0-9]+)[ ]+([-]?[0-9]*)[ ]+c:([A-z]+) [(]([0-9]+),([0-9]+)[)]$");
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
        if (matcher.matches() && matcher.groupCount() >= 7 && !matcher.group(7).isEmpty()) {
            /**
             * Group(0) = EQ:spr H:700/700 +10 S:1255/1255 +82 E:407/407 +5 $:12388  exp:79015 +2345 c:Rothikgen (175,269)
             * Group(1) = spr
             * Group(2) = 700
             * Group(3) = 700
             * Group(4) = +10
             * Group(5) = 1255
             * Group(6) = 1255
             * Group(7) = +82
             * Group(8) = 407
             * Group(9) = 407
             * Group(10) = 12388
             * Group(11) = +5
             * Group(12) = 79015
             * Group(13) = +2345
             * Group(14) = Rothikgen
             * Group(15) = 175
             */
            try {
                Integer spTicked = Integer.valueOf(matcher.group(7));
                if (spTicked > 24) {
                    resourcesTicker.setValue(0);
                }
            } catch (NumberFormatException nfe) {
                System.out.println("Ignored spTick of: " + matcher.group(7));
            }
        }
        return null;
    }

    private void debugMatcher(Matcher matcher) {
        System.out.println("Group count = " + matcher.groupCount());
        for (int i = 0; i < matcher.groupCount(); i++) {
            System.out.println("Group("+i+") = " + matcher.group(i));
        }
    }

    public void clientExit() {
        System.out.println("--- Exit TriggerPlugin ---\n");
        System.out.println("Ticker at: x = " + clientWin.getLocation().x + ", y = " + clientWin.getLocation().y);
        ConfigService.getInstance().saveWindowsConfig(this, new WindowsConfig(this, clientWin));
        timer.cancel();
    }
}
