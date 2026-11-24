package driverservice.controller;

import driverservice.dto.PaymentRequest;
import driverservice.dto.ReceiptResponse;
import driverservice.model.Payment;
import driverservice.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Use case 4: Pay for reservation + receipt.
 */
@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // GET /api/payments/methods
    @GetMapping("/methods")
    public List<String> getMethods() {
        return paymentService.getPaymentMethods();
    }

    // POST /api/payments
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Payment pay(@RequestBody PaymentRequest request) {
        return paymentService.pay(request);
    }

    // GET /api/payments/{id}/receipt
    @GetMapping("/{id}/receipt")
    public ReceiptResponse getReceipt(@PathVariable UUID id) {
        return paymentService.getReceipt(id);
    }
}
