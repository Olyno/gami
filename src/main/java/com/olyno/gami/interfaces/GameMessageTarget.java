package com.olyno.gami.interfaces;

public enum GameMessageTarget {

    GLOBAL("global"),
    PLAYER("player");

    private String target;

    public String getTarget() { 
        return this.target; 
    } 

    private GameMessageTarget(String target) {
        this.target = target;
    }

}