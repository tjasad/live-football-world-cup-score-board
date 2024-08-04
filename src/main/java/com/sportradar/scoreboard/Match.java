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

    private final String homeTownTeam;
    private final String awayTownTeam;
    private Integer homeTownScore = 0;
    private Integer awayTownScore = 0;
    private final Instant start;
}
