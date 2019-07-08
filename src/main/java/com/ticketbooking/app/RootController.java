package com.ticketbooking.app;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class RootController {

    @GetMapping
    ResourceSupport index() {
        ResourceSupport rootResource = new ResourceSupport();

        rootResource.add(linkTo(methodOn(RepertoireController.class).all()).withRel("repertoire"));
        rootResource.add(linkTo(methodOn(ScreeningController.class).all()).withRel("screenings"));
        rootResource.add(linkTo(methodOn(ReservationController.class).all()).withRel("reservations"));
        
        return rootResource;
    }

}
