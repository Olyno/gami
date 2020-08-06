package com.olyno.gami;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.olyno.gami.enums.FileFormat;
import com.olyno.gami.listeners.GameListener;
import com.olyno.gami.listeners.TeamListener;
import com.olyno.gami.models.Game;
import com.olyno.gami.models.Team;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

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
	public static <T> Game getGameOfPlayer(T player) {
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
	public static <T> Team getTeamOfPlayer(T player) {
		for (Game game : getGames().values()) {
			for (Team team : game.getTeams().values()) {
				if (team.hasPlayer(player)) {
					return team;
				}
			}
		}
		return null;
	}

	/**
	 * Load a game from a file.
	 * 
	 * @param gameFile The path to the game file
	 * @return The loaded game
	 */
	public static Game loadGame(Path gameFile) {
		if (Files.isRegularFile(gameFile)) {
			try {
				Game game;
				String extension = gameFile.getFileName().toString().split(".")[1].toUpperCase();
				FileFormat format = FileFormat.valueOf(extension);
				String content = Files.readString(gameFile);
				switch (format) {
					case YAML:
						Yaml yaml = new Yaml(new Constructor(Game.class));
						game = yaml.load(content);
					case JSON:
						Gson gson = new Gson();
						game = gson.fromJson(content, Game.class);
						break;
					default:
						throw new Error("This format doesn't exist: " + format);
				}
				Gami.getGames().put(game.getName(), game);
				for (GameListener listener : getGameListeners()) {
					listener.onGameLoaded(game, gameFile, format);
				}
				return game;
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * Load multiple games from multiple files.
	 * 
	 * @param gameFiles The list of game paths
	 * @return The list of loaded games
	 */
	public static LinkedList<Game> loadGames(List<Path> gameFiles) {
		LinkedList<Game> gamesList = new LinkedList<>();
		for (Path gameFile : gameFiles) {
			gamesList.add(loadGame(gameFile));
		}
		List<Game> notNullList = gamesList
			.stream()
			.filter(game -> game != null)
			.collect(Collectors.toList());
		if (notNullList.size() == 0) {
			return new LinkedList<>();
		}
		return gamesList;
	}

}