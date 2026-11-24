package driverservice.service;

import driverservice.dto.FacilityResponse;
import driverservice.model.ParkingFacility;
import driverservice.model.ParkingSpot;
import driverservice.repository.ParkingFacilityRepository;
import driverservice.repository.ParkingSpotRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

/**
 * Use case 1: Search for parking facility and spot.
 */
@Service
public class ParkingService {

    private final ParkingFacilityRepository facilityRepository;
    private final ParkingSpotRepository spotRepository;

    public ParkingService(ParkingFacilityRepository facilityRepository, ParkingSpotRepository spotRepository) {
        this.facilityRepository = facilityRepository;
        this.spotRepository = spotRepository;
    }

    /**
     * Returns facilities sorted by distance from (lat, lng).
     * The app sends GPS coordinates (trigger A) or coordinates of the destination (trigger B).
     */
    public List<FacilityResponse> findNearby(double lat, double lng) {
        List<FacilityResponse> result = new ArrayList<>();
        for (ParkingFacility f : facilityRepository.findAll()) {
            result.add(toResponse(f, distanceKm(lat, lng, f.getLatitude(), f.getLongitude())));
        }
        result.sort(Comparator.comparingDouble(FacilityResponse::distanceKm));
        return result;
    }

    public FacilityResponse getFacility(UUID facilityId) {
        return toResponse(getFacilityEntity(facilityId), 0);
    }

    public ParkingFacility getFacilityEntity(UUID facilityId) {
        return facilityRepository.findById(facilityId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Facility not found"));
    }

    /** status is optional, e.g. "AVAILABLE" */
    public List<ParkingSpot> getSpots(UUID facilityId, String status) {
        if (status == null) {
            return spotRepository.findByFacilityId(facilityId);
        }
        return spotRepository.findByFacilityIdAndCurrentStatus(facilityId, status);
    }

    private FacilityResponse toResponse(ParkingFacility f, double distance) {
        return new FacilityResponse(
                f.getId(), f.getName(), f.getAddress(), f.getLatitude(), f.getLongitude(),
                Math.round(distance * 100) / 100.0,
                f.getHourlyRate(),
                spotRepository.countByFacilityId(f.getId()),
                spotRepository.countByFacilityIdAndCurrentStatus(f.getId(), "AVAILABLE"));
    }

    /** Haversine formula – distance between two GPS points in km */
    private double distanceKm(double lat1, double lng1, double lat2, double lng2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLng / 2) * Math.sin(dLng / 2);
        return 6371 * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }
}
