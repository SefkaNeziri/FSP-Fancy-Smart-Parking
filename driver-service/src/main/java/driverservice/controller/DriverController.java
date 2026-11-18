package driverservice.controller;

import driverservice.dto.DriverProfileResponse;
import driverservice.dto.LoginRequest;
import driverservice.dto.RegisterRequest;
import driverservice.model.Notification;
import driverservice.model.Reservation;
import driverservice.model.Vehicle;
import driverservice.service.DriverService;
import driverservice.service.NotificationService;
import driverservice.service.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Use case 3 (register / login) + driver data (vehicles, reservations, notifications).
 */
@RestController
@RequestMapping("/api/drivers")
public class DriverController {

    private final DriverService driverService;
    private final ReservationService reservationService;
    private final NotificationService notificationService;

    public DriverController(DriverService driverService,
                            ReservationService reservationService,
                            NotificationService notificationService) {
        this.driverService = driverService;
        this.reservationService = reservationService;
        this.notificationService = notificationService;
    }

    // POST /api/drivers/register
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public DriverProfileResponse register(@RequestBody RegisterRequest request) {
        return driverService.register(request);
    }

    // POST /api/drivers/login
    @PostMapping("/login")
    public DriverProfileResponse login(@RequestBody LoginRequest request) {
        return driverService.login(request);
    }

    // GET /api/drivers/{id}  -> dashboard data
    @GetMapping("/{id}")
    public DriverProfileResponse getProfile(@PathVariable UUID id) {
        return driverService.getProfile(id);
    }

    // GET /api/drivers/{id}/vehicles
    @GetMapping("/{id}/vehicles")
    public List<Vehicle> getVehicles(@PathVariable UUID id) {
        return driverService.getVehicles(id);
    }

    // POST /api/drivers/{id}/vehicles
    @PostMapping("/{id}/vehicles")
    @ResponseStatus(HttpStatus.CREATED)
    public Vehicle addVehicle(@PathVariable UUID id, @RequestBody Vehicle vehicle) {
        return driverService.addVehicle(id, vehicle);
    }

    // GET /api/drivers/{id}/reservations
    @GetMapping("/{id}/reservations")
    public List<Reservation> getReservations(@PathVariable UUID id) {
        return reservationService.getForDriver(id);
    }

    // GET /api/drivers/{id}/notifications  (use case 5)
    @GetMapping("/{id}/notifications")
    public List<Notification> getNotifications(@PathVariable UUID id) {
        return notificationService.getForDriver(id);
    }
}
