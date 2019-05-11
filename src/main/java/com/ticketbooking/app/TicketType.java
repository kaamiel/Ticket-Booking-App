package com.ticketbooking.app;


import lombok.Getter;

enum TicketType {

    ADULT(25),
    STUDENT(18),
    CHILD(12.5);

    @Getter
    private final double price;

    TicketType(double price) {
        this.price = price;
    }

}
