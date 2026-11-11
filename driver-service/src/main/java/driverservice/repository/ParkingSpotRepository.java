package driverservice.repository;

import driverservice.model.ParkingSpot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ParkingSpotRepository extends JpaRepository<ParkingSpot, UUID> {

    List<ParkingSpot> findByFacilityId(UUID facilityId);

    List<ParkingSpot> findByFacilityIdAndCurrentStatus(UUID facilityId, String currentStatus);

    long countByFacilityId(UUID facilityId);

    long countByFacilityIdAndCurrentStatus(UUID facilityId, String currentStatus);
}
