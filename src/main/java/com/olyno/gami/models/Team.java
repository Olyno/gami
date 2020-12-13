package com.olyno.gami.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import com.olyno.gami.Gami;
import com.olyno.gami.enums.GameMessageTarget;
import com.olyno.gami.enums.GameMessageType;
import com.olyno.gami.listeners.TeamListener;

public class Team extends GameManager {

	private ArrayList<Point> points;
	private Integer goal;

	/**
	 * Create a new team
	 * 
	 * @param name The name of the team
	 */
	public Team(String name) {
		super(name);
		this.points = new ArrayList<Point>(Arrays.asList(new Point(0)));
		this.goal = 5;
		this.addMessage(GameMessageType.JOIN,
			new GameMessage(GameMessageTarget.GLOBAL, "${player} joined the ${team} team!"));
		this.addMessage(GameMessageType.JOIN,
			new GameMessage(GameMessageTarget.PLAYER, "You joined the ${team} team!"));
		this.addMessage(GameMessageType.LEAVE,
			new GameMessage(GameMessageTarget.GLOBAL, "${player} left the ${team} team!"));
		this.addMessage(GameMessageType.LEAVE,
			new GameMessage(GameMessageTarget.PLAYER, "You left the ${team} team!"));
		this.addMessage(GameMessageType.WIN_POINT,
			new GameMessage(GameMessageTarget.GLOBAL, "${player} scored a point for the ${team} team!"));
		this.addMessage(GameMessageType.WIN_POINT,
			new GameMessage(GameMessageTarget.PLAYER, "You scored a point for your team!"));
		this.addMessage(GameMessageType.LOSE_POINT,
			new GameMessage(GameMessageTarget.GLOBAL, "${player} lost a point for the ${team} team"));
		this.addMessage(GameMessageType.LOSE_POINT,
			new GameMessage(GameMessageTarget.PLAYER, "The opponent team won a point!"));
		for (TeamListener listener : Gami.getTeamListeners()) {
			listener.onTeamCreated(this);
		}
	}

	@Override
	public boolean equals(Object o) {
		if (o == null) return false;
		if (o == this) return true;
		if (!(o instanceof Team)) return false;
		Team team = (Team) o;
		return team.getName() == this.getName();
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	/**
	 * Returns the goal of the team, How many points the team must to have to win
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
	 * Returns the list of points of a team
	 * 
	 * @return The points of the team
	 */
	public ArrayList<Point> getPoints() {
		return points;
	}

	/**
	 * Returns the amount of points of a team
	 * 
	 * @return The points of the team
	 */
	public Integer getTotalPoints() {
		return points.stream().mapToInt(point -> point.getPoints()).sum();
	}

	/**
	 * Set the points of the team
	 * 
	 * @param points The new points of the team
	 */
	public void setPoints(Integer points) {
		this.points = new ArrayList<Point>(Arrays.asList(new Point(0)));
	}

	/**
	 * Set the points of the team
	 *
	 * @param points The new points of the team
	 */
	public void setPoints(Point points) {
		this.points = new ArrayList<Point>(Arrays.asList(points));
	}

	/**
	 * Add points to the team
	 * 
	 * @param points The points added
	 */
	public void addPoints(Point points) {
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
	public void removePoints(Point points) {
		this.points.remove(points);
		for (TeamListener listener : Gami.getTeamListeners()) {
			listener.onPointLose(this, points);
		}
	}

	/**
	 * Returns the game of the Team
	 *
	 * @return The game of the Team
	 */
	public Optional<Game> getGame() {
		for (Game game : Gami.getGames()) {
			if (game.getTeams().contains(this)) {
				return Optional.of(game);
			}
		}
		return Optional.empty();
	}

	@Override
	public <T> void addPlayer(T player) {
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
	public <T> void removePlayer(T player) {
		if (players.contains(player)) {
			players.remove(player);
			for (TeamListener listener : Gami.getTeamListeners()) {
				listener.onPlayerLeave(this, player);
			}
		}

	}

	@Override
	public <T> void addSpectator(T player) {
		if (!spectators.contains(player)) {
			spectators.add(player);
			for (TeamListener listener : Gami.getTeamListeners()) {
				listener.onSpectatorJoin(this, player);
			}
		}
	}

	@Override
	public <T> void removeSpectator(T player) {
		if (spectators.contains(player)) {
			spectators.remove(player);
			for (TeamListener listener : Gami.getTeamListeners()) {
				listener.onSpectatorLeave(this, player);
			}
		}
	}

	@Override
	public void delete() {
		getGame().ifPresent(game -> {
			game.removeTeam(this);
			for (TeamListener listener : Gami.getTeamListeners()) {
				listener.onTeamDeleted(this);
			}
		});
	}

}
