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

    private String target;

    public String getTarget() { 
        return this.target; 
    } 

    private GameMessageTarget(String target) {
        this.target = target;
    }

}