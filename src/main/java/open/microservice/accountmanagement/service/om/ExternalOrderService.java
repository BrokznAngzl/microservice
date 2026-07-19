package open.microservice.accountmanagement.service.om;

import open.microservice.accountmanagement.model.hibernate.om.ExternalOrder;
import open.microservice.accountmanagement.util.DateUtil;
import open.microservice.accountmanagement.util.StringUtil;
import org.springframework.stereotype.Service;

import static open.microservice.accountmanagement.constant.StatusConstant.PENDING;

@Service
public class ExternalOrderService {

    public void initExternalOrder(ExternalOrder exOrder, String endpoint, String requestInfo, String status) {
        exOrder.setCreatedDate(DateUtil.getCurrentLocalDateTime());
        exOrder.setCreatedBy("SOOD LORE");
        exOrder.setEndpoint(endpoint);
        exOrder.setRequestInfo(requestInfo);
        exOrder.setStatus(StringUtil.defaultIfEmpty(status, PENDING));
    }
}
