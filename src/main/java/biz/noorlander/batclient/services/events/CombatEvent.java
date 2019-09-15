package biz.noorlander.batclient.services.events;

import java.util.Optional;

public class CombatEvent {
    public enum Type { ROUND, FINISHED }
    private Type type;
    private Optional<Integer> round;

    public CombatEvent(Type type) {
		super();
		this.type = type;
		this.round = Optional.empty();
	}
    
    public void setRound(Integer round) {
    	this.round = Optional.of(round);
    }
    
    public Optional<Integer> getRound() {
    	return round;
    }
    
    public Type getType() {
        return type;
    }
}
