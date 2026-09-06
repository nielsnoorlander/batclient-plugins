package biz.noorlander.batclient.services.events;

public class ActionEvent {
    public enum Type { START, DONE, INTERRUPTED }
    private Type type;

    public ActionEvent(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }
}
