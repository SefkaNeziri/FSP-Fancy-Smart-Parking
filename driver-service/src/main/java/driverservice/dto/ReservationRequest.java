package driverservice.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Data sent when the driver clicks "Reserve" (use case 2).
 * Example: startTime = "2026-10-28T10:00:00+01:00", durationHours = 2
 */
public record ReservationRequest(
        UUID driverId,
        UUID spotId,
        UUID vehicleId,
        OffsetDateTime startTime,
        int durationHours
) {
}
