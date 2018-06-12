package biz.noorlander.batclient.services;

public interface EventListener<T> {
    void handleEvent(T event);
}
