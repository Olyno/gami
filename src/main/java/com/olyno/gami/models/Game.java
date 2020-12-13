package com.olyno.gami.models;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.olyno.gami.Gami;
import com.olyno.gami.enums.FileFormat;
import com.olyno.gami.enums.GameMessageAs;
import com.olyno.gami.enums.GameMessageTarget;
import com.olyno.gami.enums.GameMessageType;
import com.olyno.gami.enums.GameState;
import com.olyno.gami.listeners.GameListener;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

public class Game extends GameManager implements Cloneable {

	private ArrayList<Team> teams;
	private GameMessageAs timerMessageAs;
	private ArrayList<Game> sessions;
	private boolean isSession;

	private int id;
	private GameState state;
	private Integer timer;
	private Team winner;
	private Object world;

	/**
	 * Create a new Game.
	 * Add the game in the list only if it already doesn't exist.
	 * 
	 * @param name The name of the game
	 */
	public Game(String name) {
		super(name);
		this.id = Gami.getGames().size();
		this.state = GameState.WAITING;
		this.timer = 15;
		this.timerMessageAs = GameMessageAs.TITLE;
		this.teams = new ArrayList<>();
		this.sessions = new ArrayList<>();
		this.isSession = false;
		this.addMessage(GameMessageType.TIMER,
			new GameTimerMessage(20, GameMessageTarget.GLOBAL, "Game starts in ${time} seconds"));
		this.addMessage(GameMessageType.TIMER,
			new GameTimerMessage(15, GameMessageTarget.GLOBAL, "Game starts in ${time} seconds"));
		for (int time = 1; time < 11; time++) {
			this.addMessage(GameMessageType.TIMER,
				new GameTimerMessage(time, GameMessageTarget.GLOBAL, "Game starts in ${time} seconds")
			);
		}
		this.addMessage(GameMessageType.JOIN,
			new GameMessage(GameMessageTarget.GLOBAL, "${player} joined the game!"));
		this.addMessage(GameMessageType.JOIN,
			new GameMessage(GameMessageTarget.PLAYER, "You joined the game ${game}"));
		this.addMessage(GameMessageType.LEAVE,
			new GameMessage(GameMessageTarget.GLOBAL, "${player} left the game!"));
		this.addMessage(GameMessageType.LEAVE,
			new GameMessage(GameMessageTarget.PLAYER, "You left the game ${game}"));
		this.addMessage(GameMessageType.END,
			new GameMessage(GameMessageTarget.GLOBAL, "The ${game} game is finished! The winner is the ${winner} team!"));
		if (!Gami.getGameByName(this.name).isPresent()) {
			Gami.getGames().add(this);
			for (GameListener listener : Gami.getGameListeners()) {
				listener.onGameCreated(this);
			}
		}
	}

