package driverservice.dto;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * One row in the search result list (use case 1):
 * name, distance, tariff, total vs. available spots.
 */
public record FacilityResponse(
        UUID id,
        String name,
        String address,
        double latitude,
        double longitude,
        double distanceKm,
        BigDecimal hourlyRate,
        long totalSpots,
        long availableSpots
) {
}
