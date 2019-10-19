package biz.noorlander.batclient.handlers;

import biz.noorlander.batclient.timers.TickerTimerTask;
import biz.noorlander.batclient.ui.BatGauge;
import com.mythicscape.batclient.interfaces.ClientGUI;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class TickerHandler extends AbstractWindowedHandler {

    private BatGauge resourcesTicker;
    private Timer timer;

    public TickerHandler(ClientGUI gui) {
        super(gui, "Ticker");
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
}
