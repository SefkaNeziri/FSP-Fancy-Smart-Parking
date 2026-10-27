package driverservice.repository;

import driverservice.model.ParkingFacility;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ParkingFacilityRepository extends JpaRepository<ParkingFacility, UUID> {
}
