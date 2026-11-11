
package fsp.parkingApplication.service;

import fsp.parkingApplication.dto.staff.IllegalParkingReportDTO;
import org.springframework.stereotype.Service;

@Service
public class ParkingService implements IParkingService {

    @Override
    public String getLicensePlate(String parkingSpot) {
        // TODO: Temporary test data until the database is implemented
        switch (parkingSpot.toUpperCase()) {
            case "A1":
                return "AB-11223";
            case "B2":
                return "XY-55667";
            case "C3":
                return null;
            default:
                return null;
        }
    }

    // TODO: Implement when the database is available
    @Override
    public void reportParking(IllegalParkingReportDTO report) {
        return;
    }
}
