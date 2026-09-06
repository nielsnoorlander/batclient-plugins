package biz.noorlander.batclient.services;

public interface EventService<T> {
    void raiseEvent(T event);
    void subscribe(EventListener<T> listener);
}
