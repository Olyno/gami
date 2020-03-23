package com.olyno.gami.listeners;

import com.olyno.gami.models.Game;

public interface GameListener {

    /**
     * Triggered when a game is created
     * 
     * @param game The game created
     */
    public void onGameCreated(Game game);

    /**
     * Triggered when a game is deleted
     * 
     * @param game The game deleted
     */
    public void onGameDeleted(Game game);

    /**
     * Triggered when a game is is able to start
     * 
     * @param game The game able to start
     */
    public void onGameCanStart(Game game);

    /**
     * Triggered when a game is ready to start
     * 
     * @param game The game ready
     */
    public void onGameReady(Game game);

    /**
     * Triggered when a game is started
     * 
     * @param game The game started
     */
    public void onGameStarted(Game game);
    
    /**
     * Triggered when a game is stopped
     * 
     * @param game The game stopped
     */
    public void onGameStopped(Game game);

    /**
     * Triggered when a player joins a game
     * 
     * @param game The game joined
     * @param player The player who joined the game
     */
    public void onPlayerJoin(Game game, Object player);

    /**
     * Triggered when a player leaves a game
     * 
     * @param game The game left
     * @param player The player who left the game
     */
    public void onPlayerLeave(Game game, Object player);

    /**
     * Triggered when a player joins a game
     * 
     * @param game The game joined
     * @param player The player who started to spectate
     */
    public void onSpectatorJoin(Game game, Object player);

    /**
     * Triggered when a spectator leaves a game
     * 
     * @param game The game left
     * @param player The player who stopped to spectate
     */
    public void onSpectatorLeave(Game game, Object player);

}