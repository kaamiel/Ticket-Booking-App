package com.ticketbooking.app;

import org.springframework.data.jpa.repository.JpaRepository;

interface ScreeningRepository extends JpaRepository<Screening, Long> {
}
