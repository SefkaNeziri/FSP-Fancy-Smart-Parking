package fsp.parkingApplication.service;

import fsp.parkingApplication.dto.staff.IllegalParkingReportDTO;

public interface IParkingService {

    // Driver reserve parking

    // Driver buy parking

    // Driver cancel parking

    // Staff get expected license plate for a parking spot
    String getLicensePlate(String parkingSpot);

    // Staff report illegal parking
    void reportParking(IllegalParkingReportDTO report);
}
