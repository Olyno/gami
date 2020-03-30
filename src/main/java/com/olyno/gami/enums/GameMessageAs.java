package com.olyno.gami.enums;

public enum GameMessageAs {

    /**
     * Displays as title (default)
     */
    TITLE("title");

    private String type;

    private GameMessageAs(String type) {
        this.type = type;
    }

    public String getType() { 
        return this.type; 
    } 

}