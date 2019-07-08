package com.ticketbooking.app;


import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Entity
@Data
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"rowNr", "nr"})
})
class Seat {

    private @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Exclude
    Long id;

    private int rowNr;
    private int nr;

    Seat() {
    }

    Seat(int rowNr, int nr) {
        this.rowNr = rowNr;
        this.nr = nr;
    }
}