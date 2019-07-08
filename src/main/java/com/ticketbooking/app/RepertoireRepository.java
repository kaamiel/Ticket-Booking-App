package com.ticketbooking.app;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

interface RepertoireRepository extends JpaRepository<Repertoire, Long> {

    List<Repertoire> findAllByOrderByMovieTitleAscDateTimeAsc();

    List<Repertoire> findAllByDateTimeGreaterThanEqualAndDateTimeLessThanEqualOrderByMovieTitleAscDateTimeAsc(LocalDateTime from, LocalDateTime to);

}
