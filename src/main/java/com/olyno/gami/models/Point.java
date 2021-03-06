package com.olyno.gami.models;

public class Point {

    private Integer points;
    private Integer advantage;
    private Object author;

    public Point( Integer points ) {
        this.points = points;
        this.advantage = points;
    }

    public <T> Point( Integer points, T author ) {
        this.points = points;
        this.advantage = points;
        this.author = author;
    }

    @Override
	public boolean equals(Object o) {
		if (o == null) return false;
		if (o == this) return true;
		if (!(o instanceof Point)) return false;
		Point point = (Point) o;
		return point.getPoints() == this.getPoints();
	}

	@Override
	public int hashCode() {
		return points.hashCode();
	}

    /**
     * @return Points of the team
     */
    public Integer getPoints() {
        return points;
    }

    /**
     * @param points Points of the new "points" type
     */
    public void setPoints( Integer points ) {
        this.points = points;
    }

    /**
     * Return the advantage that the team will win with this number of point
     *
     * @return The advantage with these points
     */
    public Integer getAdvantage() {
        return advantage;
    }

    /**
     * Set the advantage that the team will win with this number of point
     *
     * @param advantage The advantage that the team will win with this number of point
     */
    public void setAdvantage( Integer advantage ) {
        this.advantage = advantage;
    }

    /**
     * Returns the author (player) who scored the point
     *
     * @return Who scored the point
     */
    public Object getAuthor() {
        return author;
    }

    /**
     * Set author of the point
     *
	 * @param <T> The player type
     * @param author The player who scored the point
     */
    public <T> void setAuthor( T author ) {
        this.author = author;
    }

}
