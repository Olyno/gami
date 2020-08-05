package com.olyno.gami.listeners;

import java.nio.file.Path;

import com.olyno.gami.enums.FileFormat;
import com.olyno.gami.models.Game;
import com.olyno.gami.models.Team;

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
     * Triggered when a game is loaded from a file
     * 
     * @param game The game loaded
     */
    public void onGameLoaded(Game game, Path path, FileFormat format);

    /**
     * Triggered when a game is saved in a file
     * 
     * @param game The game saved
     */
    public void onGameSaved(Game game, Path path, FileFormat format);

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
     * Triggered when a team is added to a Game
     * 
     * @param game The game with the new Team
     * @param team The team which has been added
     */
    public void onTeamAdded(Game game, Team team);

    /**
     * Triggered when a team is removed from a Game
     * 
     * @param game The game with the old Team
     * @param team The team which has been removed
     */
    public void onTeamRemoved(Game game, Team team);

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