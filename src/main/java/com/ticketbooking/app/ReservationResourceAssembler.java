package com.ticketbooking.app;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

@Component
class ReservationResourceAssembler implements ResourceAssembler<Reservation, Resource<Reservation>> {

    @Override
    public Resource<Reservation> toResource(Reservation reservation) {
        return new Resource<>(reservation,
                linkTo(methodOn(ReservationController.class).one(reservation.getId())).withSelfRel(),
                linkTo(methodOn(ReservationController.class).all()).withRel("reservations"));
    }
}