	@Override
	public boolean equals(Object o) {
		if (o == null) return false;
		if (o == this) return true;
		if (!(o instanceof Game)) return false;
		Game game = (Game) o;
		return game.getName() == this.getName();
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	/**
	 * Update the list with all games.
	 * If regreshAll is true, will update id of all games.
	 * 
	 * @param refreshAll Should we refresh all ids?
	 */
	private Void update(Boolean refreshAll) {
		return CompletableFuture.runAsync(() -> {
			if (refreshAll) {
				for (int i = 0; i < Gami.getGames().size(); i++) {
					Gami.getGames().get(i).id = i;
				}
			} else {
				Gami.getGames().set(this.id, this);
			}
		}).join();
	}

	/**
	 * Starts the game
	 */
	public void start() {
		this.setState(GameState.START);
		for (GameListener listener : Gami.getGameListeners()) {
			listener.onGameStarted(this);
		}
	}

	/**
	 * Stops the game
	 */
	public void stop() {
		this.state = GameState.ENDED;
		this.update(true);
		for (GameListener listener : Gami.getGameListeners()) {
			listener.onGameStopped(this);
		}
	}

	/**
	 * Delete the game
	 */
	public void delete() {
		Gami.getGames().remove(this);
		this.update(true);
		for (GameListener listener : Gami.getGameListeners()) {
			listener.onGameDeleted(this);
		}
	}

	/**
	 * Alias of Game#save(Path, FileFormat) with other types.
	 * 
	 * @param directory The directory where the file should be saved
	 * @param format    The format of the file (yaml, json...)
	 */
	public void save(String directory, FileFormat format) {
		this.save(Paths.get(directory), format);
	}

	/**
	 * Save a game as a file. It includes all current players, spectators, teams,
	 * messages and all related stuff.
	 * 
	 * @param directory The directory where the file should be saved
	 * @param format    The format of the file (yaml, json...)
	 */
	public void save(Path directory, FileFormat format) {
		try {
			Path gameFile = Paths.get(directory.toString(), this.name + "." + format.name().toLowerCase());
			String data = "";
			switch (format) {
				case YAML:
					DumperOptions options = new DumperOptions();
					options.setIndent(2);
					options.setPrettyFlow(true);
					// Fix below - additional configuration
					options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
					Yaml yaml = new Yaml(options);
					data = yaml.dump(this);
					break;
				case JSON:
					Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
					data = gson.toJson(this);
					break;
				default:
					throw new Error("This format doesn't exist: " + format);
			}
			Files.createDirectories(directory);
			Files.write(gameFile, Arrays.asList(data.split("\n")), StandardOpenOption.CREATE);
			for (GameListener listener : Gami.getGameListeners()) {
				listener.onGameSaved(this, directory, format);
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Returns an ArrayList of all existing teams of this game
	 * 
	 * @return List of teams
	 */
	public ArrayList<Team> getTeams() {
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
		this.update(false);
	}

	/**
     * Returns a list of teams dependening of a filter.
     * 
	 * @param filter The filter to apply to find a teams list
	 * @return A list of teams dependening of a filter
	 */
	public ArrayList<Team> getTeamsByFilter(Predicate<? super Team> filter) {
		List<Team> teamsFound = teams.stream().filter(filter).collect(Collectors.toList()); 
		return new ArrayList<Team>(teamsFound);
	}

	/**
     * Returns the first team which match with the filter.
     * 
	 * @param filter The filter to apply to find a team
	 * @return The first team which match with the filter
	 */
	public Optional<Team> getTeamByFilter(Predicate<? super Team> filter) {
		return teams.stream().filter(filter).findFirst();
	}

	/**
	 * Returns a team from its name
	 *
	 * @param teamName The name of the team which will be return
	 * @return Team with the same name
	 */
	public Optional<Team> getTeamByName(String teamName) {
		return teams.stream().filter(team -> team.getName().equals(teamName)).findFirst();
	}

	/**
	 * Add a team to the game
	 * Add the team in the list only if it already doesn't exist.
	 *
	 * @param teamToAdd The team which will be added
	 */
	public void addTeam(Team teamToAdd) {
		if (!teams.contains(teamToAdd)) {
			teams.add(teamToAdd);
			this.update(false);
			for (GameListener listener : Gami.getGameListeners()) {
				listener.onTeamAdded(this, teamToAdd);
			}
		}
	}

	/**
	 * Remove a team from the game
	 *
	 * @param team The team which will be removed
	 */
	public void removeTeam(Team team) {
		teams.remove(team);
		this.update(false);
		for (GameListener listener : Gami.getGameListeners()) {
			listener.onTeamRemoved(this, team);
		}
	}

	/**
	 * Checks if a team exist inside the game
	 * 
	 * @param teamName The team that you need to check existence
	 * @return If the team exists or not
	 */
	public Boolean teamExists(String teamName) {
		return teams.stream().anyMatch(team -> team.getName().equals(teamName));
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
	 * @param <T> The world type
	 * @param world The world
	 */
	public <T> void setWorld(T world) {
		this.world = world;
		this.update(false);
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
		this.update(false);
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
		this.update(false);
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
		this.update(false);
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
	 * Returns a specific game session from its id. Can be empty if the id is wrong
	 * or out of the range.
	 * 
	 * @param id The session id
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
	 * same game multiple times. Can be an empty optional if the session creation
	 * fail.
	 *
	 * @return The created session
	 */
	public Optional<Game> createSession() {
		try {
			Game clonedGame = (Game) this.clone();
			clonedGame.isSession = true;
			sessions.add(clonedGame);
			this.update(false);
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
	 * @param id The id of the session to delete
	 * @return The deleted session
	 */
	public Game deleteSession(Integer id) {
		Game deletedSession = sessions.get(id);
		sessions.remove(id);
		this.update(false);
		for (GameListener listener : Gami.getGameListeners()) {
			listener.onSessionDeleted(deletedSession);
		}
		return deletedSession;
	}

	/**
	 * Returns a boolean depending if the game is a session or not.
	 *
	 * @return A boolean depending if the game is a session or not.
	 */
	public Boolean isSession() {
		return this.isSession;
	}

	@Override
	public <T> void addPlayer(T player) {
		if (!players.contains(player)) {
			if (players.size() < maxPlayer) {
				players.add(player);
				this.update(false);
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
			this.update(false);
			for (GameListener listener : Gami.getGameListeners()) {
				listener.onPlayerLeave(this, player);
			}
		}
	}

	@Override
	public <T> void addSpectator(T player) {
		if (!spectators.contains(player)) {
			spectators.add(player);
			this.update(false);
			for (GameListener listener : Gami.getGameListeners()) {
				listener.onSpectatorJoin(this, player);
			}
		}
	}

	@Override
	public <T> void removeSpectator(T player) {
		if (spectators.contains(player)) {
			spectators.remove(player);
			this.update(false);
			for (GameListener listener : Gami.getGameListeners()) {
				listener.onSpectatorLeave(this, player);
			}
		}
	}

}