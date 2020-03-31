package com.olyno.gami.models;

import com.olyno.gami.enums.GameMessageTarget;

public class GameTimerMessage extends GameMessage {

    private Integer time;

    /**
     * Create a special game message with a time
     * 
     * @param time The time of the message (when it is showed)
     * @param messageTarget The target of the message
     * @param message The message
     */
    public GameTimerMessage(Integer time, GameMessageTarget messageTarget, String message) {
        super(messageTarget, message);
        this.time = time;
    }

    /**
     * Set the time when the message will be send.
     * 
     * @param time The new time of the message
     */
    public void setTime(Integer time) {
        this.time = time;
    }

    /**
     * Returns current time of the message
     * 
     * @return Time of the message
     */
    public Integer getTime() {
        return time;
    }

}