package com.ticketbooking.app;


import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Data
class Movie {

    private @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(unique = true)
    @NotEmpty
    private String title;

    Movie() {
    }

    Movie(String title) {
        this.title = title;
    }

}
