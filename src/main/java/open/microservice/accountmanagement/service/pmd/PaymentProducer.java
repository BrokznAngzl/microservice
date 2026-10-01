package open.microservice.accountmanagement.service.pmd;


import open.microservice.accountmanagement.model.request.pmd.PaymentEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import static open.microservice.accountmanagement.constant.TopiConstant.PAYMENT;

@Service
@ConditionalOnProperty(
        name = "spring.kafka.enabled",
        havingValue = "true",
        matchIfMissing = false
)
public class PaymentProducer {

    @Autowired
    private KafkaTemplate<String, PaymentEvent> kafkaTemplate;

    public void sendPaymentCompleted(PaymentEvent event) {
        kafkaTemplate.send(PAYMENT, event.paymentId(), event);
    }
}