package com.olyno.gami.listeners;

import com.olyno.gami.models.Point;
import com.olyno.gami.models.Team;

public interface TeamListener {

    /**
     * Triggered when a team is created
     * 
     * @param team The team created
     */
    public void onTeamCreated(Team team);

    /**
     * Triggered when a team is deleted
     * 
     * @param team The team deleted
     */
    public void onTeamDeleted(Team team);

    /**
     * Triggered when a team is ready
     * 
     * @param team The team ready
     */
    public void onTeamReady(Team team);

    /**
     * Triggered when a team wins a point
     * 
     * @param team The team which won the point
     * @param points The point won
     */
    public void onPointWin(Team team, Point points);

    /**
     * Triggered when a team loses a point
     * 
     * @param team The team which lose the point
     * @param points The point lost
     */
    public void onPointLose(Team team, Point points);

    /**
     * Triggered when a player joins a team
     * 
     * @param team The team that has been joined
     * @param player The player who joined
     */
    public void onPlayerJoin(Team team, Object player);

    /**
     * Triggered when a player leaves a team
     * 
     * @param team The team that has been left
     * @param player The player who left
     */
    public void onPlayerLeave(Team team, Object player);

    /**
     * Triggered when a spectator joins a team
     * 
     * @param team The team that has been joined
     * @param player The player who started to spectate
     */
    public void onSpectatorJoin(Team team, Object player);

    /**
     * Triggered when a spectator leaves a team
     * 
     * @param team The team that has been left
     * @param player The point who stopped to spectate
     */
    public void onSpectatorLeave(Team team, Object player);

    /**
     * Triggered when a team wins
     * 
     * @param team The team which won
     */
    public void onWin(Team team);
    
}