package com.olyno.gami.models;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Optional;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.olyno.gami.Gami;
import com.olyno.gami.enums.FileFormat;
import com.olyno.gami.enums.GameMessageAs;
import com.olyno.gami.enums.GameMessageTarget;
import com.olyno.gami.enums.GameMessageType;
import com.olyno.gami.enums.GameState;
import com.olyno.gami.listeners.GameListener;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

public class Game extends GameManager {

	private HashMap<String, Team> teams;
	private GameMessageAs timerMessageAs;
	private ArrayList<Game> sessions;

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
		this.sessions = new ArrayList<>();
		this.getMessages(GameMessageType.TIMER)
			.add(new GameTimerMessage(20, GameMessageTarget.GLOBAL, "Game starts in ${time} seconds"));
		this.getMessages(GameMessageType.TIMER)
			.add(new GameTimerMessage(15, GameMessageTarget.GLOBAL, "Game starts in ${time} seconds"));
		for (int time = 1; time < 11; time++) {
			this.getMessages(GameMessageType.TIMER)
				.add(new GameTimerMessage(time, GameMessageTarget.GLOBAL, "Game starts in ${time} seconds"));
		}
		this.getMessages(GameMessageType.JOIN)
			.add(new GameMessage(GameMessageTarget.GLOBAL, "${player} joined the game!"));
		this.getMessages(GameMessageType.JOIN)
			.add(new GameMessage(GameMessageTarget.PLAYER, "You joined the game ${game}"));
		this.getMessages(GameMessageType.LEAVE)
			.add(new GameMessage(GameMessageTarget.GLOBAL, "${player} left the game!"));
		this.getMessages(GameMessageType.LEAVE)
			.add(new GameMessage(GameMessageTarget.PLAYER, "You left the game ${game}"));
		this.getMessages(GameMessageType.END).add(new GameMessage(GameMessageTarget.GLOBAL,
			"The ${game} game is finished! The winner is the ${winner} team!"));
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
	 * Save a game as a file. It includes all current players, spectators, teams,
	 * messages and all related stuff.
	 * 
	 * @param directory The directory where the file should be saved
	 * @param format    The format of the file (yaml, json...)
	 */
	public void save(Path directory, FileFormat format) {
		if (Files.isDirectory(directory)) {
			try {
				Path gameFile = Paths.get(this.name + "." + format.name().toLowerCase());
				String data;
				switch (format) {
					case YAML:
						Yaml yaml = new Yaml(new Constructor(Game.class));
						data = yaml.dump(this);
						break;
					case JSON:
						Gson gson = new GsonBuilder().setPrettyPrinting().create();
						data = gson.toJson(this);
						break;
					default:
						throw new Error("This format doesn't exist: " + format);
				}
				Files.write(gameFile, Arrays.asList(data.split("\n")), StandardOpenOption.CREATE);
				for (GameListener listener : Gami.getGameListeners()) {
					listener.onGameSaved(this, directory, format);
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
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
	 * Add a team to the game
	 *
	 * @param team The team which will be added
	 */
	public void addTeam(Team team) {
		teams.put(team.getName(), team);
		for (GameListener listener : Gami.getGameListeners()) {
			listener.onTeamAdded(this, team);
		}
	}

	/**
	 * Remove a team from the game
	 *
	 * @param team The team which will be removed
	 */
	public void removeTeam(Team team) {
		teams.remove(team.getName());
		for (GameListener listener : Gami.getGameListeners()) {
			listener.onTeamRemoved(this, team);
		}
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
	public <T> void setWorld(T world) {
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
	 * Set the duration of the timer
	 * 
	 * @param time Duration of the timer
	 */
	public void setTimer(Integer time) {
		this.timer = time;
	}

	/**
	 * Returns which form the message should take, Default: title
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

	/**
	 * Returns all existing sessions for this game
	 * 
	 * @return All sessions of the game
	 */
	public ArrayList<Game> getSessions() {
		return sessions;
	}

	/**
	 * Returns a specific game session from its id. Can be empty if the id is wrong or out of the range.
	 * 
	 * @return The session with its id
	 */
	public Optional<Game> getSession(Integer id) {
		if (sessions.size() >= id + 1) {
			return Optional.of(sessions.get(id));
		}
		return Optional.empty();
	}

	/**
	 * Create a new session for this game It's simply a copy of the game to host the
	 * same game multiple times. Can be an empty optional if the session creation fail.
	 *
	 * @return The created session
	 */
	public Optional<Game> createSession() {
		try {
			Game clonedGame = (Game) this.clone();
			sessions.add(clonedGame);
			for (GameListener listener : Gami.getGameListeners()) {
				listener.onSessionCreated(clonedGame);
			}
			return Optional.of(clonedGame);
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return Optional.empty();
	}

	/**
	 * Delete an exisiting session for this game
	 *
	 * @return The deleted session
	 */
	public Game deleteSession(Integer id) {
		Game deletedGame = sessions.get(id);
		sessions.remove(id);
		for (GameListener listener : Gami.getGameListeners()) {
			listener.onSessionDeleted(deletedGame);
		}
		return deletedGame;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> void addPlayer(T player) {
		if (!players.contains(player)) {
			if (players.size() < maxPlayer) {
				((LinkedList<T>) players).add(player);
				for (GameListener listener : Gami.getGameListeners()) {
					if (players.size() == minPlayer) {
						listener.onGameCanStart(this);
					}
					if (players.size() == maxPlayer) {
						listener.onGameReady(this);
					}
					listener.onPlayerJoin(this, player);
				}
			} else if (spectators.size() < maxSpectator) {
				addSpectator(player);
			}
		}
	}

	@Override
	public <T> void removePlayer(T player) {
		if (players.contains(player)) {
			players.remove(player);
			for (GameListener listener : Gami.getGameListeners()) {
				listener.onPlayerLeave(this, player);
			}
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> void addSpectator(T player) {
		if (!spectators.contains(player)) {
			((LinkedList<T>) this.spectators).add(player);
			for (GameListener listener : Gami.getGameListeners()) {
				listener.onSpectatorJoin(this, player);
			}
		}
	}

	@Override
	public <T> void removeSpectator(T player) {
		if (spectators.contains(player)) {
			spectators.remove(player);
			for (GameListener listener : Gami.getGameListeners()) {
				listener.onSpectatorLeave(this, player);
			}
		}
	}

}