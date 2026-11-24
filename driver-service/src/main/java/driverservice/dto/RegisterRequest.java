package driverservice.dto;

/**
 * Data sent by the app when a driver signs up (use case 3).
 * Contains account data + first vehicle.
 */
public record RegisterRequest(
        String email,
        String password,
        String firstName,
        String lastName,
        String phoneNumber,
        String licensePlate,
        String brand,
        String model
) {
}
