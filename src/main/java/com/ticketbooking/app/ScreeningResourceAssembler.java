package com.ticketbooking.app;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

@Component
class ScreeningResourceAssembler implements ResourceAssembler<Screening, Resource<Screening>> {

    @Override
    public Resource<Screening> toResource(Screening screening) {
        return new Resource<>(screening,
                linkTo(methodOn(ScreeningController.class).one(screening.getId())).withSelfRel(),
                linkTo(methodOn(RepertoireController.class).one(screening.getId())).withRel("repertoire"),
                linkTo(methodOn(ScreeningController.class).all()).withRel("screenings"));
    }

}
