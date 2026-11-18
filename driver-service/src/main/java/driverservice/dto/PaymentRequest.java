package driverservice.dto;

import java.util.UUID;

/**
 * Data sent when the driver pays (use case 4).
 * paymentMethod: e.g. "CARD", "MOBILEPAY"
 * cardNumber: fake payment – must have 16 digits, otherwise payment FAILS
 */
public record PaymentRequest(
        UUID reservationId,
        String paymentMethod,
        String cardNumber
) {
}
