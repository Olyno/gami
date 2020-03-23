package com.olyno.gami.models;

import java.util.HashMap;

import com.olyno.gami.Gami;
import com.olyno.gami.interfaces.GameMessageTarget;
import com.olyno.gami.interfaces.GameState;
import com.olyno.gami.listeners.GameListener;

public class Game extends GameManager {

	private HashMap<String, Team> teams;

	// Messages
	private HashMap<GameMessageTarget, String> endMessages;
	private HashMap<Integer, String> timerMessages;

	private GameState state;
	private Integer timer;
	private String timerMessageAs;
	private Team winner;
	private Object world;

	public Game(String name) {
		super(name);
		this.state = GameState.WAITING;
		this.timer = 15;
		this.endMessages.put(GameMessageTarget.GLOBAL, "The ${game} game is finished! The winner is the ${winner} team!");
		this.timerMessageAs = "title";
		this.timerMessages.put(20, "Game starts in ${time} seconds");
		this.timerMessages.put(15, "Game starts in ${time} seconds");
		for (int index = 1; index < 11; index ++) {
			this.timerMessages.put(index, "Game starts in ${time} seconds");
		}
		this.teams = new HashMap<>();
		this.endMessages = new HashMap<>();
		this.timerMessages = new HashMap<>();
		if (!Gami.getGames().containsKey(name)) {
			Gami.getGames().put(name, this);
		}
	}

	/**
	 * Starts the game
	 */
	public void start() {
		this.state = GameState.START;
		for (GameListener listener : Gami.getGameListeners()) {
			listener.onGameStarted(this);
		}
	}

	/**
	 * Stops the game
	 */
	public void stop() {
		this.state = GameState.ENDED;
		for (GameListener listener : Gami.getGameListeners()) {
			listener.onGameStopped(this);
		}
	}

	/**
	 * Delete the game
	 */
	public void delete() {
		Gami.getGames().remove(this.name);
		for (GameListener listener : Gami.getGameListeners()) {
			listener.onGameDeleted(this);
		}
	}

	/**
	 * Returns a hashmap of all existing teams of this game
	 * 
	 * @return List of teams
	 */
	public HashMap<String, Team> getTeams() {
		return teams;
	}

	/**
	 * Returns current state of the game
	 * 
	 * @return The state of the game
	 */
	public GameState getState() {
		return state;
	}

	/**
	 * Set the current state of the game
	 *
	 * @param state The new state of the game
	 */
	public void setState(GameState state) {
		this.state = state;
	}

	/**
	 * Return a team from its name
	 *
	 * @param team The team which will be return
	 * @return Team with the name in argument
	 */
	public Team getTeam(String team) {
		return teams.getOrDefault(team, null);
	}

	/**
	 * Add a team to your game
	 *
	 * @param team The team which will be added
	 */
	public void addTeam(Team team) {
		teams.put(team.getName(), team);
	}

	/**
	 * Checks if a team exist inside the game
	 * 
	 * @param team The team that you need to check existence
	 * @return If the team exists or not
	 */
	public Boolean teamExists(String team) {
		return teams.containsKey(team);
	}

	/**
	 * Returns the world of the game
	 * 
	 * @return The world
	 */
	public Object getWorld() {
		return world;
	}

	/**
	 * Set the world where the game is supposed to be in.
	 *
	 * @param world The world
	 */
	public void setWorld(Object world) {
		this.world = world;
	}

	/**
	 * Returns the timer before the game starts (in seconds)
	 * 
	 * @return The timer
	 */
	public Integer getTimer() {
		return timer;
	}

	/**
	 * Set the timer before the game starts (in seconds)
	 *
	 * @param timer The new timer
	 */
	public void setTimer(Integer timer) {
		this.timer = timer;
	}

	/**
	 * @return The timer message type (title or action bar)
	 */
	public String getTimerMessageAs() {
		return timerMessageAs;
	}

	/**
	 * Set the timer message as title or action bar
	 *
	 * @param timerMessageAs The new timer message type
	 */
	public void setTimerMessageAs(String timerMessageAs) {
		this.timerMessageAs = timerMessageAs;
	}

	/**
	 * Returns the team which won the game
	 * 
	 * @return The winner of the game
	 */
	public Team getWinner() {
		return winner;
	}

	/**
	 * Set the winner of the game
	 *
	 * @param winner The new winner of the game
	 */
	public void setWinner(Team winner) {
		this.winner = winner;
	}

	/**
	 * Returns a hashmap of all messages during the timer
	 *
	 * @return All timer messages
	 */
	public HashMap<Integer, String> getTimerMessages() {
		return timerMessages;
	}

	@Override
	public void addPlayer(Object player) {
		if (!players.contains(player)) {
			players.add(player);
			for (GameListener listener : Gami.getGameListeners()) {
				if (players.size() == minPlayer) {
					listener.onGameCanStart(this);
				}
				if (players.size() == maxPlayer) {
					listener.onGameReady(this);
				}
				listener.onPlayerJoin(this, player);
			}
		}
	}

	@Override
	public void removePlayer(Object player) {
		if (players.contains(player)) {
			players.remove(player);
			for (GameListener listener : Gami.getGameListeners()) {
				listener.onPlayerLeave(this, player);
			}
		}
	}

	@Override
	public void addSpectator(Object player) {
		if (!spectators.contains(player)) {
			spectators.add(player);
			for (GameListener listener : Gami.getGameListeners()) {
				listener.onSpectatorJoin(this, player);
			}
		}
	}

	@Override
	public void removeSpectator(Object player) {
		if (spectators.contains(player)) {
			spectators.remove(player);
			for (GameListener listener : Gami.getGameListeners()) {
				listener.onSpectatorLeave(this, player);
			}
		}
	}

	public HashMap<GameMessageTarget, String> getEndMessages() {
		return endMessages;
	}

}