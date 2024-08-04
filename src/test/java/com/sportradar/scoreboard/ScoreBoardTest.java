package com.sportradar.scoreboard;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ConcurrentHashMap;

public class ScoreBoardTest {

    @Test
    @DisplayName("Should add a new Match in repository When startMatch is called")
    public void testStartMatch() {
        ConcurrentHashMap<String, Match> repo = new ConcurrentHashMap<>();
        ScoreBoard scoreBoard = new ScoreBoardImpl(repo);

        String homeTownTeam = "Argentina";
        String awayTownTeam = "Brazil";

        scoreBoard.startNewMatch(homeTownTeam, awayTownTeam);

        Assertions.assertTrue(repo.containsKey("Argentina-Brazil"));
    }

    @Test
    @DisplayName("Should return exception When arguments are empty")
    public void testStartMatch_emptyString() {
        ScoreBoard scoreBoard = new ScoreBoardImpl();

        IllegalArgumentException exception = Assertions.assertThrowsExactly(IllegalArgumentException.class, () -> {
            scoreBoard.startNewMatch("", "Argentina");
        });
        Assertions.assertEquals("Arguments can not be empty or null.", exception.getMessage());

        exception = Assertions.assertThrowsExactly(IllegalArgumentException.class, () -> {
            scoreBoard.startNewMatch("Argentina", "");
        });
        Assertions.assertEquals("Arguments can not be empty or null.", exception.getMessage());
    }

    @Test
    @DisplayName("Should return exception When arguments are null")
    public void testStartMatch_nullString() {
        ScoreBoard scoreBoard = new ScoreBoardImpl();

        IllegalArgumentException exception = Assertions.assertThrowsExactly(IllegalArgumentException.class, () -> {
            scoreBoard.startNewMatch("Argentina", null);
        });
        Assertions.assertEquals("Arguments can not be empty or null.", exception.getMessage());

        exception = Assertions.assertThrowsExactly(IllegalArgumentException.class, () -> {
            scoreBoard.startNewMatch(null, "Argentina");
        });
        Assertions.assertEquals("Arguments can not be empty or null.", exception.getMessage());
    }

    @Test
    @DisplayName("Should return exception When a team is in a ongoing match")
    public void testStartMatch_ongoingMatch() {
        ScoreBoard scoreBoard = new ScoreBoardImpl();
        String homeTownTeam = "Argentina";
        String awayTownTeam = "Brazil";

        scoreBoard.startNewMatch(homeTownTeam, awayTownTeam);

        IllegalStateException exception = Assertions.assertThrowsExactly(IllegalStateException.class, () -> {
            scoreBoard.startNewMatch("Germany", awayTownTeam);
        });
        Assertions.assertEquals("Can not start match, a team is in a ongoing match.", exception.getMessage());

        exception = Assertions.assertThrowsExactly(IllegalStateException.class, () -> {
            scoreBoard.startNewMatch(homeTownTeam, "Germany");
        });
        Assertions.assertEquals("Can not start match, a team is in a ongoing match.", exception.getMessage());

    }

    @Test
    @DisplayName("Should return exception When home town team is equal to away town team")
    public void testStartMatch_sameTeam() {
        ScoreBoard scoreBoard = new ScoreBoardImpl();
        String homeTownTeam = "Slovenia";
        String awayTownTeam = "Slovenia";

        IllegalArgumentException exception = Assertions.assertThrowsExactly(IllegalArgumentException.class, () -> {
            scoreBoard.startNewMatch(homeTownTeam, awayTownTeam);
        });
        Assertions.assertEquals("Home town team and away town team should be different.", exception.getMessage());

    }

}
