package com.watchlist.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.watchlist.backend.model.WatchRoom;

public interface WatchRoomRepository extends JpaRepository<WatchRoom, Long> {
    Optional<WatchRoom> findByInviteCode(String inviteCode);
}
