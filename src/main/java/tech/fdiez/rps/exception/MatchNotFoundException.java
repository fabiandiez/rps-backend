package tech.fdiez.rps.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;


public class MatchNotFoundException extends ResponseStatusException {
    public MatchNotFoundException(Long id) {
        super(HttpStatus.NOT_FOUND, String.format("Match with id %s not found", id));
    }
}
