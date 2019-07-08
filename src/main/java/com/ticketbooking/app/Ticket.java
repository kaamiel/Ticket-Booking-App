package com.ticketbooking.app;


import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Data
class Ticket {

    private @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotNull
    private TicketType ticketType;
    @ManyToOne
    @NotNull
    private Seat seat;

    Ticket() {
    }

    Ticket(TicketType ticketType, Seat seat) {
        this.ticketType = ticketType;
        this.seat = seat;
    }

}
