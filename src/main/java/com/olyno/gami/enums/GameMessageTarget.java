package com.olyno.gami.enums;

public enum GameMessageTarget {

    /**
     * All players in the game or team of player
     */
    GLOBAL("global"),

    /**
     * Only the player
     */
    PLAYER("player");

    private Object value;

    public Object getName() { 
        return this.value; 
    }

    private GameMessageTarget(Object value) {
        this.value = value;
    }

}