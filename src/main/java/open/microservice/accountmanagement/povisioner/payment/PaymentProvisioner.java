package open.microservice.accountmanagement.povisioner.payment;


import lombok.extern.log4j.Log4j2;
import open.microservice.accountmanagement.http.PaymentHttpClient;
import open.microservice.accountmanagement.model.exception.ProvisioningFailedException;
import open.microservice.accountmanagement.model.hibernate.om.ExternalOrder;
import open.microservice.accountmanagement.povisioner.IProvisioner;
import open.microservice.accountmanagement.repository.om.ExternalOrderRepository;
import open.microservice.accountmanagement.util.DateUtil;
import open.microservice.accountmanagement.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static open.microservice.accountmanagement.constant.ExternalNodeConstant.PMD;
import static open.microservice.accountmanagement.constant.StatusConstant.COMPLETED;
import static open.microservice.accountmanagement.constant.StatusConstant.FAILED;

@Log4j2
@Service
public class PaymentProvisioner implements IProvisioner {
    @Autowired
    private PaymentHttpClient paymentHttpClient;
    @Autowired
    private ExternalOrderRepository externalOrderRepository;

    @Override
    public boolean canProvisioning(String value) {
        return StringUtil.equals(value, PMD);
    }

    @Override
    public void provisioning(ExternalOrder externalOrder) {
        try {
            externalOrder.setRequestDate(DateUtil.getCurrentLocalDateTime());

            String responseBody = paymentHttpClient.callPayment(externalOrder.getEndpoint(), externalOrder.getRequestInfo());

            externalOrder.setResponseInfo(responseBody);
            externalOrder.setResponseDate(DateUtil.getCurrentLocalDateTime());
            externalOrder.setStatus(COMPLETED);

        } catch (ProvisioningFailedException e) {
            log.error("error provisioning product order: {}", e.getMessage());
            externalOrder.setStatus(FAILED);
            externalOrder.setResponseInfo(e.getMessage());
            externalOrder.setResponseDate(DateUtil.getCurrentLocalDateTime());
            throw e;

        } finally {
            externalOrder.setLastUpdatedBy("SOOD LORE");
            externalOrder.setLastUpdatedDate(DateUtil.getCurrentLocalDateTime());

            externalOrderRepository.save(externalOrder);
        }

    }
}
