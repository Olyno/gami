package com.olyno.gami.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import com.olyno.gami.Gami;
import com.olyno.gami.enums.FileFormat;
import com.olyno.gami.enums.GameMessageAs;
import com.olyno.gami.enums.GameMessageTarget;
import com.olyno.gami.enums.GameMessageType;
import com.olyno.gami.enums.GameState;
import com.olyno.gami.utils.PlayerTest;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class GameTest {

    private static Game gameTest;
    private static Team teamTest;
    private static PlayerTest playerTest;

    @BeforeEach
    void beforeEach() {
        Gami.getGames().clear();
        gameTest = new Game("Game Test");
        teamTest = new Team("Team Test");
        playerTest = new PlayerTest("Player Test");
    }

    @AfterAll
    static void afterAll() {
        try {
            Files.walk(Paths.get("build/tests"))
                .map(Path::toFile)
                .sorted((o1, o2) -> -o1.compareTo(o2))
                .forEach(File::delete);
        } catch (IOException e) {
            System.out.println("Can't delete the 'tests' directory");
        }
    }

    @Test
    @DisplayName("Games")
    void checkGame() {
        assertTrue(Gami.getGames().contains(gameTest), "Didn't add the game in the game list");
        assertEquals("Game Test", gameTest.getName(), "Can't define the right name");
        assertEquals("Game Test", gameTest.getDisplayName(), "Can't define the right display name");
        assertEquals(1, gameTest.getMinPlayer().intValue(), "Can't define the right minimum player amount");
        assertEquals(2, gameTest.getMaxPlayer().intValue(), "Can't define the right maximum player amount");
        assertEquals(0, gameTest.getMaxSpectator().intValue(), "Can't define the right maximum spectator");
        assertEquals(0, gameTest.getPlayers().size(), "Can't define the right players amount");
        assertEquals(0, gameTest.getSpectators().size(), "Can't define the right spectators amount");
        assertEquals(4, gameTest.getMessages().keySet().size(), "Can't define the right messages amount");
        assertEquals(GameState.WAITING, gameTest.getState(), "Can't define the right state");
        assertEquals(15, gameTest.getTimer().intValue(), "Can't define the right timer value");
        assertEquals(GameMessageAs.TITLE, gameTest.getTimerMessageAs(), "Can't define the right timer message as'");
        assertEquals(0, gameTest.getTeams().size(), "Can't define the right team amount");
        assertEquals(0, gameTest.getSessions().size(), "Can't define the right session amount'");
        assertEquals("${player} joined the game!",
            gameTest.getMessages(GameMessageType.JOIN, GameMessageTarget.GLOBAL).get(0).getMessage(),
            "Can't define the right first message");
        assertEquals(1, gameTest.<GameTimerMessage>getMessages(GameMessageType.TIMER, GameMessageTarget.GLOBAL)
            .stream()
            .filter(timer -> timer.getTime() == 15)
            .count(), "Can't define the right first timer message");
        gameTest.setDisplayName("Game Test Updated");
        assertEquals("Game Test Updated", Gami.getGameByName("Game Test").get().getDisplayName(), "Display name should be changed in the games list");
        gameTest.start();
        assertEquals(GameState.START, gameTest.getState(), "The game didn't start");
        gameTest.stop();
        assertEquals(GameState.ENDED, gameTest.getState(), "The game didn't stop");
        gameTest.save("build/tests", FileFormat.YAML);
        gameTest.save("build/tests", FileFormat.JSON);
        assertTrue(Files.exists(Paths.get("build/tests/Game Test.yaml")), "Can't save the game as yaml");
        assertTrue(Files.exists(Paths.get("build/tests/Game Test.json")), "Can't save the game as json");
        gameTest.delete();
        assertFalse(Gami.getGames().contains(gameTest), "Can't be deleted correctly");
    }

    @Test
    @DisplayName("Teams")
    void checkTeams() {
        gameTest.addTeam(teamTest);
        assertEquals(1, gameTest.getTeams().size(), "Can't add a team");
        assertSame(teamTest, gameTest.getTeamByName("Team Test").get(), "Can't get the team using name");
        gameTest.removeTeam(teamTest);
        assertEquals(0, gameTest.getTeams().size(), "Can't remove a team");
    }

    @Test
    @DisplayName("Spectators")
    void checkSpectators() {
        gameTest.addSpectator(playerTest);
        assertEquals(1, gameTest.getSpectators().size(), "Can't add the spectator");
        assertTrue(gameTest.getSpectators().contains(playerTest), "Spectators doesn't contain the player");
        gameTest.removeSpectator(playerTest);
        assertEquals(0, gameTest.getSpectators().size(), "Can't remove the spectator");
        assertFalse(gameTest.getSpectators().contains(playerTest), "Spectators still contain the player");
    }

    @Test
    @DisplayName("Players")
    void checkPlayers() {
        gameTest.addPlayer(playerTest);
        assertEquals(1, gameTest.getPlayers().size(), "Can't add the player");
        assertTrue(gameTest.getPlayers().contains(playerTest), "Players list doesn't contain the player");
        gameTest.removePlayer(playerTest);
        assertEquals(0, gameTest.getPlayers().size(), "Can't remove the player");
        assertFalse(gameTest.getPlayers().contains(playerTest), "Players list still contain the player");
    }

    @Test
    @DisplayName("Sessions")
    void checkSessions() {
        Optional<Game> game = gameTest.createSession();
        assertTrue(game.isPresent(), "Session has not been created correctly");
        assertTrue(game.get().getDisplayName().equals(gameTest.getDisplayName()), "Session is not the same of the game");
    }

}
