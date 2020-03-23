package com.olyno.gami.interfaces;

public enum GameState {

    WAITING(0),
    START(1),
    STARTED(2),
    ENDED(3);

    private Integer id;

    public Integer getValue() { 
        return this.id; 
    } 

    private GameState(Integer id) {
        this.id = id;
    }

}