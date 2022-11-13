package tech.fdiez.rps.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tech.fdiez.rps.exception.MatchNotFoundException;
import tech.fdiez.rps.model.Move;
import tech.fdiez.rps.model.domain.Match;
import tech.fdiez.rps.model.dto.MatchDTO;
import tech.fdiez.rps.model.dto.MatchDTOFactory;
import tech.fdiez.rps.model.entity.MatchEntity;
import tech.fdiez.rps.repository.MatchRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MatchService {

    private final MatchRepository matchRepository;

    private final MatchDTOFactory matchDTOFactory;

    public MatchDTO getMatch(Long id, String playerId) {
        MatchEntity matchEntity = findMatchEntity(id);
        log.info("Found match with id {}", matchEntity.getId());

        return matchDTOFactory.fromEntity(matchEntity, playerId);
    }

    public MatchDTO createMatch() {
        MatchEntity matchEntity = new MatchEntity();
        matchEntity = matchRepository.save(matchEntity);
        log.info("Created match with id {} for player with id {}", matchEntity.getId(), matchEntity.getPlayerOneId());
        return matchDTOFactory.fromEntity(matchEntity, matchEntity.getPlayerOneId());
    }

    public MatchDTO joinMatch(Long matchId) {
        MatchEntity matchEntity = findMatchEntity(matchId);
        String playerId = matchEntity.getPlayerTwoId();

        log.info("Player with id {} joined match with id {}", playerId, matchEntity.getId());
        return matchDTOFactory.fromEntity(matchEntity, playerId);
    }

    public MatchDTO playMove(Long matchId, String playerId, Move move) {
        MatchEntity matchEntity = findMatchEntity(matchId);

        Match match = Match.fromEntity(matchEntity);
        match.playMove(playerId, move);
        log.info("Player with id {} played move {} in match with id {}", playerId, move, matchEntity.getId());

        matchEntity = MatchEntity.fromDomain(match);
        matchEntity = matchRepository.save(matchEntity);

        return matchDTOFactory.fromEntity(matchEntity, playerId);
    }


    private MatchEntity findMatchEntity(Long id) {
        Optional<MatchEntity> optionalMatchEntity = matchRepository.findById(id);
        return optionalMatchEntity.orElseThrow(() -> new MatchNotFoundException(id));
    }

}