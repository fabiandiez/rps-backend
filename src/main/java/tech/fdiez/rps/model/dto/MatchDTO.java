package tech.fdiez.rps.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import tech.fdiez.rps.model.Move;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record MatchDTO(
    Long id,
    String playerId,
    Integer myScore,
    Integer opponentScore,
    Move myMove,
    Move opponentMove,
    Integer currentRound
) {
}