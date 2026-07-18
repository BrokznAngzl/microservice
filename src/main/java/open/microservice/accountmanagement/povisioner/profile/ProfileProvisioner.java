package open.microservice.accountmanagement.povisioner.profile;


import lombok.extern.log4j.Log4j2;
import open.microservice.accountmanagement.model.hibernate.om.ExternalOrder;
import open.microservice.accountmanagement.povisioner.IProvisioner;
import open.microservice.accountmanagement.service.profile.ProfileService;
import open.microservice.accountmanagement.util.DateUtil;
import open.microservice.accountmanagement.util.ObjectUtil;
import open.microservice.accountmanagement.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

import static open.microservice.accountmanagement.constant.ExternalNodeConstant.PF;
import static open.microservice.accountmanagement.constant.StatusConstant.COMPLETED;
import static open.microservice.accountmanagement.constant.StatusConstant.FAILED;

@Log4j2
@Service
public class ProfileProvisioner implements IProvisioner {

    @Autowired
    private ProfileService profileService;

    @Override
    public boolean canProvisioning(String value) {
        return StringUtil.equals(value, PF);
    }

    @Override
    public void provisioning(ExternalOrder externalOrder) {
        try {
            externalOrder.setRequestDate(DateUtil.getCurrentLocalDateTime());

            if (ObjectUtil.isNotEmpty(externalOrder.getRequestInfo())) {
                profileService.createNewProfile(externalOrder.getRequestInfo());
            }

            externalOrder.setStatus(COMPLETED);

        } catch (Exception e) {
            log.error("Error provisioning profile: {}", e.getMessage(), e);
            externalOrder.setStatus(FAILED);
            externalOrder.setRequestInfo(e.getMessage());
            throw e;

        } finally {
            externalOrder.setLastUpdatedBy("sood lor");
            externalOrder.setLastUpdatedDate(DateUtil.getCurrentLocalDateTime());
        }

    }

    @Override
    public void resend(ExternalOrder externalOrder) {
//        provisioning again
    }
}
