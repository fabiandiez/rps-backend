package tech.fdiez.rps.model.domain;

import lombok.Builder;
import lombok.Getter;
import tech.fdiez.rps.exception.AlreadyPlayedMoveException;
import tech.fdiez.rps.exception.PlayerNotInMatchException;
import tech.fdiez.rps.model.Move;
import tech.fdiez.rps.model.entity.MatchEntity;

import java.util.ArrayList;
import java.util.Optional;

@Builder
@Getter
public class Match {

    private final Long id;

    private final String playerOneId;
    private final String playerTwoId;

    private final ArrayList<Move> playerOneMoves;
    private final ArrayList<Move> playerTwoMoves;

    private Integer playerOneScore;
    private Integer playerTwoScore;

    private Integer currentRound;

    public Optional<Move> getLatestPlayerOneMove() {
        return getMove(playerOneMoves);
    }

    public Optional<Move> getLatestPlayerTwoMove() {
        return getMove(playerTwoMoves);
    }

    private Optional<Move> getMove(ArrayList<Move> playerOneMoves) {
        try {
            return Optional.ofNullable(playerOneMoves.get(currentRound - 1));
        } catch (IndexOutOfBoundsException e) {
            return Optional.empty();
        }
    }


    public void playMove(String playerId, Move move) {
        updateRoundsCounter();
        updateMoves(playerId, move);
        updateScore();
    }

    private void updateRoundsCounter() {
        // If both players have already played a move, we move to the next round
        if (getLatestPlayerOneMove().isPresent() && getLatestPlayerTwoMove().isPresent()) {
            currentRound++;
        }
    }

    private void updateMoves(String playerId, Move move) {
        if (this.playerOneId.equals(playerId)) {
            if (this.getLatestPlayerOneMove().isPresent()) {
                throw new AlreadyPlayedMoveException(playerId);
            } else {
                playerOneMoves.add(move);
            }
        } else if (this.playerTwoId.equals(playerId)) {
            if (this.getLatestPlayerTwoMove().isPresent()) {
                throw new AlreadyPlayedMoveException(playerId);
            } else {
                playerTwoMoves.add(move);
            }
        } else {
            throw new PlayerNotInMatchException(playerId, id);
        }
    }

    private void updateScore() {
        Optional<Move> playerOneMove = getLatestPlayerOneMove();
        Optional<Move> playerTwoMove = getLatestPlayerTwoMove();
        if (playerOneMove.isPresent() && playerTwoMove.isPresent()) {
            if (playerOneMove.get().beats(playerTwoMove.get())) {
                playerOneScore++;
            } else if (playerTwoMove.get().beats(playerOneMove.get())) {
                playerTwoScore++;
            }
        }
    }

    public static Match fromEntity(MatchEntity entity) {
        return Match.builder()
            .id(entity.getId())
            .playerOneMoves(new ArrayList<>(entity.getPlayerOneMoves()))
            .playerTwoMoves(new ArrayList<>(entity.getPlayerTwoMoves()))
            .playerOneId(entity.getPlayerOneId())
            .playerTwoId(entity.getPlayerTwoId())
            .playerOneScore(entity.getPlayerOneScore())
            .playerTwoScore(entity.getPlayerTwoScore())
            .currentRound(entity.getCurrentRound())
            .build();
    }

}
