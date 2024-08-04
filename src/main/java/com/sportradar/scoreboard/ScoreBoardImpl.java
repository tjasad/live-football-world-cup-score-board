package com.sportradar.scoreboard;

import java.time.Instant;
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
    public synchronized void startNewMatch(String homeTown, String awayTown) throws IllegalStateException, IllegalArgumentException {

        if(homeTown == null || awayTown == null || homeTown.isEmpty() || awayTown.isEmpty()) {
            throw new IllegalArgumentException("Arguments can not be empty or null.");
        }
        if(homeTown.equals(awayTown)) {
            throw new IllegalArgumentException("Home town team and away town team should be different.");
        }

        if(matches.keySet().stream().anyMatch(k -> k.contains(homeTown) || k.contains(awayTown))) {
            throw new IllegalStateException("Can not start match, a team is in a ongoing match.");
        }

        Match match = new Match(homeTown, awayTown, Instant.now());
        matches.put("%s-%s".formatted(homeTown, awayTown), match);
    }

}
