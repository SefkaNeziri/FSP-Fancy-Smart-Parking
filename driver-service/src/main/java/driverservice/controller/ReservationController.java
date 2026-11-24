package driverservice.controller;

import driverservice.dto.ReservationRequest;
import driverservice.model.Reservation;
import driverservice.service.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

/**
 * Use case 2 (reserve), use case 6 (cancel), use case 5 (time left).
 */
@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    // POST /api/reservations
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Reservation create(@RequestBody ReservationRequest request) {
        return reservationService.create(request);
    }

    // GET /api/reservations/{id}  -> details + price (use case 4, step 2)
    @GetMapping("/{id}")
    public Reservation getById(@PathVariable UUID id) {
        return reservationService.getById(id);
    }

    // PUT /api/reservations/{id}/cancel
    @PutMapping("/{id}/cancel")
    public Reservation cancel(@PathVariable UUID id) {
        return reservationService.cancel(id);
    }

    // GET /api/reservations/{id}/time-left
    @GetMapping("/{id}/time-left")
    public Map<String, Object> timeLeft(@PathVariable UUID id) {
        return Map.of("reservationId", id, "minutesLeft", reservationService.minutesLeft(id));
    }
}
