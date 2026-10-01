package open.microservice.accountmanagement.service.pmd;

import open.microservice.accountmanagement.model.request.pmd.PaymentEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static open.microservice.accountmanagement.constant.StatusConstant.COMPLETED;

@Service
public class PaymentService {

    @Autowired(required = false)
    private PaymentProducer paymentProducer;

    public void completePayment(String paymentId, String productId, String amount) {
        PaymentEvent event = new PaymentEvent(
                paymentId,
                Long.parseLong(productId),
                new BigDecimal(amount),
                COMPLETED
        );

        paymentProducer.sendPaymentCompleted(event);
    }
}
