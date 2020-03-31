package com.olyno.gami.enums;

import java.util.function.Predicate;

import com.olyno.gami.models.GameMessage;

public enum GameMessageTarget {

    /**
     * All players in the game or team of player
     */
    GLOBAL("global", gameMessage -> gameMessage.getTarget().getName() == "global"),

    /**
     * Only the player
     */
    PLAYER("player", gameMessage -> gameMessage.getTarget().getName() == "player"),
    
    /**
     * All players in a game when the timer begins
     */
    TIMER("timer", gameMessage -> gameMessage.getTarget().getName() == "timer");

    private Object value;
    private Predicate<GameMessage> filter;

    public Object getName() { 
        return this.value; 
    }

    public Predicate<GameMessage> getFilter() { 
        return this.filter; 
    }

    private GameMessageTarget(Object value, Predicate<GameMessage> filter) {
        this.value = value;
        this.filter = filter;
    }

}