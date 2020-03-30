package com.olyno.gami.enums;

public enum GameMessageType {

    /**
     * When a player joins a Game or a Team
     */
    JOIN("join"),

    /**
     * When a player leaves a Game or a Team
     */
    LEAVE("leave"),

    /**
     * When a player wins a point
     */
    WIN_POINT("win_point"),

    /**
     * When a player loses a point
     */
    LOSE_POINT("lose_point"),

    /**
     * Cooldown message
     */
    TIMER("timer"),

    /**
     * When a game ends
     */
    END("end");

    private String type;

    private GameMessageType(String type) {
        this.type = type;
    }

    public String getType() { 
        return this.type; 
    } 

}