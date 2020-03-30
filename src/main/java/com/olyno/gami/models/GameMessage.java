package com.olyno.gami.models;

import com.olyno.gami.enums.GameMessageTarget;

public class GameMessage {

    private GameMessageTarget target;
    private String message;

    /**
     * Create a game message
     * 
     * @param target Who is the target of the message
     * @param message What is the message
     */
    public GameMessage(GameMessageTarget target, String message) {
        this.message = message;
        this.target = target;
    }

    /**
     * Returns the target message
     * 
     * @return The target of the message
     */
    public GameMessageTarget getTarget() {
        return target;
    }

    /**
     * Returns the message
     * 
     * @return The message
     */
    public String getMessage() {
        return message;
    }

}