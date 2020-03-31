package com.olyno.gami.models;

import com.olyno.gami.enums.GameMessageTarget;

public class GameMessage {

    private GameMessageTarget target;
    private String message;

    /**
     * Create a message for a specific target.
     * 
     * @param target The target of the message
     * @param message The message
     */
    public GameMessage(GameMessageTarget target, String message) {
        this.target = target;
        this.message = message;
    }

    /**
     * Set the target of the message
     * 
     * @param target The target of the message
     */
    public void setTarget(GameMessageTarget target) {
        this.target = target;
    }

    /**
     * Returns the target of the message
     * 
     * @return The target of the message
     */
    public GameMessageTarget getTarget() {
        return target;
    }

    /**
     * Set the message to send
     * 
     * @param message The message to send
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Returns the message to send
     * 
     * @return The message
     */
    public String getMessage() {
        return message;
    }

}