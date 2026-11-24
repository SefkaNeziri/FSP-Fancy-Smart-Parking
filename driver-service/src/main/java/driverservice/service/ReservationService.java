package driverservice.service;

import driverservice.dto.ReservationRequest;
import driverservice.model.ParkingFacility;
import driverservice.model.ParkingSpot;
import driverservice.model.Payment;
import driverservice.model.Reservation;
import driverservice.repository.ParkingSpotRepository;
import driverservice.repository.PaymentRepository;
import driverservice.repository.ReservationRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Use case 2: Reserve a parking spot
 * Use case 6: Cancel a reservation
 */
@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ParkingSpotRepository spotRepository;
    private final PaymentRepository paymentRepository;
    private final ParkingService parkingService;
    private final DriverService driverService;
    private final NotificationService notificationService;

    public ReservationService(ReservationRepository reservationRepository,
                              ParkingSpotRepository spotRepository,
                              PaymentRepository paymentRepository,
                              ParkingService parkingService,
                              DriverService driverService,
                              NotificationService notificationService) {
        this.reservationRepository = reservationRepository;
        this.spotRepository = spotRepository;
        this.paymentRepository = paymentRepository;
        this.parkingService = parkingService;
        this.driverService = driverService;
        this.notificationService = notificationService;
    }

    /**
     * @Transactional = everything happens in one DB transaction:
     * either the reservation is created AND the spot is locked, or nothing happens.
     */
    @Transactional
    public Reservation create(ReservationRequest request) {
        driverService.getDriver(request.driverId()); // driver must exist
        if (request.durationHours() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Duration must be at least 1 hour");
        }
        if (request.startTime() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Start time is required");
        }

        // 1. verify the spot is still available
        ParkingSpot spot = spotRepository.findById(request.spotId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Spot not found"));
        if (!"AVAILABLE".equals(spot.getCurrentStatus())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Spot is not available");
        }

        // 2. calculate price = hourly rate * hours
        ParkingFacility facility = parkingService.getFacilityEntity(spot.getFacilityId());
        BigDecimal price = facility.getHourlyRate().multiply(BigDecimal.valueOf(request.durationHours()));

        // 3. create the reservation
        Reservation reservation = new Reservation();
        reservation.setDriverId(request.driverId());
        reservation.setSpotId(spot.getId());
        reservation.setVehicleId(request.vehicleId());
        reservation.setStartTime(request.startTime());
        reservation.setEndTime(request.startTime().plusHours(request.durationHours()));
        reservation.setTotalPrice(price);
        reservation.setStatus("PENDING_PAYMENT");
        reservation = reservationRepository.save(reservation);

        // 4. lock the spot -> nobody else can book it
        spot.setCurrentStatus("RESERVED");
        spotRepository.save(spot);

        // 5. notify the driver (use case 5, trigger A)
        notificationService.send(request.driverId(), reservation.getId(), "Reservation successful",
                "Spot " + spot.getSpotNumber() + " at " + facility.getName() + " is reserved. Price: " + price,
                "RESERVATION_CONFIRMATION");

        return reservation;
    }

    @Transactional
    public Reservation cancel(UUID reservationId) {
        Reservation reservation = getById(reservationId);
        if ("CANCELLED".equals(reservation.getStatus()) || "COMPLETED".equals(reservation.getStatus())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Reservation cannot be cancelled");
        }

        // 1. reservation -> CANCELLED
        reservation.setStatus("CANCELLED");
        reservationRepository.save(reservation);

        // 2. spot -> AVAILABLE again
        spotRepository.findById(reservation.getSpotId()).ifPresent(spot -> {
            spot.setCurrentStatus("AVAILABLE");
            spotRepository.save(spot);
        });

        // 3. if it was paid -> refund
        boolean refunded = false;
        for (Payment p : paymentRepository.findByReservationId(reservationId)) {
            if ("COMPLETED".equals(p.getPaymentStatus())) {
                p.setPaymentStatus("REFUNDED");
                paymentRepository.save(p);
                refunded = true;
            }
        }

        // 4. notify the driver
        notificationService.send(reservation.getDriverId(), reservationId, "Reservation cancelled",
                refunded ? "Your reservation was cancelled and the payment will be refunded."
                         : "Your reservation was cancelled.",
                "CANCELLATION");

        return reservation;
    }

    public Reservation getById(UUID reservationId) {
        return reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reservation not found"));
    }

    public List<Reservation> getForDriver(UUID driverId) {
        return reservationRepository.findByDriverId(driverId);
    }

    /** Use case 5, trigger B: how many minutes are left on the reservation */
    public long minutesLeft(UUID reservationId) {
        Reservation reservation = getById(reservationId);
        long minutes = Duration.between(OffsetDateTime.now(), reservation.getEndTime()).toMinutes();
        return Math.max(minutes, 0);
    }

    public void save(Reservation reservation) {
        reservationRepository.save(reservation);
    }
}
