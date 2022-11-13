package tech.fdiez.rps.model.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.fdiez.rps.model.Move;
import tech.fdiez.rps.model.domain.Match;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MatchEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String playerOneId = UUID.randomUUID().toString();

    private String playerTwoId = UUID.randomUUID().toString();

    private Integer playerOneScore = 0;

    private Integer playerTwoScore = 0;

    private Integer currentRound = 1;

    @ElementCollection
    private List<Move> playerOneMoves = List.of();

    @ElementCollection
    private List<Move> playerTwoMoves = List.of();

    public static MatchEntity fromDomain(Match match) {
        return new MatchEntity(
            match.getId(),
            match.getPlayerOneId(),
            match.getPlayerTwoId(),
            match.getPlayerOneScore(),
            match.getPlayerTwoScore(),
            match.getCurrentRound(),
            match.getPlayerOneMoves(),
            match.getPlayerTwoMoves()
        );
    }

}
