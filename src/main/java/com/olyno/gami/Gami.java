package com.olyno.gami;

import java.util.ArrayList;
import java.util.HashMap;

import com.olyno.gami.listeners.GameListener;
import com.olyno.gami.listeners.TeamListener;
import com.olyno.gami.models.Game;
import com.olyno.gami.models.Team;

public class Gami {

    private static HashMap<String, Game> games = new HashMap<>();
    private static ArrayList<GameListener> gameListeners = new ArrayList<>();
    private static ArrayList<TeamListener> teamListeners = new ArrayList<>();

    /**
	 * Register a listener to get Game events
	 * 
	 * @param listener Game listener
	 */
	public static void registerGameListener(GameListener listener) {
		gameListeners.add(listener);
    }
    
    /**
	 * Register a listener to get Team events
	 * 
	 * @param listener Team listener
	 */
	public static void registerTeamListener(TeamListener listener) {
		teamListeners.add(listener);
    }


    /**
     * Returns the list of Game listeners
     * 
     * @return Game listeners
     */
    public static ArrayList<GameListener> getGameListeners() {
        return gameListeners;
    }
    
    /**
     * Returns the list of Team listeners
     * 
     * @return Team listeners
     */
    public static ArrayList<TeamListener> getTeamListeners() {
        return teamListeners;
    }

    /**
     * Returns A hashmap of all existing games
     * 
	 * @return Existing games
	 */
	public static HashMap<String, Game> getGames() {
		return games;
	}

    /**
	 * Returns the game where the player is.
	 *
	 * @param player The player that we need to know the game where he is.
	 * @return The game where is the player
	 */
	public static Game getGameOfPlayer(Object player) {
		for (Game game : getGames().values()) {
			for (Team team : game.getTeams().values()) {
				if (team.hasPlayer(player)) {
					return game;
				}
			}
			if (game.hasPlayer(player)) {
				return game;
			}
		}
		return null;
	}

    /**
	 * Returns the team where the player is.
	 *
	 * @param player The player that we need to know the team where he is.
	 * @return The team where is the player
	 */
	public static Team getTeamOfPlayer(Object player) {
		for (Game game : getGames().values()) {
			for (Team team : game.getTeams().values()) {
				if (team.hasPlayer(player)) {
					return team;
				}
			}
		}
		return null;
	}

}