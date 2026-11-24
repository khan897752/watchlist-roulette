package com.watchlist.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.watchlist.backend.model.WatchRoom;
import com.watchlist.backend.repository.WatchRoomRepository;

@Service
public class WatchRoomService {

    private final WatchRoomRepository watchRoomRepository;

    public WatchRoomService(WatchRoomRepository watchRoomRepository) {
        this.watchRoomRepository = watchRoomRepository;
    }

    @Transactional
    public WatchRoom createRoom(String name) {
        WatchRoom room = new WatchRoom();
        room.setName(name.trim());
        return watchRoomRepository.save(room);
    }

    @Transactional(readOnly = true)
    public List<WatchRoom> getAllRooms() {
        return watchRoomRepository.findAll();
    }

    @Transactional(readOnly = true)
    public WatchRoom getByInviteCode(String inviteCode) {
        return watchRoomRepository.findByInviteCode(inviteCode.trim().toUpperCase())
                .orElseThrow(() -> new IllegalArgumentException("Room not found for invite code: " + inviteCode));
    }
}
