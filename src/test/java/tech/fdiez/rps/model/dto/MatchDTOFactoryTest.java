package tech.fdiez.rps.model.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tech.fdiez.rps.exception.PlayerNotInMatchException;
import tech.fdiez.rps.model.Move;
import tech.fdiez.rps.model.entity.MatchEntity;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MatchDTOFactoryTest {

    private final MatchDTOFactory matchDTOFactory = new MatchDTOFactory();

    private final Long matchId = 123L;

    private final String playerAId = "123";

    private final String playerBId = "456";


    @Test
    void shouldReturnMatchDTOForPlayerOne() {
        MatchEntity matchEntityMock = mockMatchEntity();

        MatchDTO actual = matchDTOFactory.fromEntity(matchEntityMock, playerAId);

        assertEquals(matchId, actual.id());
        assertEquals(playerAId, actual.playerId());
        assertEquals(0, actual.myScore());
        assertEquals(1, actual.opponentScore());
        assertEquals(Move.ROCK, actual.myMove());
        assertEquals(Move.PAPER, actual.opponentMove());
        assertEquals(1, actual.currentRound());
    }

    @Test
    void shouldNotRevealOpponentMoveIfPlayerHasNotPlayed() {
        MatchEntity matchEntityMock = mockMatchEntity();
        when(matchEntityMock.getPlayerOneMoves()).thenReturn(Collections.emptyList());

        MatchDTO actual = matchDTOFactory.fromEntity(matchEntityMock, playerAId);

        assertNull(actual.opponentMove());
    }


    @Test
    void shouldReturnMatchDTOForPlayerTwo() {
        MatchEntity matchEntityMock = mockMatchEntity();

        MatchDTO actual = matchDTOFactory.fromEntity(matchEntityMock, playerBId);

        assertEquals(matchId, actual.id());
        assertEquals(playerBId, actual.playerId());
        assertEquals(1, actual.myScore());
        assertEquals(0, actual.opponentScore());
        assertEquals(Move.PAPER, actual.myMove());
        assertEquals(Move.ROCK, actual.opponentMove());
        assertEquals(1, actual.currentRound());
    }

    @Test
    void shouldThrowExceptionWhenPlayerIsNotInMatch() {
        MatchEntity matchEntityMock = mockMatchEntity();

        assertThrows(PlayerNotInMatchException.class, () -> matchDTOFactory.fromEntity(matchEntityMock, "789"));
    }

    private MatchEntity mockMatchEntity() {
        MatchEntity matchEntity = mock(MatchEntity.class);
        when(matchEntity.getId()).thenReturn(matchId);
        when(matchEntity.getPlayerOneId()).thenReturn(playerAId);
        when(matchEntity.getPlayerTwoId()).thenReturn(playerBId);
        when(matchEntity.getPlayerOneMoves()).thenReturn(Collections.singletonList(Move.ROCK));
        when(matchEntity.getPlayerTwoMoves()).thenReturn(Collections.singletonList(Move.PAPER));
        when(matchEntity.getCurrentRound()).thenReturn(1);
        when(matchEntity.getPlayerOneScore()).thenReturn(0);
        when(matchEntity.getPlayerTwoScore()).thenReturn(1);
        return matchEntity;
    }

}