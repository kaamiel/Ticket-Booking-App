package com.ticketbooking.app;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
class RepertoireController {

    private final RepertoireRepository repository;
    private final RepertoireResourceAssembler assembler;

    RepertoireController(RepertoireRepository repository, RepertoireResourceAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    @GetMapping(value = "/repertoire", produces = "application/json; charset=UTF-8")
    Resources<Resource<Repertoire>> all() {
        List<Resource<Repertoire>> repertoire = repository.findAllByOrderByMovieTitleAscDateTimeAsc()
                .stream()
                .map(assembler::toResource)
                .collect(Collectors.toList());

        return new Resources<>(repertoire,
                linkTo(methodOn(RepertoireController.class).all()).withSelfRel());
    }

    @GetMapping(value = "/repertoire/{id:[\\d]+}", produces = "application/json; charset=UTF-8")
    Resource<Repertoire> one(@PathVariable Long id) {
        Repertoire repertoire = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("repertoire", id));

        return assembler.toResource(repertoire);
    }

    @GetMapping(value = "/repertoire/{from},{to}", produces = "application/json; charset=UTF-8")
    Resources<Resource<Repertoire>> byDate(@PathVariable("from") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime from,
                                           @PathVariable("to") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime to) {
        List<Resource<Repertoire>> repertoire =
                repository.findAllByDateTimeGreaterThanEqualAndDateTimeLessThanEqualOrderByMovieTitleAscDateTimeAsc(from, to)
                        .stream()
                        .map(assembler::toResource)
                        .collect(Collectors.toList());

        return new Resources<>(repertoire,
                linkTo(methodOn(RepertoireController.class).all()).withSelfRel());
    }
}
