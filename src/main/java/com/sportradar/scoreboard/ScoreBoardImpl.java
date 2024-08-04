package com.sportradar.scoreboard;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public final class ScoreBoardImpl implements ScoreBoard {
    private final ConcurrentHashMap<String, Match> matches;

    public ScoreBoardImpl() {
        this(new ConcurrentHashMap<>());
    }

    ScoreBoardImpl(ConcurrentHashMap<String, Match> inMemoryRepository) {
        this.matches = inMemoryRepository;
    }


    @Override
    public synchronized void startNewMatch(String homeTeam, String awayTeam) throws IllegalStateException, IllegalArgumentException {

        if (homeTeam == null || awayTeam == null || homeTeam.isEmpty() || awayTeam.isEmpty()) {
            throw new IllegalArgumentException("Arguments can not be empty or null.");
        }
        if (homeTeam.equals(awayTeam)) {
            throw new IllegalArgumentException("Home team and away team should be different.");
        }

        if (matches.keySet().stream().anyMatch(k -> k.contains(homeTeam) || k.contains(awayTeam))) {
            throw new IllegalStateException("Can not start match, a team is in a ongoing match.");
        }

        Match match = new Match(homeTeam, awayTeam, Instant.now());
        matches.put("%s-%s".formatted(homeTeam, awayTeam), match);
    }

    @Override
    public void updateScore(final String homeTeam, final String awayTeam, final Integer homeTeamScore, final Integer awayTeamScore) {

        if (homeTeamScore < 0 || awayTeamScore < 0) {
            throw new IllegalArgumentException("Scores can not be negative.");
        }

        Match updatedMatch = matches.computeIfPresent("%s-%s".formatted(homeTeam, awayTeam), (key, value) -> {
            value.setHomeScore(homeTeamScore);
            value.setAwayScore(awayTeamScore);
            return value;
        });

        if (updatedMatch == null) {
            throw new IllegalStateException("Match does not exist.");
        }
    }

    @Override
    public synchronized void finishMatch(String homeTeam, String awayTeam) {

        if (!matches.containsKey("%s-%s".formatted(homeTeam, awayTeam))) {
            throw new IllegalStateException("Match does not exist.");
        }

        matches.remove("%s-%s".formatted(homeTeam, awayTeam));
    }

    @Override
    public String getMatchesSummary() {

        List<Match> orderedMatches = matches.values().stream()
                .sorted(Comparator
                        .comparingInt((Match match) -> match.getHomeScore() + match.getAwayScore())
                        .thenComparing(Match::getStart).reversed())
                .toList();

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < orderedMatches.size(); i++) {
            Match match = orderedMatches.get(i);
            stringBuilder.append("%d. %s %d - %s %d".formatted(
                    i + 1,
                    match.getHomeTeam(),
                    match.getHomeScore(),
                    match.getAwayTeam(),
                    match.getAwayScore()));
            if (i != orderedMatches.size() - 1) {
                stringBuilder.append("\n");
            }
        }

        return stringBuilder.toString();
    }
}
