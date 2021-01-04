package biz.noorlander.batclient.handlers;

import biz.noorlander.batclient.services.EventListener;
import biz.noorlander.batclient.services.events.CombatEvent;
import biz.noorlander.batclient.services.events.CombatEvent.Type;
import biz.noorlander.batclient.services.managers.EventServiceManager;
import biz.noorlander.batclient.timers.CombatTimerTask;
import biz.noorlander.batclient.utils.Attribute;
import biz.noorlander.batclient.utils.AttributedMessageBuilder;
import com.google.common.collect.Lists;
import com.mythicscape.batclient.interfaces.ClientGUI;
import com.mythicscape.batclient.interfaces.ParsedResult;

import java.awt.*;
import java.util.Timer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CombatRoundHandler extends AbstractHandler implements EventListener<CombatEvent> {
	private int fight = 0;
    private int round = 0;
    private final Timer timer;
	private final Pattern combatRoundPattern;

	public CombatRoundHandler(ClientGUI gui) {
        super(gui, "COMBAT");
        EventServiceManager.getInstance().getCombatEventService().subscribe(this);
        this.timer = new Timer(false);
		this.combatRoundPattern = Pattern.compile("^[*]+ Round ([0-9]+) ([(][0-9]+[)] )?[*]+$");
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
            	reportToGui(AttributedMessageBuilder.create().append("*** Combat DONE", Lists.newArrayList(Attribute.fgColor(Color.CYAN))).build());
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

	@Override
	public ParsedResult handleOutputTriggers(ParsedResult parsedResult) {
		String text = parsedResult.getStrippedText().trim();
		if (text.startsWith("*")) {
			Matcher roundMatcher = combatRoundPattern.matcher(text);
			if (roundMatcher.find()) {
				combatRound(Integer.parseInt(roundMatcher.group(1)));
				return parsedResult;
			}
		}
		return null;
	}

	@Override
	public String handleCommandTriggers(String command) {
		return null;
	}


}
