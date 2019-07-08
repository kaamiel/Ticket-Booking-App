package com.ticketbooking.app;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
class RepertoireResourceAssembler implements ResourceAssembler<Repertoire, Resource<Repertoire>> {

    @Override
    public Resource<Repertoire> toResource(Repertoire repertoire) {
        List<Link> links = new ArrayList<>();
        links.add(linkTo(methodOn(RepertoireController.class).one(repertoire.getId())).withSelfRel());
        if (repertoire.isScreeninged()) {
            links.add(linkTo(methodOn(ScreeningController.class).one(repertoire.getId())).withRel("screening"));
        }
        links.add(linkTo(methodOn(RepertoireController.class).all()).withRel("repertoire"));

        return new Resource<>(repertoire, links);
    }
}
