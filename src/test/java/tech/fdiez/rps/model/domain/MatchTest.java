package tech.fdiez.rps.model.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tech.fdiez.rps.exception.AlreadyPlayedMoveException;
import tech.fdiez.rps.exception.PlayerNotInMatchException;
import tech.fdiez.rps.model.Move;
import tech.fdiez.rps.model.entity.MatchEntity;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MatchTest {

    private final Long matchId = 123L;

    private final String playerAId = "123";

    private final String playerBId = "456";

    private MatchEntity matchEntityMock;

    private Match match;

    @BeforeEach
    void setUp() {
        matchEntityMock = mockMatchEntity();
        match = Match.fromEntity(matchEntityMock);
    }

    @Test
    void shouldReturnLatestPlayerOneMove() {
        Move move = Move.ROCK;
        match.playMove(playerAId, move);
        match.playMove(playerBId, move);

        Optional<Move> latestPlayerOneMove = match.getLatestPlayerOneMove();

        assertTrue(latestPlayerOneMove.isPresent());
        assertEquals(move, latestPlayerOneMove.get());
    }

    @Test
    void shouldReturnLatestPlayerTwoMove() {
        Move move = Move.ROCK;
        match.playMove(playerAId, move);
        match.playMove(playerBId, move);

        Optional<Move> latestPlayerTwoMove = match.getLatestPlayerTwoMove();

        assertTrue(latestPlayerTwoMove.isPresent());
        assertEquals(move, latestPlayerTwoMove.get());
    }

    @Test
    void shouldReturnEmptyOptionalWhenNoMovesHaveBeenPlayed() {
        Optional<Move> latestPlayerOneMove = match.getLatestPlayerOneMove();
        Optional<Move> latestPlayerTwoMove = match.getLatestPlayerTwoMove();

        assertFalse(latestPlayerOneMove.isPresent());
        assertFalse(latestPlayerTwoMove.isPresent());
    }

    @Test
    void shouldIncreaseRoundCounterWhenMoveInNewRoundIsPlayed() {
        Move move = Move.ROCK;
        match.playMove(playerAId, move);
        match.playMove(playerBId, move);
        match.playMove(playerAId, move);

        assertEquals(2, match.getCurrentRound());
    }

    @Test
    void shouldIncreasePlayerOneScoreWhenPlayerOneWins() {
        match.playMove(playerAId, Move.ROCK);
        match.playMove(playerBId, Move.SCISSORS);

        assertEquals(1, match.getPlayerOneScore());
    }

    @Test
    void shouldIncreasePlayerTwoScoreWhenPlayerTwoWins() {
        match.playMove(playerAId, Move.ROCK);
        match.playMove(playerBId, Move.PAPER);

        assertEquals(1, match.getPlayerTwoScore());
    }

    @Test
    void shouldNotIncreaseScoreWhenDraw() {
        match.playMove(playerAId, Move.ROCK);
        match.playMove(playerBId, Move.ROCK);

        assertEquals(0, match.getPlayerOneScore());
        assertEquals(0, match.getPlayerTwoScore());
    }

    @Test
    void shouldThrowExceptionWhenPlayerOnePlaysMoveTwice() {
        match.playMove(playerAId, Move.ROCK);

        assertThrows(AlreadyPlayedMoveException.class, () -> match.playMove(playerAId, Move.PAPER));
    }

    @Test
    void shouldThrowExceptionWhenPlayerTwoPlaysMoveTwice() {
        match.playMove(playerBId, Move.ROCK);

        assertThrows(AlreadyPlayedMoveException.class, () -> match.playMove(playerBId, Move.PAPER));
    }

    @Test
    void shouldThrowExceptionWhenPlayerIsNotPartOfTheMatch() {
        assertThrows(PlayerNotInMatchException.class, () -> match.playMove("789", Move.PAPER));
    }

    private MatchEntity mockMatchEntity() {
        MatchEntity matchEntity = mock(MatchEntity.class);
        when(matchEntity.getId()).thenReturn(matchId);
        when(matchEntity.getPlayerOneId()).thenReturn(playerAId);
        when(matchEntity.getPlayerTwoId()).thenReturn(playerBId);
        when(matchEntity.getPlayerOneMoves()).thenReturn(Collections.emptyList());
        when(matchEntity.getPlayerTwoMoves()).thenReturn(Collections.emptyList());
        when(matchEntity.getCurrentRound()).thenReturn(1);
        when(matchEntity.getPlayerOneScore()).thenReturn(0);
        when(matchEntity.getPlayerTwoScore()).thenReturn(0);
        return matchEntity;
    }
}