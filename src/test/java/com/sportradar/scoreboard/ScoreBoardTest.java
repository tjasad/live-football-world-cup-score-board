package com.sportradar.scoreboard;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class ScoreBoardTest {

    @Test
    @DisplayName("Should add a new Match in repository When startMatch is called")
    public void testStartMatch() {
        ConcurrentHashMap<String, Match> repo = new ConcurrentHashMap<>();
        ScoreBoard scoreBoard = new ScoreBoardImpl(repo);

        String homeTeam = "Argentina";
        String awayTeam = "Brazil";

        scoreBoard.startNewMatch(homeTeam, awayTeam);

        Assertions.assertTrue(repo.containsKey("Argentina-Brazil"));
    }

    @Test
    @DisplayName("Should return exception When arguments are empty")
    public void testStartMatch_emptyString() {
        ScoreBoard scoreBoard = new ScoreBoardImpl();

        IllegalArgumentException exception = Assertions.assertThrowsExactly(IllegalArgumentException.class, () ->
                scoreBoard.startNewMatch("", "Argentina"));
        Assertions.assertEquals("Arguments can not be empty or null.", exception.getMessage());

        exception = Assertions.assertThrowsExactly(IllegalArgumentException.class, () ->
                scoreBoard.startNewMatch("Argentina", ""));
        Assertions.assertEquals("Arguments can not be empty or null.", exception.getMessage());
    }

    @Test
    @DisplayName("Should return exception When arguments are null")
    public void testStartMatch_nullString() {
        ScoreBoard scoreBoard = new ScoreBoardImpl();

        IllegalArgumentException exception = Assertions.assertThrowsExactly(IllegalArgumentException.class, () ->
                scoreBoard.startNewMatch("Argentina", null));
        Assertions.assertEquals("Arguments can not be empty or null.", exception.getMessage());

        exception = Assertions.assertThrowsExactly(IllegalArgumentException.class, () ->
                scoreBoard.startNewMatch(null, "Argentina"));
        Assertions.assertEquals("Arguments can not be empty or null.", exception.getMessage());
    }

    @Test
    @DisplayName("Should return exception When a team is in a ongoing match")
    public void testStartMatch_ongoingMatch() {
        ScoreBoard scoreBoard = new ScoreBoardImpl();
        String homeTeam = "Argentina";
        String awayTeam = "Brazil";

        scoreBoard.startNewMatch(homeTeam, awayTeam);

        IllegalStateException exception = Assertions.assertThrowsExactly(IllegalStateException.class, () ->
                scoreBoard.startNewMatch("Germany", awayTeam));
        Assertions.assertEquals("Can not start match, a team is in a ongoing match.", exception.getMessage());

        exception = Assertions.assertThrowsExactly(IllegalStateException.class, () ->
                scoreBoard.startNewMatch(homeTeam, "Germany"));
        Assertions.assertEquals("Can not start match, a team is in a ongoing match.", exception.getMessage());
    }

    @Test
    @DisplayName("Should return exception When home team is equal to away team")
    public void testStartMatch_sameTeam() {
        ScoreBoard scoreBoard = new ScoreBoardImpl();
        String homeTeam = "Argentina";
        String awayTeam = "Argentina";

        IllegalArgumentException exception = Assertions.assertThrowsExactly(IllegalArgumentException.class, () ->
                scoreBoard.startNewMatch(homeTeam, awayTeam));
        Assertions.assertEquals("Home team and away team should be different.", exception.getMessage());
    }

    @Test
    @DisplayName("Should update Match score in repository When updateScore is called")
    public void testUpdateScore() {
        ConcurrentHashMap<String, Match> repo = new ConcurrentHashMap<>();
        ScoreBoard scoreBoard = new ScoreBoardImpl(repo);

        String homeTeam = "Argentina";
        String awayTeam = "Brazil";

        scoreBoard.startNewMatch(homeTeam, awayTeam);

        scoreBoard.updateScore(homeTeam, awayTeam, 1, 2);

        String matchKey = "Argentina-Brazil";
        Assertions.assertTrue(repo.containsKey(matchKey));
        Assertions.assertEquals(repo.get(matchKey).getHomeScore(), 1);
        Assertions.assertEquals(repo.get(matchKey).getAwayScore(), 2);
    }

    @Test
    @DisplayName("Should return exception When updating with negative score")
    public void testUpdateScore_negativeScore() {
        ScoreBoard scoreBoard = new ScoreBoardImpl();
        String homeTeam = "Argentina";
        String awayTeam = "Brazil";

        scoreBoard.startNewMatch(homeTeam, awayTeam);

        IllegalArgumentException exception = Assertions.assertThrowsExactly(IllegalArgumentException.class, () ->
                scoreBoard.updateScore(homeTeam, awayTeam, -1, 3));
        Assertions.assertEquals("Scores can not be negative.", exception.getMessage());

        exception = Assertions.assertThrowsExactly(IllegalArgumentException.class, () ->
                scoreBoard.updateScore(homeTeam, awayTeam, 0, -15));
        Assertions.assertEquals("Scores can not be negative.", exception.getMessage());
    }

    @Test
    @DisplayName("Should return exception When the teams are currently not in a match")
    public void testUpdateScore_noMatch() {
        ScoreBoard scoreBorad = new ScoreBoardImpl();
        String homeTeam = "Argentina";
        String awayTeam = "Brazil";

        IllegalStateException exception = Assertions.assertThrowsExactly(IllegalStateException.class, () ->
                scoreBorad.updateScore(homeTeam, awayTeam, 1, 0));

        Assertions.assertEquals("Match does not exist.", exception.getMessage());
    }

    @Test
    @DisplayName("Should remove Match from repository When finishMatch is called")
    public void testFinishMatch() {
        ConcurrentHashMap<String, Match> repo = new ConcurrentHashMap<>();
        ScoreBoard scoreBoard = new ScoreBoardImpl(repo);

        String homeTeam = "Argentina";
        String awayTeam = "Brazil";

        scoreBoard.startNewMatch(homeTeam, awayTeam);
        scoreBoard.finishMatch(homeTeam, awayTeam);

        Assertions.assertFalse(repo.containsKey("Argentina-Brazil"));
    }

    @Test
    @DisplayName("Should return exception When the teams are currently not in a match")
    public void testFinishMatch_noMatch() {
        ScoreBoard scoreBorad = new ScoreBoardImpl();
        String homeTeam = "Argentina";
        String awayTeam = "Brazil";

        IllegalStateException exception = Assertions.assertThrowsExactly(IllegalStateException.class, () ->
                scoreBorad.finishMatch(homeTeam, awayTeam));

        Assertions.assertEquals("Match does not exist.", exception.getMessage());
    }

    @Test
    @DisplayName("Should return matches summary When getMatchesSummary is called")
    public void testGetMatchesSummary() {
        ScoreBoard scoreBoard = new ScoreBoardImpl();
        scoreBoard.startNewMatch("Argentina", "Brazil");

        String summary = scoreBoard.getMatchesSummary();

        String expectedSummary = "1. Argentina 0 - Brazil 0";
        Assertions.assertEquals(expectedSummary, summary);
    }

    @Test
    @DisplayName("Should return empty string When scoreboard is empty")
    public void testGetMatchesSummary_emptyScoreBoard() {
        ScoreBoard scoreBoard = new ScoreBoardImpl();
        String summary = scoreBoard.getMatchesSummary();

        Assertions.assertEquals("", summary);
    }

    @Test
    @DisplayName("Should return match summary ordered by total score When total scores differ")
    public void testGetMatchesSummary_orderedByTotalScore() {
        ScoreBoard scoreBoard = new ScoreBoardImpl();

        scoreBoard.startNewMatch("Argentina", "Brazil");
        scoreBoard.updateScore("Argentina", "Brazil", 0, 1);

        scoreBoard.startNewMatch("Germany", "Spain");
        scoreBoard.updateScore("Germany", "Spain", 1, 1);

        scoreBoard.startNewMatch("Mexico", "Canada");
        scoreBoard.updateScore("Mexico", "Canada", 4, 0);

        String summary = scoreBoard.getMatchesSummary();

        String expectedSummary = """
                1. Mexico 4 - Canada 0
                2. Germany 1 - Spain 1
                3. Argentina 0 - Brazil 1""";

        Assertions.assertEquals(expectedSummary, summary);
    }

    @Test
    @DisplayName("Should return match summary ordered by most recently started When total scores are the same")
    public void testGetMatchesSummary_orderedByMostRecentlyStarted() throws InterruptedException {
        ScoreBoard scoreBoard = new ScoreBoardImpl();

        scoreBoard.startNewMatch("Germany", "Spain");
        scoreBoard.updateScore("Germany", "Spain", 2, 2);

        TimeUnit.SECONDS.sleep(1);

        scoreBoard.startNewMatch("Argentina", "Brazil");
        scoreBoard.updateScore("Argentina", "Brazil", 0, 4);

        TimeUnit.SECONDS.sleep(1);

        scoreBoard.startNewMatch("Mexico", "Canada");
        scoreBoard.updateScore("Mexico", "Canada", 4, 0);

        String summary = scoreBoard.getMatchesSummary();

        String expectedSummary = """
                1. Mexico 4 - Canada 0
                2. Argentina 0 - Brazil 4
                3. Germany 2 - Spain 2""";

        Assertions.assertEquals(expectedSummary, summary);
    }

    @Test
    @DisplayName("Should return match summary ordered first by total score then by most recently started")
    public void testGetMatchesSummary_correctOrder() throws InterruptedException {
        ScoreBoard scoreBoard = new ScoreBoardImpl();

        scoreBoard.startNewMatch("Mexico", "Canada");
        scoreBoard.updateScore("Mexico", "Canada", 4, 0);

        TimeUnit.SECONDS.sleep(1);

        scoreBoard.startNewMatch("Germany", "Spain");
        scoreBoard.updateScore("Germany", "Spain", 1, 1);

        TimeUnit.SECONDS.sleep(1);

        scoreBoard.startNewMatch("Argentina", "Brazil");
        scoreBoard.updateScore("Argentina", "Brazil", 1, 1);

        String summary = scoreBoard.getMatchesSummary();

        String expectedSummary = """
                1. Mexico 4 - Canada 0
                2. Argentina 1 - Brazil 1
                3. Germany 1 - Spain 1""";

        Assertions.assertEquals(expectedSummary, summary);
    }

}
