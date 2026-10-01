package open.microservice.accountmanagement.model.request.pmd;

import java.math.BigDecimal;

public record PaymentEvent(
        String paymentId,
        Long productId,
        BigDecimal amount,
        String status
) {
}