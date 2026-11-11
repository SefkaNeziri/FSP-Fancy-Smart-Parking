package driverservice.controller;

import driverservice.dto.FacilityResponse;
import driverservice.model.ParkingSpot;
import driverservice.service.ParkingService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Use case 1: Search for parking facility and spot.
 */
@RestController
@RequestMapping("/api/facilities")
public class ParkingController {

    private final ParkingService parkingService;

    public ParkingController(ParkingService parkingService) {
        this.parkingService = parkingService;
    }

    // GET /api/facilities?lat=55.86&lng=9.85  -> nearest first
    @GetMapping
    public List<FacilityResponse> findNearby(@RequestParam double lat, @RequestParam double lng) {
        return parkingService.findNearby(lat, lng);
    }

    // GET /api/facilities/{id}
    @GetMapping("/{id}")
    public FacilityResponse getFacility(@PathVariable UUID id) {
        return parkingService.getFacility(id);
    }

    // GET /api/facilities/{id}/spots?status=AVAILABLE
    @GetMapping("/{id}/spots")
    public List<ParkingSpot> getSpots(@PathVariable UUID id, @RequestParam(required = false) String status) {
        return parkingService.getSpots(id, status);
    }
}
