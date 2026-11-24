package com.watchlist.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.watchlist.backend.model.Nomination;
import com.watchlist.backend.model.NominationStatus;

public interface NominationRepository extends JpaRepository<Nomination, Long> {
    List<Nomination> findByRoomIdOrderByVoteCountDescCreatedAtAsc(Long roomId);
    List<Nomination> findByRoomIdAndStatus(Long roomId, NominationStatus status);
}
