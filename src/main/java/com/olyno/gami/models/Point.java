package com.olyno.gami.models;

public class Point {

    private Integer points;
    private Integer advantage;
    private Object target;

    public Point( Integer points ) {
        this.points = points;
        this.advantage = points;
    }

    public Point( Integer points, Object target ) {
        this.points = points;
        this.advantage = points;
        this.target = target;
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

    public Object getTarget() {
        return target;
    }

    public void setTarget( Object target ) {
        this.target = target;
    }

}
