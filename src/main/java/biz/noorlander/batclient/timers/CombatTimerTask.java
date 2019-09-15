package biz.noorlander.batclient.timers;

import java.util.TimerTask;

import biz.noorlander.batclient.handlers.CombatRoundHandler;

public class CombatTimerTask extends TimerTask {
	CombatRoundHandler handler;
    int fight;
    int round;

    public CombatTimerTask(CombatRoundHandler handler, int fight, int round) {
        super();
        this.handler = handler;
        this.fight = fight;
        this.round = round;
    }
    @Override
    public void run() {
        if (handler.getFight() == this.fight && handler.getRound() == round) {
        	handler.combatFinished();
        }
    }


}
