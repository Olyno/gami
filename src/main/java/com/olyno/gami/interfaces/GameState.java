package com.olyno.gami.interfaces;

public enum GameState {

    /**
     * Waiting players, can't start the game
     */
    WAITING(0),

    /**
     * Game starting
     */
    START(1),

    /**
     * Game started, players are in game
     */
    STARTED(2),

    /**
     * Game stopped or finished
     */
    ENDED(3);

    private Integer id;

    public Integer getValue() { 
        return this.id; 
    } 

    private GameState(Integer id) {
        this.id = id;
    }

}