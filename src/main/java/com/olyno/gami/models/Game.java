package com.olyno.gami.models;

import java.util.ArrayList;
import java.util.HashMap;

import com.olyno.gami.Gami;
import com.olyno.gami.enums.GameMessageAs;
import com.olyno.gami.enums.GameMessageTarget;
import com.olyno.gami.enums.GameMessageType;
import com.olyno.gami.enums.GameState;
import com.olyno.gami.listeners.GameListener;

public class Game extends GameManager {

	private HashMap<String, Team> teams;
	private GameMessageAs timerMessageAs;

	private GameState state;
	private Integer timer;
	private Team winner;
	private Object world;

	public Game(String name) {
		super(name);
		this.state = GameState.WAITING;
		this.timer = 15;
		this.timerMessageAs = GameMessageAs.TITLE;
		this.teams = new HashMap<>();
		this.messages.put(GameMessageType.JOIN, new ArrayList<GameMessage>());
		this.messages.put(GameMessageType.LEAVE, new ArrayList<GameMessage>());
		this.messages.put(GameMessageType.TIMER, new ArrayList<GameMessage>());
		this.messages.put(GameMessageType.END, new ArrayList<GameMessage>());
		this.messages.get(GameMessageType.TIMER).add(new GameTimerMessage(20, GameMessageTarget.GLOBAL, "Game starts in ${time} seconds"));
		this.messages.get(GameMessageType.TIMER).add(new GameTimerMessage(15, GameMessageTarget.GLOBAL, "Game starts in ${time} seconds"));
		for (int time = 1; time < 11; time ++) {
			this.messages.get(GameMessageType.TIMER).add(new GameTimerMessage(time, GameMessageTarget.GLOBAL, "Game starts in ${time} seconds"));
		}
		this.messages.get(GameMessageType.JOIN).add(new GameMessage(GameMessageTarget.GLOBAL, "${player} joined the game!"));
		this.messages.get(GameMessageType.JOIN).add(new GameMessage(GameMessageTarget.PLAYER, "You joined the game ${game}"));
		this.messages.get(GameMessageType.LEAVE).add(new GameMessage(GameMessageTarget.GLOBAL, "${player} left the game!"));
		this.messages.get(GameMessageType.LEAVE).add(new GameMessage(GameMessageTarget.PLAYER, "You left the game ${game}"));
		this.messages.get(GameMessageType.END).add(new GameMessage(GameMessageTarget.GLOBAL, "The ${game} game is finished! The winner is the ${winner} team!"));
		if (!Gami.getGames().containsKey(name)) {
			Gami.getGames().put(name, this);
			for (GameListener listener : Gami.getGameListeners()) {
				listener.onGameCreated(this);
			}
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
	 * Returns a team from its name
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
	 * Returns which form the message should take,
	 * Default: title
	 * 
	 * @return The timer message type
	 */
	public GameMessageAs getTimerMessageAs() {
		return timerMessageAs;
	}

	/**
	 * Set which form the message should take
	 * 
	 * @param as The form of the message
	 */
	public void setTimerMessageAs(GameMessageAs as) {
		this.timerMessageAs = as;
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

}