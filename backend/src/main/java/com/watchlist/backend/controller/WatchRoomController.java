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

import com.watchlist.backend.model.WatchRoom;
import com.watchlist.backend.service.WatchRoomService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@RestController
@RequestMapping("/api/rooms")
public class WatchRoomController {

    private final WatchRoomService watchRoomService;

    public WatchRoomController(WatchRoomService watchRoomService) {
        this.watchRoomService = watchRoomService;
    }

    public record CreateRoomRequest(@NotBlank(message = "Room name is required") String name) {}

    @PostMapping
    public ResponseEntity<WatchRoom> createRoom(@Valid @RequestBody CreateRoomRequest request) {
        WatchRoom created = watchRoomService.createRoom(request.name());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public List<WatchRoom> getAllRooms() {
        return watchRoomService.getAllRooms();
    }

    @GetMapping("/code/{inviteCode}")
    public ResponseEntity<WatchRoom> getRoomByCode(@PathVariable String inviteCode) {
        return ResponseEntity.ok(watchRoomService.getByInviteCode(inviteCode));
    }
}
