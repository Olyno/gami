package com.olyno.gami.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.olyno.gami.Gami;
import com.olyno.gami.enums.GameMessageTarget;
import com.olyno.gami.enums.GameMessageType;
import com.olyno.gami.utils.PlayerTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TeamTest {

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

    @Test
    @DisplayName("Games")
    void checkGame() {
        gameTest.addTeam(teamTest);
        assertTrue(gameTest.getTeams().contains(teamTest), "Didn't add the team in the team list");
        assertTrue(teamTest.getGame().isPresent(), "Didn't set the game of the team");
        assertEquals(gameTest, teamTest.getGame().get(), "Can't define the right game");
        assertEquals("Team Test", teamTest.getName(), "Can't define the right name");
        assertEquals("Team Test", teamTest.getDisplayName(), "Can't define the right display name");
        assertEquals(1, teamTest.getMinPlayer().intValue(), "Can't define the right minimum player amount");
        assertEquals(2, teamTest.getMaxPlayer().intValue(), "Can't define the right maximum player amount");
        assertEquals(0, teamTest.getMaxSpectator().intValue(), "Can't define the right maximum spectator");
        assertEquals(0, teamTest.getPlayers().size(), "Can't define the right players amount");
        assertEquals(0, teamTest.getSpectators().size(), "Can't define the right spectators amount");
        assertEquals(4, teamTest.getMessages().keySet().size(), "Can't define the right messages amount");
        assertEquals("${player} joined the ${team} team!",
            teamTest.getMessages(GameMessageType.JOIN, GameMessageTarget.GLOBAL).get(0).getMessage(),
            "Can't define the right first message");
        teamTest.setDisplayName("Team Test Updated");
        assertEquals("Team Test Updated", Gami.getGameByName("Game Test").get().getTeamByName("Team Test").get().getDisplayName(), "Display name should be changed in the teams list");
        teamTest.delete();
        assertFalse(gameTest.getTeams().contains(teamTest), "Can't be deleted correctly");
    }

    @Test
    @DisplayName("Players")
    void checkPlayers() {
        teamTest.addPlayer(playerTest);
        assertEquals(1, teamTest.getPlayers().size(), "Can't add the player");
        assertTrue(teamTest.getPlayers().contains(playerTest), "Players list doesn't contain the player");
        teamTest.removeSpectator(playerTest);
        assertEquals(0, teamTest.getSpectators().size(), "Can't remove the player");
        assertFalse(teamTest.getSpectators().contains(playerTest), "Players list still contain the player");
    }

    @Test
    @DisplayName("Spectators")
    void checkSpectators() {
        teamTest.addSpectator(playerTest);
        assertEquals(1, teamTest.getSpectators().size(), "Can't add the spectator");
        assertTrue(teamTest.getSpectators().contains(playerTest), "Spectators doesn't contain the player");
        teamTest.removeSpectator(playerTest);
        assertEquals(0, teamTest.getSpectators().size(), "Can't remove the spectator");
        assertFalse(teamTest.getSpectators().contains(playerTest), "Spectators still contain the player");
    }

}
