package com.ticketbooking.app;


import lombok.Data;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Data
class Reservation {

    private @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotNull(message = "The reservation must apply to a screening.")
    @ManyToOne
    private Screening screening;

    @OneToMany(cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    @OrderBy("ticketType")
    @NotEmpty(message = "The reservation must apply to at least one ticket.")
    private Set<Ticket> tickets;

    @NotEmpty(message = "Name must not be empty.")
    @Pattern(regexp = "^\\p{IsUppercase}[\\p{IsAlphabetic}]{2,}$",
            message = "Name should be at least three characters long, start with a capital letter " +
                    "and consist only of alphabetic characters.")
    private String name;

    @NotEmpty(message = "Surname must not be empty.")
    @Pattern(regexp = "^\\p{IsUppercase}[\\p{IsAlphabetic}]{2,}$|^\\p{IsUppercase}[\\p{IsAlphabetic}]+-\\p{IsUppercase}[\\p{IsAlphabetic}]{2,}$",
            message = "Surname should be at least three characters long, start with a capital letter and consist " +
                    "only of alphabetic characters. Surname may consist of two parts separated with a single dash.")
    private String surname;

    private double amountToPay;
    private LocalDateTime expirationTime;

    Reservation() {
    }

    private Reservation(Screening screening, Set<Ticket> tickets, String name, String surname,
                        double amountToPay, LocalDateTime expirationTime) {
        this.screening = screening;
        this.tickets = tickets;
        this.name = name;
        this.surname = surname;
        this.amountToPay = amountToPay;
        this.expirationTime = expirationTime;
    }

    Reservation(Screening screening, Set<Ticket> tickets, String name, String surname) {
        this(screening, tickets, name, surname, 0, null);
        this.update();
    }

    Set<Seat> getSeats() {
        return this.tickets.stream().map(Ticket::getSeat).collect(Collectors.toSet());
    }

    void update() {
        this.amountToPay = this.tickets.stream().mapToDouble(ticket -> ticket.getTicketType().getPrice()).sum();
        if (this.expirationTime == null) { // the expiration time is at most an hour
            this.expirationTime = (LocalDateTime.now().plusMinutes(60)
                    .isBefore(this.screening.getRepertoire().getDateTime().minusMinutes(15))
                    ? LocalDateTime.now().plusMinutes(60)
                    : this.screening.getRepertoire().getDateTime().minusMinutes(15));
        }
    }

    boolean tooLateToBook() {
        return LocalDateTime.now().isAfter(this.screening.getRepertoire().getDateTime().minusMinutes(15));
    }

    boolean incorrectSeats() {
        Set<Seat> seats = this.getSeats();
        if (this.tickets.size() != seats.size() // repetition in the set of chosen seats
                || !this.screening.getAvailableSeats().containsAll(seats)) {
            return true;
        }
        Set<Seat> allSeats = this.getScreening().getRoom().getSeats();
        for (Seat seat : this.screening.getAvailableSeats()) {
            Seat prev = new Seat(seat.getRowNr(), seat.getNr() - 1);
            Seat next = new Seat(seat.getRowNr(), seat.getNr() + 1);
            if (!seats.contains(seat) && allSeats.contains(prev) && allSeats.contains(next) &&
                    (!this.screening.getAvailableSeats().contains(prev) || seats.contains(prev)) &&
                    (!this.screening.getAvailableSeats().contains(next) || seats.contains(next))) {
                return true; // single place left over in a row between two already reserved places
            }
        }
        return false;
    }
}