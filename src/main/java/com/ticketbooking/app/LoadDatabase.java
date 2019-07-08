package com.ticketbooking.app;


import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.*;

@Configuration
@Slf4j
class LoadDatabase {

    @Bean
    CommandLineRunner initDatabase(SeatRepository seatRepository, RoomRepository roomRepository,
                                   MovieRepository movieRepository, RepertoireRepository repertoireRepository,
                                   ScreeningRepository screeningRepository,
                                   ReservationRepository reservationRepository) {
        Set<Seat> seats = new HashSet<>();
        List<Room> rooms = new ArrayList<>();
        for (int rowNum = 1; rowNum <= 2; ++rowNum) {
            for (int nr = 1; nr <= 3; ++nr) {
                seats.add(new Seat(rowNum, nr));
            }
        }
        rooms.add(new Room(new HashSet<>(seats)));
        rooms.add(new Room(new HashSet<>(seats)));
        for (int rowNum = 1; rowNum <= 3; ++rowNum) {
            for (int nr = 1; nr <= 5; ++nr) {
                seats.add(new Seat(rowNum, nr));
            }
        }
        rooms.add(new Room(new HashSet<>(seats)));
        rooms.add(new Room(new HashSet<>(seats)));

        List<Movie> movies = new ArrayList<>(Arrays.asList(
                new Movie("Movie1"),
                new Movie("Movie2"),
                new Movie("Movie3")
        ));

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime dateTime = LocalDateTime.of(now.getYear(), now.getMonth(),
                now.getDayOfMonth() + 5, 12, 30);

        List<Repertoire> repertoires = new ArrayList<>();
        for (int days = 0; days <= 2; ++days) {
            for (int minutes = 0, movie = 0; minutes <= 2; ++minutes, movie = (movie + 1) % movies.size()) {
                repertoires.add(new Repertoire(movies.get(movie), dateTime.plusDays(2 * days).plusMinutes(190 * minutes)));
            }
        }

        List<Screening> screenings = new ArrayList<>();
        for (int repertoire = 1, room = 0; repertoire < repertoires.size(); ++repertoire, room = (room + 1) % rooms.size()) {
            screenings.add(new Screening(repertoires.get(repertoire), rooms.get(room)));
        }

        return args -> {

            seatRepository.saveAll(seats);
            roomRepository.saveAll(rooms);
            movieRepository.saveAll(movies);
            screeningRepository.saveAll(screenings);
            repertoireRepository.saveAll(repertoires);

            for (JpaRepository<?, Long> repository : Arrays.asList(seatRepository, roomRepository,
                    movieRepository, repertoireRepository, screeningRepository, reservationRepository)) {
                repository.findAll().forEach(elem -> log.info("Preloaded " + elem));
            }
        };
    }
}
