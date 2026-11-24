package driverservice.repository;

import driverservice.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ReservationRepository extends JpaRepository<Reservation, UUID> {

    List<Reservation> findByDriverId(UUID driverId);

    // e.g. statuses = [PENDING_PAYMENT, CONFIRMED, ACTIVE] -> "active" reservations
    List<Reservation> findByDriverIdAndStatusIn(UUID driverId, List<String> statuses);

    List<Reservation> findByStatusIn(List<String> statuses);
}
