package tech.fdiez.rps.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import tech.fdiez.rps.exception.AlreadyPlayedMoveException;
import tech.fdiez.rps.exception.MatchNotFoundException;
import tech.fdiez.rps.model.Move;
import tech.fdiez.rps.model.dto.MatchDTO;
import tech.fdiez.rps.service.MatchService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MatchControllerTest {

    private MatchService matchServiceMock;

    private MatchController matchController;

    @BeforeEach
    void setUp() {
        matchServiceMock = mock(MatchService.class);
        matchController = new MatchController(matchServiceMock);
    }

    @Test
    void shouldReturnMatchDTOIfMatchExists() {
        Long matchId = 123L;
        String playerId = "123";
        MatchDTO matchDTO = MatchDTO.builder().build();
        when(matchServiceMock.getMatch(matchId, playerId)).thenReturn(matchDTO);

        ResponseEntity<MatchDTO> actual = matchController.getMatch(matchId, playerId);

        verify(matchServiceMock).getMatch(matchId, playerId);
        assertEquals(actual.getBody(), matchDTO);
    }

    @Test
    void shouldThrowMatchNotFoundExceptionIfMatchDoesNotExist() {
        Long matchId = 123L;
        String playerId = "123";
        when(matchServiceMock.getMatch(matchId, playerId)).thenThrow(new MatchNotFoundException(matchId));

        Assertions.assertThrows(MatchNotFoundException.class, () -> matchController.getMatch(matchId, playerId));
    }

    @Test
    void shouldCreateMatch() {
        MatchDTO matchDTO = MatchDTO.builder().build();
        when(matchServiceMock.createMatch()).thenReturn(matchDTO);

        ResponseEntity<MatchDTO> actual = matchController.createMatch();

        verify(matchServiceMock).createMatch();
        assertEquals(actual.getBody(), matchDTO);
    }


    @Test
    void shouldJoinMatchIfMatchExistsAndP2IsMissing() {
        Long matchId = 123L;
        MatchDTO matchDTO = MatchDTO.builder().build();
        when(matchServiceMock.joinMatch(matchId)).thenReturn(matchDTO);

        ResponseEntity<MatchDTO> actual = matchController.joinMatch(matchId);

        verify(matchServiceMock).joinMatch(matchId);
        assertEquals(actual.getBody(), matchDTO);
    }

    @Test
    void shouldThrowMatchNotFoundExceptionIfMatchDoesNotExistWhenJoining() {
        Long matchId = 123L;
        when(matchServiceMock.joinMatch(matchId)).thenThrow(new MatchNotFoundException(matchId));

        Assertions.assertThrows(MatchNotFoundException.class, () -> matchController.joinMatch(matchId));
    }

    @Test
    void shouldPlayMove() {
        Long matchId = 123L;
        String playerId = "123";
        Move move = Move.ROCK;
        MatchDTO matchDTO = MatchDTO.builder().build();
        when(matchServiceMock.playMove(matchId, playerId, move)).thenReturn(matchDTO);

        ResponseEntity<MatchDTO> actual = matchController.playMove(matchId, playerId, move);

        verify(matchServiceMock).playMove(matchId, playerId, move);
        assertEquals(actual.getBody(), matchDTO);
    }

    @Test
    void shouldThrowAlreadyPlayedMoveExceptionIfPlayerAlreadyPlayedMove() {
        Long matchId = 123L;
        String playerId = "123";
        Move move = Move.ROCK;
        when(matchServiceMock.playMove(matchId, playerId, move)).thenThrow(new AlreadyPlayedMoveException(playerId));

        Assertions.assertThrows(AlreadyPlayedMoveException.class, () -> matchController.playMove(matchId, playerId, move));
    }
}