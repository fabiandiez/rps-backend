package tech.fdiez.rps.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;


public class AlreadyPlayedMoveException extends ResponseStatusException {
    public AlreadyPlayedMoveException(String playerId) {
        super(HttpStatus.CONFLICT, String.format("Player with id %s already played a move", playerId));
    }
}
