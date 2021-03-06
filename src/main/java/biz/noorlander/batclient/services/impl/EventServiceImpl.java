package biz.noorlander.batclient.services.impl;

import biz.noorlander.batclient.services.EventListener;
import biz.noorlander.batclient.services.EventService;

import java.util.ArrayList;
import java.util.List;

public class EventServiceImpl<T> implements EventService<T> {

    private List<EventListener<T>> listeners;

    public EventServiceImpl() {
        listeners = new ArrayList<>();
    }

    public void subscribe(EventListener<T> eventListener) {
    	System.out.println("EventService: SUBSCRIBED -> " + eventListener.getClass().getSimpleName());
        listeners.add(eventListener);
    }

    public void raiseEvent(T event) {
        for (EventListener<T> listener : listeners) {
            listener.handleEvent(event);
        }
    }

}
