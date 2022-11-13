package tech.fdiez.rps.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tech.fdiez.rps.exception.MatchNotFoundException;
import tech.fdiez.rps.model.Move;
import tech.fdiez.rps.model.dto.MatchDTO;
import tech.fdiez.rps.model.dto.MatchDTOFactory;
import tech.fdiez.rps.model.entity.MatchEntity;
import tech.fdiez.rps.repository.MatchRepository;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MatchServiceTest {

    private MatchService matchService;

    private final Long matchId = 123L;

    private final String playerAId = "123";

    private MatchRepository matchRepositoryMock;

    private MatchEntity matchEntityMock;

    private MatchDTO matchDTO;


    @BeforeEach
    void setUp() {
        matchRepositoryMock = mock(MatchRepository.class);
        matchEntityMock = mockMatchEntity();
        MatchDTOFactory matchDTOFactoryMock = mock(MatchDTOFactory.class);

        matchDTO = createMatchDTO();

        when(matchDTOFactoryMock.fromDomain(any(), any())).thenReturn(matchDTO);
        when(matchDTOFactoryMock.fromEntity(any(), any())).thenReturn(matchDTO);

        matchService = new MatchService(matchRepositoryMock, matchDTOFactoryMock);
    }

    @Test
    void shouldReturnMatchDTOIfMatchExists() {
        when(matchRepositoryMock.findById(matchId)).thenReturn(Optional.of(matchEntityMock));

        MatchDTO actual = matchService.getMatch(matchId, playerAId);

        verify(matchRepositoryMock).findById(matchId);
        assertEquals(matchDTO, actual);
    }

    @Test
    void shouldThrowMatchNotFoundExceptionIfMatchDoesNotExist() {
        when(matchRepositoryMock.findById(matchId)).thenReturn(Optional.empty());

        assertThrows(MatchNotFoundException.class, () -> matchService.getMatch(matchId, playerAId));
    }

    @Test
    void shouldCreateMatch() {
        when(matchRepositoryMock.save(any(MatchEntity.class))).thenReturn(matchEntityMock);

        MatchDTO actual = matchService.createMatch();

        verify(matchRepositoryMock).save(any(MatchEntity.class));
        assertEquals(matchDTO, actual);
    }

    @Test
    void shouldJoinMatch() {
        when(matchEntityMock.getPlayerTwoId()).thenReturn(playerAId);
        when(matchRepositoryMock.findById(matchId)).thenReturn(Optional.of(matchEntityMock));

        MatchDTO actual = matchService.joinMatch(matchId);

        verify(matchRepositoryMock).findById(matchId);
        assertEquals(matchDTO, actual);
    }

    @Test
    void shouldPlayMove() {
        when(matchRepositoryMock.findById(matchId)).thenReturn(Optional.of(matchEntityMock));
        when(matchRepositoryMock.save(any(MatchEntity.class))).thenReturn(matchEntityMock);

        MatchDTO actual = matchService.playMove(matchId, playerAId, Move.ROCK);

        verify(matchRepositoryMock).findById(matchId);
        verify(matchRepositoryMock).save(any(MatchEntity.class));
        assertEquals(matchDTO, actual);
    }

    private MatchDTO createMatchDTO() {
        return MatchDTO.builder()
            .id(matchId)
            .playerId(playerAId)
            .myMove(Move.ROCK)
            .opponentMove(Move.PAPER)
            .currentRound(1)
            .myScore(0)
            .opponentScore(1).build();
    }

    private MatchEntity mockMatchEntity() {
        MatchEntity matchEntity = mock(MatchEntity.class);
        when(matchEntity.getId()).thenReturn(matchId);
        when(matchEntity.getPlayerOneId()).thenReturn(playerAId);
        String playerBId = "456";
        when(matchEntity.getPlayerTwoId()).thenReturn(playerBId);
        when(matchEntity.getPlayerOneMoves()).thenReturn(Collections.singletonList(Move.ROCK));
        when(matchEntity.getPlayerTwoMoves()).thenReturn(Collections.singletonList(Move.PAPER));
        when(matchEntity.getCurrentRound()).thenReturn(1);
        when(matchEntity.getPlayerOneScore()).thenReturn(0);
        when(matchEntity.getPlayerTwoScore()).thenReturn(1);
        return matchEntity;
    }

}