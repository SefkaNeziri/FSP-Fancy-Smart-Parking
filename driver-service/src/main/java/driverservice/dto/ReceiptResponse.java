package driverservice.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Receipt for a paid reservation. It is not a separate table –
 * it is built from payments + reservations + parking data.
 */
public record ReceiptResponse(
        UUID paymentId,
        String transactionId,
        String driverName,
        String facilityName,
        String spotNumber,
        OffsetDateTime startTime,
        OffsetDateTime endTime,
        BigDecimal amount,
        String paymentMethod,
        String paymentStatus,
        OffsetDateTime paidAt
) {
}
