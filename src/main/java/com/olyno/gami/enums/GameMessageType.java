package com.olyno.gami.enums;

public enum GameMessageType {

    /**
     * When a player joins a Game or a Team
     */
    JOIN,

    /**
     * When a player leaves a Game or a Team
     */
    LEAVE,

    /**
     * When a player wins a point
     */
    WIN_POINT,

    /**
     * When a player loses a point
     */
    LOSE_POINT,

    /**
     * Cooldown message
     */
    TIMER,

    /**
     * When a game ends
     */
    END

}