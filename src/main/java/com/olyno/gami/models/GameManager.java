package com.olyno.gami.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.olyno.gami.enums.GameMessageTarget;
import com.olyno.gami.enums.GameMessageType;

public abstract class GameManager {

	protected HashMap<GameMessageType, ArrayList<GameMessage>> messages;

	protected String name;
	protected String displayName;
	protected Integer minPlayer;
	protected Integer maxPlayer;
	protected Object spawn;
	protected Object lobby;
	protected LinkedList<Object> players;
	protected LinkedList<Object> spectators;

	public GameManager(String name) {
		this.name = name;
		this.displayName = name;
		this.minPlayer = 1;
		this.maxPlayer = 2;
		this.players = new LinkedList<>();
		this.spectators = new LinkedList<>();
		this.messages = new HashMap<>();
	}

	/**
	 * Returns the name of the Game/Team
	 * 
	 * @return The name of the Game/Team
	 */
	public String getName() {
		return name;
	};

	/**
	 * Set the name of your Game/Team
	 * 
	 * @param name The name of your Game/Team
	 */
	public void setName(String name) {
		this.name = Pattern.compile("^\\S[a-z0-9]+").matcher(name).find() ? name : this.name;
	}

	/**
	 * Returns display name of Game/Team
	 * 
	 * @return The display name of your Game/Team
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * Set the display name of your Game/Team
	 * 
	 * @param displayName The display name of your Game/Team
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * Returns the minimum of player that your Game/Team require before start
	 * 
	 * @return The minimum of player in your Game/Team
	 */
	public Integer getMinPlayer() {
		return minPlayer;
	}

	/**
	 * Set the minimum of player that your Game/Team require before start
	 * 
	 * @param minPlayer The minimum of player in your Game/Team
	 */
	public void setMinPlayer(Integer minPlayer) {
		this.minPlayer = minPlayer;
	}

	/**
	 * Returns The maximum of player that your Game/Team require before start
	 * 
	 * @return The maximum of player in your Game/Team
	 */
	public Integer getMaxPlayer() {
		return maxPlayer;
	}

	/**
	 * Set the maximum of player that your Game/Team require before start
	 * 
	 * @param maxPlayer The maximum of player that your Game/Team require before start
	 */
	public void setMaxPlayer(Integer maxPlayer) {
		this.maxPlayer = maxPlayer;
	}

	/**
	 * Returns lobby of your Game/Team,
	 * Where players spawn for the first time for teams and where players are before join your game.
	 * 
	 * @return Lobby of your Game/Team
	 */
	public Object getLobby() {
		return this.lobby;
	}

	/**
	 * Set the location of the lobby of your Game/Team.
	 * Where players spawn for the first time for teams and where players are before join your game.
	 *
	 * @param lobby Lobby of your Game/Team
	 */
	public void setLobby(Object lobby) {
		this.lobby = lobby;
	}

	/**
	 * Returns the spawn of your Game/Team,
	 * Where players are when they respawn for teams and where players spawn when game started
	 * 
	 * @return The spawn of your Game/Team
	 */
	public Object getSpawn() {
		return spawn;
	}

	/**
	 * Set the location of the spawn of your Game/Team,
	 * Where players are when they respawn for teams and where players spawn when game started.
	 *
	 * @param spawn The spawn of your Game/Team
	 */
	public void setSpawn(Object spawn) {
		this.spawn = spawn;
	}

	/**
	 * Returns list of players in your Game or Team.
	 * 
	 * @return Players in your Game/Team
	 */
	public LinkedList<Object> getPlayers() {
		return players;
	}

	/**
	 * Add a player to your Game or Team
	 *
	 * @param player The player who will join the Game/Team
	 */
	public abstract void addPlayer(Object player);

	/**
	 * Remove a player from your Game or Team
	 *
	 * @param player The player who will leave the Game/Team
	 */
	public abstract void removePlayer(Object player);

	/**
	 * Checks if a player is in the Game or Team
	 * 
	 * @param player The player to check if he is in the Game/Team
	 * @return If the player is in your Game/Team or not
	 */
	public Boolean hasPlayer(Object player) {
		return players.contains(player);
	}

	/**
	 * Returns list of spectators in your Game or Team.
	 * 
	 * @return Spectators in your Game/Team
	 */
	public LinkedList<Object> getSpectators() {
		return players;
	}

	/**
	 * Add a spectator to your Game or Team
	 *
	 * @param player The player who will spectate the Game/Team
	 */
	public abstract void addSpectator(Object player);

	/**
	 * Remove a spectator from your Game or Team
	 *
	 * @param player The player who will stop to spectate the Game/Team
	 */
	public abstract void removeSpectator(Object player);

	/**
	 * Checks if a spectator is in the Game or Team
	 * 
	 * @param player The spectator to check if he is in the Game/Team
	 * @return If the spectator is in your Game/Team or not
	 */
	public Boolean hasSpectator(Object player) {
		return players.contains(player);
	}

	/**
	 * Delete your Game/Team
	 */
	public abstract void delete();

	/**
	 * All existing messages
	 *
	 * @return A hashmap of messages
	 */
	public HashMap<GameMessageType, ArrayList<GameMessage>> getMessages() {
		return messages;
	}

	/**
	 * All existing messages filtered by a GameMessageTarget
	 *
	 * @return An ArrayList of filtered messages
	 */
	public ArrayList<GameMessage> getMessages(GameMessageTarget target) {
		return new ArrayList<>(
			messages.get(target)
				.stream()
				.filter(target.getFilter())
				.collect(Collectors.toList())
		);
	}

}
