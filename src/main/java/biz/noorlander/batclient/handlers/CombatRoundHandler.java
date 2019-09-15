package biz.noorlander.batclient.handlers;

import java.awt.Color;
import java.util.Optional;
import java.util.Timer;

import com.mythicscape.batclient.interfaces.ClientGUI;

import biz.noorlander.batclient.services.EventListener;
import biz.noorlander.batclient.services.events.CombatEvent;
import biz.noorlander.batclient.services.events.CombatEvent.Type;
import biz.noorlander.batclient.services.managers.EventServiceManager;
import biz.noorlander.batclient.timers.CombatTimerTask;
import biz.noorlander.batclient.utils.AttributedMessageBuilder;

public class CombatRoundHandler extends AbstractHandler implements EventListener<CombatEvent> {
	private int fight = 0;
    private int round = 0;
    private Timer timer;

	public CombatRoundHandler(ClientGUI gui) {
        super(gui, "COMBAT");
    }

    @Override
    public void initHandler() {
        EventServiceManager.getInstance().getCombatEventService().subscribe(this);
        this.timer = new Timer(false);
    }

    @Override
    public void handleEvent(CombatEvent event) {
        switch (event.getType()) {
            case ROUND:
            	if (event.getRound().isPresent() ) {
            		this.round = event.getRound().get();
            		if (this.round == 1) {
            			this.fight++;
                		resetCombatTimer(5999L);
            		} else {
            			resetCombatTimer(3500L);
            		}
            	}
            	break;
            case FINISHED:
            	reportToGui(AttributedMessageBuilder.create().append("*** Combat DONE", Optional.of(Color.CYAN), Optional.empty()).build());
            	break;
        }
    }

	private void resetCombatTimer(long wait) {
		timer.schedule(new CombatTimerTask(this, this.fight, this.round), wait);		
	}

	public int getFight() {
		return fight;
	}

	public int getRound() {
		return round;
	}

	public void combatFinished() {
		EventServiceManager.getInstance().getCombatEventService().raiseEvent(new CombatEvent(Type.FINISHED));
	}

	public void combatRound(int parseInt) {
		CombatEvent roundEvent = new CombatEvent(CombatEvent.Type.ROUND);
		roundEvent.setRound(parseInt);
		EventServiceManager.getInstance().getCombatEventService().raiseEvent(roundEvent);
	}

	@Override
	public void destroyHandler() {
		this.timer.cancel();
	}
	
	
}
