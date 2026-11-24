package com.watchlist.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.watchlist.backend.model.Nomination;
import com.watchlist.backend.service.NominationService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@RestController
@RequestMapping("/api")
public class NominationController {

    private final NominationService nominationService;

    public NominationController(NominationService nominationService) {
        this.nominationService = nominationService;
    }

    public record AddNominationRequest(
            @NotBlank(message = "Movie title is required") String title,
            String moodTag
    ) {}

    @PostMapping("/rooms/{roomId}/nominations")
    public ResponseEntity<Nomination> addNomination(
            @PathVariable Long roomId,
            @Valid @RequestBody AddNominationRequest request) {
        Nomination created = nominationService.addNomination(roomId, request.title(), request.moodTag());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/rooms/{roomId}/nominations")
    public List<Nomination> getRoomNominations(@PathVariable Long roomId) {
        return nominationService.getRoomNominations(roomId);
    }

    @PostMapping("/nominations/{nominationId}/upvote")
    public ResponseEntity<Nomination> upvote(@PathVariable Long nominationId) {
        return ResponseEntity.ok(nominationService.upvote(nominationId));
    }

    @PostMapping("/rooms/{roomId}/spin")
    public ResponseEntity<Nomination> spinRoulette(@PathVariable Long roomId) {
        return ResponseEntity.ok(nominationService.spinRoulette(roomId));
    }
}
