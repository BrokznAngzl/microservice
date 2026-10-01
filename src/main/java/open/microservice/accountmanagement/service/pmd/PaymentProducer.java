package open.microservice.accountmanagement.service.pmd;


import open.microservice.accountmanagement.model.request.pmd.PaymentEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import static open.microservice.accountmanagement.constant.TopiConstant.PAYMENT;

@Service
public class PaymentProducer {

    @Autowired
    private KafkaTemplate<String, PaymentEvent> kafkaTemplate;

    public void sendPaymentCompleted(PaymentEvent event) {
        kafkaTemplate.send(PAYMENT, event.paymentId(), event);
    }
}