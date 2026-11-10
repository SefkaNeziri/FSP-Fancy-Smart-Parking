package fsp.parkingApplication.service;

import fsp.parkingApplication.dto.staff.IllegalParkingReportDTO;
import org.springframework.stereotype.Service;

@Service
public class ParkingService implements IParkingService {

    @Override
    public String getLicensePlate(String parkingSpot) {
        return "";
    }

    @Override
    public void reportParking(IllegalParkingReportDTO report) {

    }
}