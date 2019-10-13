package biz.noorlander.batclient.handlers;

import biz.noorlander.batclient.timers.TickerTimerTask;
import biz.noorlander.batclient.ui.BatGauge;
import biz.noorlander.batclient.utils.ConfigService;
import com.mythicscape.batclient.interfaces.BatWindow;
import com.mythicscape.batclient.interfaces.ClientGUI;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class TickerHandler extends AbstractWindowedHandler {
    private final static Color GREEN = new Color(0, 153, 0);
    private final static Color RED = new Color(153, 32, 24);
    private final static Color ORANGE = new Color(163, 89, 27);

    private BatGauge resourcesTicker;
    private Timer timer;

    public TickerHandler(ClientGUI gui) {
        super(gui, "Ticker");
        resourcesTicker = new BatGauge(new Dimension(240, 50), 30, "s", GREEN);
        resourcesTicker.addBoundary(24, ORANGE);
        resourcesTicker.addBoundary(27, RED);
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
}
