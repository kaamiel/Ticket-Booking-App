package com.ticketbooking.app;


import lombok.Data;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Entity
@Data
class Room {

    private @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long nr;

    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    @NotEmpty
    @OrderBy("rowNr, nr")
    private Set<Seat> seats;

    Room() {
    }

    Room(Set<Seat> seats) {
        this.seats = seats;
    }

}
