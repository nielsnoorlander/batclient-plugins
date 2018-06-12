package biz.noorlander.batclient.services;

import java.util.ArrayList;
import java.util.List;

public class EventServiceImpl<T> implements EventService<T> {

    private List<EventListener<T>> listeners;

    public EventServiceImpl() {
        listeners = new ArrayList<>();
    }

    public void subscribe(EventListener<T> eventListener) {
        listeners.add(eventListener);
    }

    public void raiseEvent(T event) {
        for (EventListener<T> listener : listeners) {
            listener.handleEvent(event);
        }
    }

}
