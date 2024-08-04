package com.sportradar.scoreboard;


/**
 * Live Football World Cup Scoreboard library that shows ongoing matches and their scores.
 */
public interface ScoreBoard {
    /**
     * Starts a new match, assuming the initial score is 0 – 0, and adds it to the scoreboard.
     *
     * @param homeTown the name of the home team
     * @param awayTown the name of the away team
     * @throws IllegalStateException    if one of the teams is already in an ongoing match
     * @throws IllegalArgumentException if the homeTown or awayTown is null, empty or have the same value
     */
    void startNewMatch(String homeTown, String awayTown) throws IllegalStateException, IllegalArgumentException;
}
