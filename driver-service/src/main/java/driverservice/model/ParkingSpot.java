package driverservice.model;

import jakarta.persistence.*;

import java.util.UUID;

/**
 * Table: parking_spots  (one place inside a facility)
 * currentStatus: AVAILABLE / RESERVED / ... (PostgreSQL enum spot_status)
 */
@Entity
@Table(name = "parking_spots")
public class ParkingSpot {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "facility_id")
    private UUID facilityId;

    @Column(name = "spot_number")
    private String spotNumber;

    @Column(name = "current_status")
    private String currentStatus = "AVAILABLE";

    public ParkingSpot() {
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getFacilityId() { return facilityId; }
    public void setFacilityId(UUID facilityId) { this.facilityId = facilityId; }

    public String getSpotNumber() { return spotNumber; }
    public void setSpotNumber(String spotNumber) { this.spotNumber = spotNumber; }

    public String getCurrentStatus() { return currentStatus; }
    public void setCurrentStatus(String currentStatus) { this.currentStatus = currentStatus; }
}
