package com.olyno.gami;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.olyno.gami.enums.FileFormat;
import com.olyno.gami.listeners.GameListener;
import com.olyno.gami.listeners.TeamListener;
import com.olyno.gami.models.Game;
import com.olyno.gami.models.Team;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

/**
 * The "global" class, with a lot of utils and global methods.
 */
public class Gami {

    private static ArrayList<Game> games = new ArrayList<>();
    private static ArrayList<GameListener> gameListeners = new ArrayList<>();
    private static ArrayList<TeamListener> teamListeners = new ArrayList<>();

    /**
	 * Register a listener to get Game events.
	 * 
	 * @param listener Game listener
	 */
	public static void registerGameListener(GameListener listener) {
		gameListeners.add(listener);
    }
    
    /**
	 * Register a listener to get Team events.
	 * 
	 * @param listener Team listener
	 */
	public static void registerTeamListener(TeamListener listener) {
		teamListeners.add(listener);
    }


    /**
     * Returns the list of Game listeners.
     * 
     * @return Game listeners
     */
    public static ArrayList<GameListener> getGameListeners() {
        return gameListeners;
    }
    
    /**
     * Returns the list of Team listeners.
     * 
     * @return Team listeners
     */
    public static ArrayList<TeamListener> getTeamListeners() {
        return teamListeners;
    }

    /**
     * Returns an ArrayList of all existing games.
     * 
	 * @return Existing games
	 */
	public static ArrayList<Game> getGames() {
		return games;
	}

	/**
     * Returns a list of games dependening of a filter.
     * 
	 * @param filter The function which will filter values
	 * @return A list of games dependening of a filter
	 */
	public static ArrayList<Game> getGamesByFilter(Predicate<? super Game> filter) {
		List<Game> gamesFound = games.stream().filter(filter).collect(Collectors.toList()); 
		return new ArrayList<Game>(gamesFound);
	}

	/**
     * Returns the first game which match with the filter.
     * 
	 * @param filter The function which will filter values
	 * @return The first game which match with the filter
	 */
	public static Optional<Game> getGameByFilter(Predicate<? super Game> filter) {
		return games.stream().filter(filter).findFirst();
	}

	/**
     * Returns the first game found with the target name.
     * 
	 * @param gameName The name of the target game
	 * @return The first game found with the target name
	 */
	public static Optional<Game> getGameByName(String gameName) {
		return games.stream().filter(game -> game.getName().equals(gameName)).findFirst();
	}
	
    /**
	 * Returns the game where the player is.
	 *
	 * @param player The player that we need to know the game where he is.
	 * @return The game where is the player
	 */
	public static <T> Optional<Game> getGameOfPlayer(T player) {
		for (Game game : games) {
			for (Team team : game.getTeams()) {
				if (team.hasPlayer(player)) {
					return Optional.of(game);
				}
			}
			if (game.hasPlayer(player)) {
				return Optional.of(game);
			}
		}
		return Optional.empty();
	}

    /**
	 * Returns the team where the player is.
	 *
	 * @param player The player that we need to know the team where he is.
	 * @return The team where is the player
	 */
	public static <T> Optional<Team> getTeamOfPlayer(T player) {
		for (Game game : games) {
			for (Team team : game.getTeams()) {
				if (team.hasPlayer(player)) {
					return Optional.of(team);
				}
			}
		}
		return Optional.empty();
	}

	/**
	 * Load a game from a file.
	 * 
	 * @param gameFile The path to the game file
	 * @return The loaded game
	 */
	public static Optional<Game> loadGame(Path gameFile) {
		if (Files.isRegularFile(gameFile)) {
			try {
				Game game;
				String extension = gameFile.getFileName().toString().split(".")[1].toUpperCase();
				FileFormat format = FileFormat.valueOf(extension);
				String content = String.join("\n", Files.readAllLines(gameFile, StandardCharsets.UTF_8));
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
				games.add(game);
				for (GameListener listener : getGameListeners()) {
					listener.onGameLoaded(game, gameFile, format);
				}
				return Optional.of(game);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return Optional.empty();
	}

	/**
	 * Load multiple games from multiple files.
	 * 
	 * @param gameFiles The list of game paths
	 * @return The list of loaded games
	 */
	public static LinkedList<Game> loadGames(List<Path> gameFiles) {
		LinkedList<Optional<Game>> gamesList = new LinkedList<>();
		for (Path gameFile : gameFiles) {
			gamesList.add(loadGame(gameFile));
		}
		List<Game> notNullList = gamesList
			.stream()
			.filter(Optional::isPresent)
			.map(Optional::get)
			.collect(Collectors.toList());
		return new LinkedList<>(notNullList);
	}

}