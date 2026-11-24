package driverservice.repository;

import driverservice.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {

    List<Notification> findByDriverIdOrderByCreatedAtDesc(UUID driverId);

    boolean existsByReservationIdAndType(UUID reservationId, String type);
}
