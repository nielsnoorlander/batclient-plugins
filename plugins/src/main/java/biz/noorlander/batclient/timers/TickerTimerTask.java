package biz.noorlander.batclient.timers;

import biz.noorlander.batclient.ui.BatGauge;

import java.util.TimerTask;

public class TickerTimerTask extends TimerTask {

    BatGauge progressBar;

    public TickerTimerTask(BatGauge progressBar) {
        super();
        this.progressBar = progressBar;
    }
    @Override
    public void run() {
        progressBar.setValue(progressBar.getValue() + 1);
    }


}
