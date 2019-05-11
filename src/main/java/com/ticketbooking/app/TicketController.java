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
class TicketController {

    private final TicketRepository repository;

    TicketController(TicketRepository repository) {
        this.repository = repository;
    }

    @GetMapping(value = "/tickets", produces = "application/json; charset=UTF-8")
    Resources<Resource<Ticket>> all() {

        List<Resource<Ticket>> tickets = repository.findAll()
                .stream()
                .map(ticket -> new Resource<>(ticket,
                        linkTo(methodOn(TicketController.class).one(ticket.getId())).withSelfRel(),
                        linkTo(methodOn(TicketController.class).all()).withRel("tickets")))
                .collect(Collectors.toList());

        return new Resources<>(tickets,
                linkTo(methodOn(TicketController.class).all()).withSelfRel());
    }

    @GetMapping(value = "/tickets/{id}", produces = "application/json; charset=UTF-8")
    Resource<Ticket> one(@PathVariable Long id) {

        Ticket ticket = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("ticket", id));

        return new Resource<>(ticket,
                linkTo(methodOn(TicketController.class).one(id)).withSelfRel(),
                linkTo(methodOn(TicketController.class).all()).withRel("tickets"));
    }
}
