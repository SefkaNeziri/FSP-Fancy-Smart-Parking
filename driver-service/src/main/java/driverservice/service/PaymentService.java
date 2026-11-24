package driverservice.service;

import driverservice.dto.PaymentRequest;
import driverservice.dto.ReceiptResponse;
import driverservice.model.*;
import driverservice.repository.ParkingSpotRepository;
import driverservice.repository.PaymentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Use case 4: Pay for reservation (+ receipt).
 * Payment is simulated (no real bank): a 16-digit card number = success.
 */
@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final ReservationService reservationService;
    private final ParkingSpotRepository spotRepository;
    private final ParkingService parkingService;
    private final DriverService driverService;

    public PaymentService(PaymentRepository paymentRepository,
                          ReservationService reservationService,
                          ParkingSpotRepository spotRepository,
                          ParkingService parkingService,
                          DriverService driverService) {
        this.paymentRepository = paymentRepository;
        this.reservationService = reservationService;
        this.spotRepository = spotRepository;
        this.parkingService = parkingService;
        this.driverService = driverService;
    }

    public List<String> getPaymentMethods() {
        return List.of("CARD", "MOBILEPAY", "APPLE_PAY");
    }

    public Payment pay(PaymentRequest request) {
        Reservation reservation = reservationService.getById(request.reservationId());
        if (!"PENDING_PAYMENT".equals(reservation.getStatus())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Reservation cannot be paid (status: " + reservation.getStatus() + ")");
        }

        Payment payment = new Payment();
        payment.setReservationId(reservation.getId());
        payment.setAmount(reservation.getTotalPrice());
        payment.setPaymentMethod(request.paymentMethod());

        // "process" the payment
        boolean success = request.cardNumber() != null && request.cardNumber().matches("\\d{16}");

        if (!success) {
            // alternative flow: payment failed -> driver can try again
            payment.setPaymentStatus("FAILED");
            paymentRepository.save(payment);
            throw new ResponseStatusException(HttpStatus.PAYMENT_REQUIRED, "Payment failed, please try again");
        }

        payment.setPaymentStatus("COMPLETED");
        payment.setTransactionId("TX-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        payment.setPaidAt(OffsetDateTime.now());
        payment = paymentRepository.save(payment);

        // mark reservation as paid
        reservation.setStatus("CONFIRMED");
        reservationService.save(reservation);

        return payment;
    }

    public ReceiptResponse getReceipt(UUID paymentId) {
        Payment p = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment not found"));
        Reservation r = reservationService.getById(p.getReservationId());
        ParkingSpot spot = spotRepository.findById(r.getSpotId()).orElseThrow();
        ParkingFacility facility = parkingService.getFacilityEntity(spot.getFacilityId());
        User driver = driverService.getDriver(r.getDriverId());

        return new ReceiptResponse(p.getId(), p.getTransactionId(),
                driver.getFirstName() + " " + driver.getLastName(),
                facility.getName(), spot.getSpotNumber(),
                r.getStartTime(), r.getEndTime(),
                p.getAmount(), p.getPaymentMethod(), p.getPaymentStatus(), p.getPaidAt());
    }
}
