package driverservice.service;

import driverservice.model.Notification;
import driverservice.model.Reservation;
import driverservice.repository.NotificationRepository;
import driverservice.repository.ReservationRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Use case 5: Notify driver about reservation.
 * Notifications are saved in the "notifications" table, the app reads them.
 * Types (DB enum): RESERVATION_CONFIRMATION, EXPIRATION_WARNING, CANCELLATION
 */
@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final ReservationRepository reservationRepository;

    public NotificationService(NotificationRepository notificationRepository,
                               ReservationRepository reservationRepository) {
        this.notificationRepository = notificationRepository;
        this.reservationRepository = reservationRepository;
    }

    public Notification send(UUID driverId, UUID reservationId, String title, String message, String type) {
        return notificationRepository.save(new Notification(driverId, reservationId, title, message, type));
    }

    public List<Notification> getForDriver(UUID driverId) {
        return notificationRepository.findByDriverIdOrderByCreatedAtDesc(driverId);
    }

    /**
     * Trigger B: runs every minute. If a reservation ends in less than 15 minutes,
     * the driver gets one "time left" notification.
     */
    @Scheduled(fixedRate = 60000)
    public void checkExpiringReservations() {
        OffsetDateTime now = OffsetDateTime.now();
        for (Reservation r : reservationRepository.findByStatusIn(List.of("CONFIRMED", "ACTIVE"))) {
            long minutesLeft = Duration.between(now, r.getEndTime()).toMinutes();
            if (minutesLeft > 0 && minutesLeft <= 15
                    && !notificationRepository.existsByReservationIdAndType(r.getId(), "EXPIRATION_WARNING")) {
                send(r.getDriverId(), r.getId(), "Parking time ending",
                        "You have " + minutesLeft + " minutes left on your parking spot.", "EXPIRATION_WARNING");
            }
        }
    }
}
