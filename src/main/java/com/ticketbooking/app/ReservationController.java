package com.ticketbooking.app;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
class ReservationController {

    private final ReservationRepository repository;
    private final ReservationResourceAssembler assembler;
    private final ScreeningRepository screeningRepository;

    ReservationController(ReservationRepository repository, ReservationResourceAssembler assembler,
                          ScreeningRepository screeningRepository) {
        this.repository = repository;
        this.assembler = assembler;
        this.screeningRepository = screeningRepository;
    }

    @GetMapping(value = "/reservations", produces = "application/json; charset=UTF-8")
    Resources<Resource<Reservation>> all() {
        List<Resource<Reservation>> reservations = repository.findAll()
                .stream()
                .map(assembler::toResource)
                .collect(Collectors.toList());

        return new Resources<>(reservations,
                linkTo(methodOn(ReservationController.class).all()).withSelfRel());
    }

    @GetMapping(value = "/reservations/{id}", produces = "application/json; charset=UTF-8")
    Resource<Reservation> one(@PathVariable Long id) {
        Reservation reservation = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("reservation", id));

        return assembler.toResource(reservation);
    }

    @PostMapping("/reservations")
    ResponseEntity<?> newReservation(@RequestBody @Valid Reservation newReservation) throws URISyntaxException {
        newReservation.update();

        if (newReservation.tooLateToBook()) {
            return ResponseEntity.badRequest()
                    .body("Seats can be booked at latest 15 minutes before the screening begins.");
        } else if (newReservation.incorrectSeats()) {
            return ResponseEntity.badRequest()
                    .body("Incorrect seats chosen (not available or " +
                            "caused single place left over in a row between two already reserved).");
        } else {
            Screening screening = screeningRepository.getOne(newReservation.getScreening().getId());
            screening.getAvailableSeats().removeAll(newReservation.getSeats());
            screeningRepository.save(screening);

            Resource<Reservation> resource = assembler.toResource(repository.save(newReservation));

            return ResponseEntity
                    .created(new URI(resource.getId().expand().getHref()))
                    .body(resource);
        }
    }
}
