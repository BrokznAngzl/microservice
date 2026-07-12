package open.microservice.accountmanagement.service.profile;


import lombok.extern.log4j.Log4j2;
import open.microservice.accountmanagement.model.dto.AddressDto;
import open.microservice.accountmanagement.model.hibernate.OrderPropertyInformation;
import open.microservice.accountmanagement.model.hibernate.om.ExternalOrder;
import open.microservice.accountmanagement.model.hibernate.om.Order;
import open.microservice.accountmanagement.model.hibernate.pf.Account;
import open.microservice.accountmanagement.model.hibernate.pf.Address;
import open.microservice.accountmanagement.model.request.AccountRequest;
import open.microservice.accountmanagement.service.IComposer;
import open.microservice.accountmanagement.util.ObjectUtil;
import open.microservice.accountmanagement.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import static open.microservice.accountmanagement.constant.ComposeKeyConstant.CREAT_PROFILE;
import static open.microservice.accountmanagement.constant.StatusConstant.PENDING;


@Service
@Log4j2
public class CreateAccountService implements IComposer {
    @Autowired
    ObjectMapper objectMapper;

    @Override
    public boolean canCompose(String value) {
        return StringUtil.equals(value, CREAT_PROFILE);
    }

    @Override
    public void compose(OrderPropertyInformation orderProperty, ExternalOrder externalOrder, Order order) throws Exception {
        AccountRequest request = objectMapper.readValue(order.getRequest(), AccountRequest.class);
        Account account = new Account();
        String accountNo = genAccountNo();
        account.setAccountNo(accountNo);

        if (ObjectUtil.isNotEmpty(request.getAddress())) {
            account.setAddress(composeAddress(request, accountNo));
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

    private Address composeAddress(AccountRequest request, String accountNo) {
        Address address = new Address();
        AddressDto addressReq = request.getAddress();
        address.setHomeNumber(addressReq.getHomeNumber());
        address.setProvince(addressReq.getProvince());
        address.setAccountNo(accountNo);
        // Set other address fields as needed
        return address;
    }
}
