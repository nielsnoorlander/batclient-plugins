package biz.noorlander.batclient.services.managers;

import biz.noorlander.batclient.services.impl.EventServiceImpl;
import biz.noorlander.batclient.services.EventService;
import biz.noorlander.batclient.services.events.ActionEvent;
import biz.noorlander.batclient.services.events.HeartbeatEvent;

public class EventServiceManager {
    private EventService<ActionEvent> actionEventService;
    private EventService<HeartbeatEvent> heartbeatEventService;

    public EventServiceManager() {
        super();
        actionEventService = new EventServiceImpl<>();
        heartbeatEventService = new EventServiceImpl<>();
    }

    public EventService<ActionEvent> getActionEventService() {
        return actionEventService;
    }

    public void setActionEventService(EventService<ActionEvent> actionEventService) {
        this.actionEventService = actionEventService;
    }

    public EventService<HeartbeatEvent> getHeartbeatEventService() {
        return heartbeatEventService;
    }

    public void setHeartbeatEventService(EventService<HeartbeatEvent> heartbeatEventService) {
        this.heartbeatEventService = heartbeatEventService;
    }

}
