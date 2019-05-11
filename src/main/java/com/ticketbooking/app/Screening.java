package com.ticketbooking.app;


import lombok.Data;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Data
class Screening {

    private @Id
    Long id;

    @OneToOne
    @MapsId
    @NotNull
    private Repertoire repertoire;
    @ManyToOne
    @NotNull
    private Room room;
    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    @OrderBy("rowNr, nr")
    private Set<Seat> availableSeats;

    Screening() {
    }

    private Screening(Repertoire repertoire, Room room, Set<Seat> availableSeats) {
        this.repertoire = repertoire;
        this.room = room;
        this.availableSeats = availableSeats;
        repertoire.setScreeninged(true);
    }

    Screening(Repertoire repertoire, Room room) {
        this(repertoire, room, room.getSeats());
    }

}
