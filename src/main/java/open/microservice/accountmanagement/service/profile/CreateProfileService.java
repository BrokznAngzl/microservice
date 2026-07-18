package open.microservice.accountmanagement.service.profile;


import lombok.extern.log4j.Log4j2;
import open.microservice.accountmanagement.model.dto.AddressDto;
import open.microservice.accountmanagement.model.hibernate.OrderPropertyInformation;
import open.microservice.accountmanagement.model.hibernate.om.ExternalOrder;
import open.microservice.accountmanagement.model.hibernate.om.Order;
import open.microservice.accountmanagement.model.hibernate.pf.Account;
import open.microservice.accountmanagement.model.hibernate.pf.Address;
import open.microservice.accountmanagement.model.request.internal.AccountRequest;
import open.microservice.accountmanagement.repository.profile.AddressRepository;
import open.microservice.accountmanagement.service.IComposer;
import open.microservice.accountmanagement.util.DateUtil;
import open.microservice.accountmanagement.util.ObjectUtil;
import open.microservice.accountmanagement.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.util.Date;
import java.util.UUID;

import static open.microservice.accountmanagement.constant.ComposeKeyConstant.CREATE_PROFILE;
import static open.microservice.accountmanagement.constant.StatusConstant.PENDING;


@Service
@Log4j2
public class CreateProfileService implements IComposer {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private AddressRepository addressRepository;

    @Override
    public boolean canCompose(String value) {
        return StringUtil.equals(value, CREATE_PROFILE);
    }

    @Override
    public void compose(OrderPropertyInformation orderProperty, ExternalOrder externalOrder, Order order) {
        AccountRequest request = objectMapper.readValue(order.getRequest(), AccountRequest.class);
        Date currentDateTime = DateUtil.getCurrentLocalDateTime();
        Account account = new Account();
        account.setId(UUID.randomUUID().toString());
        account.setAccountNo(genAccountNo());
        account.setAccountName(request.getAccountName());
        account.setCreateBy("SOOD LORE");
        account.setLastUpdateBy("SOOD LORE");
        account.setCreateDate(currentDateTime);
        account.setLastUpdatedDate(currentDateTime);

        if (ObjectUtil.isNotEmpty(request.getAddress())) {
            account.setAddress(composeAddress(request));
        }

        orderProperty.getOrderItem().setAccount(ObjectUtil.getDto(account));
        String profileRequestParam = objectMapper.writeValueAsString(account);
        if (log.isDebugEnabled()) log.debug("Profile Request Param: {}", profileRequestParam);
        externalOrder.setRequestInfo(profileRequestParam);
        externalOrder.setStatus(PENDING);
        // no endpoint for this task, save with jpa
        // externalOrder.setEndpoint();
    }

    private String genAccountNo() {
        // Logic to generate  account number
        return "mock_for_test_account_no";
    }

    private Address composeAddress(AccountRequest request) {
        AddressDto addressReq = request.getAddress();
        Address existAddress = addressRepository.findByAddressDetailAndProvince(addressReq.getDetail(), addressReq.getProvince());

        if (ObjectUtil.isNotEmpty(existAddress)) {
            return existAddress;
        }

        Address newAddress = new Address();
        newAddress.setId(UUID.randomUUID().toString());
        newAddress.setAddressDetail(addressReq.getDetail());
        newAddress.setProvince(addressReq.getProvince());
        return newAddress;
    }
}
