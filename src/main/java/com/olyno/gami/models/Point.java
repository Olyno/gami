package com.olyno.gami.models;

public class Point {

    private Integer points;
    private Integer advantage;
    private Object author;

    public Point( Integer points ) {
        this.points = points;
        this.advantage = points;
    }

    public Point( Integer points, Object author ) {
        this.points = points;
        this.advantage = points;
        this.author = author;
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
     * @param author The player who scored the point
     */
    public void setAuthor( Object author ) {
        this.author = author;
    }

}
