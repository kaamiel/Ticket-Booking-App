package com.ticketbooking.app;


import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
class ScreeningController {

    private final ScreeningRepository repository;
    private final ScreeningResourceAssembler assembler;

    ScreeningController(ScreeningRepository repository, ScreeningResourceAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    @GetMapping(value = "/screenings", produces = "application/json; charset=UTF-8")
    Resources<Resource<Screening>> all() {
        List<Resource<Screening>> screenings = repository.findAll()
                .stream()
                .map(assembler::toResource)
                .collect(Collectors.toList());

        return new Resources<>(screenings,
                linkTo(methodOn(ScreeningController.class).all()).withSelfRel());
    }

    @GetMapping(value = "/screenings/{id:[\\d]+}", produces = "application/json; charset=UTF-8")
    Resource<Screening> one(@PathVariable Long id) {
        Screening screening = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("screening", id));

        return assembler.toResource(screening);
    }
}
