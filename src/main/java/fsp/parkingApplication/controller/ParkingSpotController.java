package fsp.parkingApplication.controller;

import fsp.parkingApplication.dto.staff.IllegalParkingReportDTO;
import fsp.parkingApplication.service.IParkingService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/parking-spots")
public class ParkingSpotController {

    private final IParkingService parkingService;

    public ParkingSpotController(IParkingService parkingService) {
        this.parkingService = parkingService;
    }

    @GetMapping("/{parkingSpot}")
    public String getLicensePlate(@PathVariable("parkingSpot") String parkingSpot) {
        return parkingService.getLicensePlate(parkingSpot);
    }

    @PostMapping("/report")
    public void reportParking(@RequestBody IllegalParkingReportDTO report) {
        parkingService.reportParking(report);
    }
}