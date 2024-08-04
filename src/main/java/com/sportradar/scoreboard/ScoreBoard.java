package com.sportradar.scoreboard;


/**
 * Live Football World Cup Scoreboard library that shows ongoing matches and their scores.
 */
public interface ScoreBoard {
    /**
     * Starts a new match, assuming the initial score is 0 – 0, and adds it to the scoreboard.
     *
     * @param homeTeam the name of the home team
     * @param awayTeam the name of the away team
     * @throws IllegalStateException    if one of the teams is already in an ongoing match
     * @throws IllegalArgumentException if the homeTeam or awayTeam is null, empty or have the same value
     */
    void startNewMatch(String homeTeam, String awayTeam) throws IllegalStateException, IllegalArgumentException;

    /**
     * Updates the score of an ongoing match.
     *
     * @param homeTeam      the name of the home team
     * @param awayTeam      the name of the away team
     * @param homeTeamScore the new score of the home team
     * @param awayTeamScore the new score of the away team
     * @throws IllegalArgumentException if the homeTeamScore or awayTeamScore is negative
     * @throws IllegalStateException    if there is no ongoing match for the given teams
     */
    void updateScore(String homeTeam, String awayTeam, Integer homeTeamScore, Integer awayTeamScore) throws IllegalStateException, IllegalArgumentException;
}
