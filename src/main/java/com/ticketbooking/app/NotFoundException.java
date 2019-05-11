package com.ticketbooking.app;

class NotFoundException extends RuntimeException {

    NotFoundException(String what, Long id) {
        super("Could not find " + what + " " + id + ".");
    }

}
