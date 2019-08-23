package biz.noorlander.batclient.services.managers;

import biz.noorlander.batclient.services.impl.EventServiceImpl;
import biz.noorlander.batclient.services.EventService;
import biz.noorlander.batclient.services.events.ActionEvent;
import biz.noorlander.batclient.services.events.CombatEvent;

public class EventServiceManager {
    private EventService<ActionEvent> actionEventService;
    private EventService<CombatEvent> combatEventService;

    private EventServiceManager() {
        super();
        actionEventService = new EventServiceImpl<>();
        combatEventService = new EventServiceImpl<>();
    }

    public EventService<ActionEvent> getActionEventService() {
        return actionEventService;
    }

    public EventService<CombatEvent> getCombatEventService() {
        return combatEventService;
    }

    private static class EventServiceManagerHelper {
    	private static final EventServiceManager INSTANCE = new EventServiceManager();
    }
    
    public static EventServiceManager getInstance() {
    	return EventServiceManagerHelper.INSTANCE;
    }
}
