package open.microservice.accountmanagement.povisioner.productorder;


import lombok.extern.log4j.Log4j2;
import open.microservice.accountmanagement.http.ProductHttpClient;
import open.microservice.accountmanagement.model.exception.ProvisioningFailedException;
import open.microservice.accountmanagement.model.hibernate.om.ExternalOrder;
import open.microservice.accountmanagement.povisioner.IProvisioner;
import open.microservice.accountmanagement.repository.om.ExternalOrderRepository;
import open.microservice.accountmanagement.util.DateUtil;
import open.microservice.accountmanagement.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static open.microservice.accountmanagement.constant.ExternalNodeConstant.POD;
import static open.microservice.accountmanagement.constant.StatusConstant.COMPLETED;
import static open.microservice.accountmanagement.constant.StatusConstant.FAILED;

@Log4j2
@Service
public class ProductOrderProvisioner implements IProvisioner {
    @Autowired
    private ProductHttpClient productHttpClient;
    @Autowired
    private ExternalOrderRepository externalOrderRepository;

    @Override
    public boolean canProvisioning(String value) {
        return StringUtil.equals(value, POD);
    }

    @Override
    public void provisioning(ExternalOrder externalOrder) {
        try {
            externalOrder.setRequestDate(DateUtil.getCurrentLocalDateTime());

            String responseBody = productHttpClient.callProductOrder(externalOrder.getEndpoint(), externalOrder.getRequestInfo());

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
