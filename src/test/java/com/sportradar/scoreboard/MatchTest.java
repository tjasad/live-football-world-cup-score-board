package com.sportradar.scoreboard;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;

class MatchTest {

    @Test
    @DisplayName("Should create new match with new score 0-0")
    void testMatch() {
        Match match = new Match("test","test", Instant.now());

        Assertions.assertEquals(0, match.getHomeTownScore());
        Assertions.assertEquals(0, match.getAwayTownScore());
    }
}