package driverservice.dto;

import driverservice.model.Reservation;
import driverservice.model.User;
import driverservice.model.Vehicle;

import java.util.List;

/**
 * What the app gets after register / login: the driver, his vehicles
 * and his active reservations (the "dashboard" data).
 */
public record DriverProfileResponse(
        String message,
        User driver,
        List<Vehicle> vehicles,
        List<Reservation> activeReservations
) {
}
