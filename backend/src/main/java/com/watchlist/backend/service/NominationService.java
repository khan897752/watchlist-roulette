package com.watchlist.backend.service;

import com.watchlist.backend.model.Nomination;
import com.watchlist.backend.model.NominationStatus;
import com.watchlist.backend.model.WatchRoom;
import com.watchlist.backend.repository.NominationRepository;
import com.watchlist.backend.repository.WatchRoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class NominationService {

    private final NominationRepository nominationRepository;
    private final WatchRoomRepository watchRoomRepository;

    public NominationService(NominationRepository nominationRepository, WatchRoomRepository watchRoomRepository) {
        this.nominationRepository = nominationRepository;
        this.watchRoomRepository = watchRoomRepository;
    }

    @Transactional
    public Nomination addNomination(Long roomId, String title, String moodTag) {
        WatchRoom room = watchRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found: " + roomId));

        Nomination nomination = new Nomination();
        nomination.setRoom(room);
        nomination.setTitle(title.trim());
        nomination.setMoodTag(moodTag != null && !moodTag.isBlank() ? moodTag.trim() : "General");
        return nominationRepository.save(nomination);
    }

    @Transactional(readOnly = true)
    public List<Nomination> getRoomNominations(Long roomId) {
        return nominationRepository.findByRoomIdOrderByVoteCountDescCreatedAtAsc(roomId);
    }

    @Transactional
    public Nomination upvote(Long nominationId) {
        Nomination nomination = nominationRepository.findById(nominationId)
                .orElseThrow(() -> new IllegalArgumentException("Nomination not found: " + nominationId));
        nomination.setVoteCount(nomination.getVoteCount() + 1);
        return nominationRepository.save(nomination);
    }

    @Transactional
    public Nomination spinRoulette(Long roomId) {
        List<Nomination> pending = nominationRepository.findByRoomIdAndStatus(roomId, NominationStatus.PENDING);
        if (pending.isEmpty()) {
            throw new IllegalStateException("No pending nominations in this room to spin!");
        }

        // Weighted pool: every movie gets 1 base ticket + 1 extra ticket per upvote
        List<Nomination> ticketPool = new ArrayList<>();
        for (Nomination movie : pending) {
            int tickets = 1 + Math.max(0, movie.getVoteCount());
            for (int i = 0; i < tickets; i++) {
                ticketPool.add(movie);
            }
        }

        Nomination winner = ticketPool.get(ThreadLocalRandom.current().nextInt(ticketPool.size()));
        winner.setStatus(NominationStatus.WATCHED);
        return nominationRepository.save(winner);
    }
}
