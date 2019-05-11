package com.ticketbooking.app;

import org.springframework.data.jpa.repository.JpaRepository;

interface SeatRepository extends JpaRepository<Seat, Long> {
}
