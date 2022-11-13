package tech.fdiez.rps.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import tech.fdiez.rps.model.Move;
import tech.fdiez.rps.model.dto.MatchDTO;
import tech.fdiez.rps.service.MatchService;

@Controller
@RequestMapping("/api/v1/match")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Slf4j
public class MatchController {

    private final MatchService matchService;

    @GetMapping("/{matchId}")
    public ResponseEntity<MatchDTO> getMatch(@PathVariable Long matchId, @RequestParam String playerId) {
        log.info("Getting request for match with id {}", matchId);
        return ResponseEntity.ok(matchService.getMatch(matchId, playerId));
    }

    @PostMapping
    public ResponseEntity<MatchDTO> createMatch() {
        log.info("Getting request to create match");
        return new ResponseEntity<>(matchService.createMatch(), HttpStatus.CREATED);
    }

    @PutMapping("/{matchId}")
    public ResponseEntity<MatchDTO> joinMatch(@PathVariable Long matchId) {
        log.info("Getting request to join match {}", matchId);
        return new ResponseEntity<>(matchService.joinMatch(matchId), HttpStatus.OK);
    }

    @PutMapping("/{matchId}/moves")
    public ResponseEntity<MatchDTO> playMove(@PathVariable Long matchId, @RequestParam String playerId, @RequestParam Move move) {
        log.info("Getting request to play move {} for player {} in match {}", move, playerId, matchId);
        return ResponseEntity.ok(matchService.playMove(matchId, playerId, move));
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseStatusException handleException(ResponseStatusException e) {
        log.info("Error while processing request: {}", e.getMessage());
        throw e;
    }

}
