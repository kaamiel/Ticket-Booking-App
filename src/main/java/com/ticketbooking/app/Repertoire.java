package com.ticketbooking.app;


import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Data
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"movie_id", "dateTime"})
})
class Repertoire {

    private @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    private Movie movie;
    @NotNull
    private LocalDateTime dateTime;
    private boolean screeninged = false;

    Repertoire() {
    }

    Repertoire(Movie movie, LocalDateTime dateTime) {
        this.movie = movie;
        this.dateTime = dateTime;
    }

}
