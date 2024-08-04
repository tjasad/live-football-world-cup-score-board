package com.sportradar.scoreboard;

import java.time.Instant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public final class Match {

    private final String homeTeam;
    private final String awayTeam;
    private Integer homeScore = 0;
    private Integer awayScore = 0;
    private final Instant start;
}
