package tech.fdiez.rps.model.dto;

import org.springframework.stereotype.Service;
import tech.fdiez.rps.exception.PlayerNotInMatchException;
import tech.fdiez.rps.model.Move;
import tech.fdiez.rps.model.domain.Match;
import tech.fdiez.rps.model.entity.MatchEntity;

@Service
public class MatchDTOFactory {

    public MatchDTO fromEntity(MatchEntity entity, String playerId) {
        return fromDomain(Match.fromEntity(entity), playerId);
    }

    public MatchDTO fromDomain(Match match, String playerId) {
        Move myMove;
        Move opponentMove;
        Integer myScore;
        Integer opponentScore;
        if (match.getPlayerOneId().equals(playerId)) {
            myMove = match.getLatestPlayerOneMove().orElse(null);
            myScore = match.getPlayerOneScore();
            opponentMove = match.getLatestPlayerTwoMove().orElse(null);
            opponentScore = match.getPlayerTwoScore();
        } else if (match.getPlayerTwoId().equals(playerId)) {
            myMove = match.getLatestPlayerTwoMove().orElse(null);
            myScore = match.getPlayerTwoScore();
            opponentMove = match.getLatestPlayerOneMove().orElse(null);
            opponentScore = match.getPlayerOneScore();
        } else {
            throw new PlayerNotInMatchException(playerId, match.getId());
        }

        // do not reveal opponent's move if you have not played yet
        if (myMove == null) {
            opponentMove = null;
        }
        return MatchDTO.builder()
            .id(match.getId())
            .playerId(playerId)
            .myScore(myScore)
            .opponentScore(opponentScore)
            .myMove(myMove)
            .opponentMove(opponentMove)
            .currentRound(match.getCurrentRound())
            .build();
    }
}
