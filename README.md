# Live Football World Cup Scoreboard Library

## Overview

The Live Football World Cup Scoreboard library provides functionality for managing and displaying ongoing football
matches and their scores. This library includes an implementation of the `ScoreBoard` interface that supports operations
such as starting new matches, updating scores, finishing matches, and getting a summary of ongoing matches.

## Usage

### Prerequisites

- Java 21
- Maven 3.9.6+

### Building the project

```shell
mvn clean install
```

### Using the library

1. **Include as Maven dependency**  
   To use this library in a Maven project, add the following dependency to your `pom.xml`:

   ```xml
   <dependency>
       <groupId>com.sportradar</groupId>
       <artifactId>live-football-world-cup-scoreboard</artifactId>
       <version>1.0-SNAPSHOT</version>
   </dependency>
   ```

2. **Instantiate the ScoreBoard implementation**  
   Use the provided implementation of the `ScoreBoard` interface to manage matches and scores.

3. **Create and manage Matches**
    - **Start a new Match:**
      ```java
      scoreBoard.startNewMatch("HomeTeam", "AwayTeam");
      ```
    - **Update scores:**
      ```java
      scoreBoard.updateScore("HomeTeam", "AwayTeam", homeTeamScore, awayTeamScore);
      ```
    - **Finish a Match:**
      ```java
      scoreBoard.finishMatch("HomeTeam", "AwayTeam");
      ```

4. **Retrieve Match summary**
    - **Get summary of ongoing Matches:**
      ```java
      String summary = scoreBoard.getMatchesSummary();
      System.out.println(summary);
      ```

### Example

Example of how to use the library:

```java
public class Main {
    public static void main(String[] args) {
        // Instantiate the scoreboard implementation or make a producer
        ScoreBoard scoreBoard = new ScoreBoardImpl();

        // Start new matches
        scoreBoard.startNewMatch("Mexico", "Canada");
        scoreBoard.updateScore("Mexico", "Canada", 0, 5);
        scoreBoard.startNewMatch("Spain", "Brazil");
        scoreBoard.updateScore("Spain", "Brazil", 10, 2);

        // Print the match summary
        System.out.println(scoreBoard.getMatchesSummary());

        // Finish a match and print the updated summary
        scoreBoard.finishMatch("Mexico", "Canada");
        System.out.println(scoreBoard.getMatchesSummary());
    }
}
```

## Testing and Code Quality

- **Testing**

  Code written following Test-Driven Development (TDD) principles to ensure the implementation is thoroughly tested.
- **Static checks**

  Used SonarLint (local version), together with IntelliJ "problems" analysis. Formatting is also IntelliJ default, that
  gets applied with all other checks before commit.

### JaCoCo Code Coverage Report

To generate the JaCoCo code coverage report, follow these steps:

1. **Generate report**
   ```sh
   mvn clean test jacoco:report
   ```

2. **Locate report**  
   The report will be available in: `target/site/jacoco`

## Assumptions

- The library operates purely in-memory and does not persist data beyond the runtime of the application. The data
  structure used for this is `ConcurrentHashMap` to satisfy the requirement of simplest solution that could work.
- Added Lombok with `lombok.config` to avoid having to test the models getters and setters. 