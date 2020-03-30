package com.olyno.gami.models;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

import com.olyno.gami.Gami;
import com.olyno.gami.enums.GameMessageTarget;
import com.olyno.gami.enums.GameMessageType;
import com.olyno.gami.listeners.TeamListener;

public class Team extends GameManager {

	private LinkedList<Point> points;
	private Game game;
	private Integer goal;

	// Messages
	private HashMap<GameMessageTarget, String> winPointMessages;
	private HashMap<GameMessageTarget, String> losePointMessages;

    /**
     * Create a new team
	 * 
     * @param name The name of the team
	 * @param game The game of the team
     */
	public Team(String name, Game game) {
		super(name);
        this.points = new LinkedList<Point>(Arrays.asList( new Point(0) ));
		this.game = game;
		this.goal = 5;
		this.messages.get(GameMessageType.JOIN).add(new GameMessage(GameMessageTarget.GLOBAL, "${player} joined the ${team} team!"));
		this.messages.get(GameMessageType.JOIN).add(new GameMessage(GameMessageTarget.PLAYER, "You joined the ${team} team!"));
		this.messages.get(GameMessageType.LEAVE).add(new GameMessage(GameMessageTarget.GLOBAL, "${player} left the ${team} team!"));
		this.messages.get(GameMessageType.LEAVE).add(new GameMessage(GameMessageTarget.PLAYER, "You left the ${team} team!"));
		this.messages.get(GameMessageType.WIN_POINT).add(new GameMessage(GameMessageTarget.GLOBAL, "${player} scored a point for the ${team} team!"));
		this.messages.get(GameMessageType.WIN_POINT).add(new GameMessage(GameMessageTarget.PLAYER, "You scored a point for your team!"));
		this.messages.get(GameMessageType.LOSE_POINT).add(new GameMessage(GameMessageTarget.GLOBAL, "${player} lost a point for the ${team} team"));
		this.messages.get(GameMessageType.LOSE_POINT).add(new GameMessage(GameMessageTarget.PLAYER, "The opponent team won a point!"));
		if (!game.getTeams().containsKey(name)) {
			game.addTeam(this);
			for (TeamListener listener : Gami.getTeamListeners()) {
				listener.onTeamCreated(this);
			}
		}
    }

	/**
	 * Returns the goal of the team,
	 * How many points the team must to have to win
	 * 
	 * @return The goal of the team
	 */
	public Integer getGoal() {
		return goal;
	}

	/**
	 * Set the goal of the team
	 *
	 * @param goal The new goal of the team
	 */
	public void setGoal(Integer goal) {
		this.goal = goal;
	}

    /**
     * @return The points of the team
     */
    public LinkedList<Point> getPoints() {
        return points;
    }

    /**
     * Set the points of the team
	 * 
     * @param points The new points of the team
     */
    public void setPoints( Integer points ) {
        this.points = new LinkedList<Point>(Arrays.asList( new Point(0) ));
    }

	/**
	 * Set the points of the team
	 *
	 * @param points The new points of the team
	 */
	public void setPoints(Point points) {
		this.points = new LinkedList<Point>(Arrays.asList( points ));
	}

    /**
     * Add points to the team
	 * 
     * @param points The points added
	 */
	public void addPoints( Point points ) {
		this.points.add(points);
		for (TeamListener listener : Gami.getTeamListeners()) {
			listener.onPointWin(this, points);
			Integer currentPoints = 0;
			for (Point point : this.points) {
				currentPoints += point.getPoints();
			}
			if (currentPoints == goal) {
				listener.onWin(this);
			}
		}
	}

    /**
     * Remove points from the team
	 * 
     * @param points The point removed
	 */
	public void removePoints( Point points ) {
		this.points.remove(points);
		for (TeamListener listener : Gami.getTeamListeners()) {
			listener.onPointLose(this, points);
		}
	}

    /**
     * Return the game which is the parent of the team
     *
     * @return The parent game of a team
     */
    public Game getGame() {
		return game;
	}

	/**
	 * Returns the won point messages
	 *
	 * @return A hashmap of the Win Point Message
	 */
	public HashMap<GameMessageTarget, String> getWinPointMessagess() {
		return winPointMessages;
	}

	/**
	 * Returns the lost point messages
	 *
	 * @return A hashmap of the Lose Point Message
	 */
	public HashMap<GameMessageTarget, String> getLosePointMessagess() {
		return losePointMessages;
	}

	@Override
	public void addPlayer(Object player) {
		if (!players.contains(player)) {
			players.add(player);
			for (TeamListener listener : Gami.getTeamListeners()) {
				listener.onPlayerJoin(this, player);
				if (minPlayer == players.size()) {
					listener.onTeamReady(this);
				}
			}
		}
	}

	@Override
	public void removePlayer(Object player) {
		if (players.contains(player)) {
			players.remove(player);
			for (TeamListener listener : Gami.getTeamListeners()) {
				listener.onPlayerLeave(this, player);
			}
		}

	}

	@Override
	public void addSpectator(Object player) {
		if (!spectators.contains(player)) {
			spectators.add(player);
			for (TeamListener listener : Gami.getTeamListeners()) {
				listener.onSpectatorJoin(this, player);
			}
		}
	}

	@Override
	public void removeSpectator(Object player) {
		if (spectators.contains(player)) {
			spectators.remove(player);
			for (TeamListener listener : Gami.getTeamListeners()) {
				listener.onSpectatorLeave(this, player);
			}
		}
	}

	@Override
	public void delete() {
		game.getTeams().remove(this.getName());
		for (TeamListener listener : Gami.getTeamListeners()) {
			listener.onTeamDeleted(this);
		}
	}

}
