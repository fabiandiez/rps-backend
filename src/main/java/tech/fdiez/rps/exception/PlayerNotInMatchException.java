package tech.fdiez.rps.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;


public class PlayerNotInMatchException extends ResponseStatusException {
    public PlayerNotInMatchException(String playerId, Long matchId) {
        super(HttpStatus.FORBIDDEN, String.format("Player with id %s is not in match %d", playerId, matchId));
    }
}
