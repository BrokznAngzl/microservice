package open.microservice.accountmanagement.util;

import io.micrometer.common.lang.Nullable;
import open.microservice.accountmanagement.model.dto.AccountDto;
import open.microservice.accountmanagement.model.dto.AddressDto;
import open.microservice.accountmanagement.model.hibernate.pf.Account;
import open.microservice.accountmanagement.model.hibernate.pf.Address;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public class ObjectUtil {
    public static boolean isEmpty(@Nullable Object obj) {
        if (obj == null) {
            return true;
        }

        if (obj instanceof Optional<?> optional) {
            return optional.isEmpty();
        }
        if (obj instanceof CharSequence charSequence) {
            return charSequence.isEmpty();
        }
        if (obj.getClass().isArray()) {
            return Array.getLength(obj) == 0;
        }
        if (obj instanceof Collection<?> collection) {
            return collection.isEmpty();
        }
        if (obj instanceof Map<?, ?> map) {
            return map.isEmpty();
        }

        // else
        return false;
    }

    public static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }

    public static Address getEntity(AddressDto addressDto) {
        Address address = new Address();
        address.setId(addressDto.getId());
        address.setAddressDetail(addressDto.getDetail());
        address.setProvince(addressDto.getProvince());

        return address;
    }

    public static Account getEntity(AccountDto accountDto) throws ParseException {
        Account account = new Account();
        account.setId(accountDto.getId());
        account.setAccountNo(accountDto.getAccountNo());
        account.setAccountName(accountDto.getAccountName());
        account.setCreateDate(DateUtil.stringToDate(accountDto.getCreateDate(), DateUtil.APP_DATE_FORMAT));

        return account;
    }

    public static AccountDto getDto(Account account) {
        AccountDto accountDto = new AccountDto();
        accountDto.setId(account.getId());
        accountDto.setAccountNo(account.getAccountNo());
        accountDto.setAccountName(account.getAccountName());
        accountDto.setCreateDate(DateUtil.dateToString(account.getCreateDate(), DateUtil.APP_DATE_FORMAT));
        accountDto.setCreateBy(account.getCreateBy());
        accountDto.setLastUpdatedDate(DateUtil.dateToString(account.getCreateDate(), DateUtil.APP_DATE_FORMAT));
        accountDto.setLastUpdateBy(account.getLastUpdateBy());
        accountDto.setAddress(ObjectUtil.getDto(account.getAddress()));
        return accountDto;
    }

    public static AddressDto getDto(Address address) {
        AddressDto addressDto = new AddressDto();
        addressDto.setId(address.getId());
        addressDto.setDetail(address.getAddressDetail());
        addressDto.setProvince(address.getProvince());
        return addressDto;
    }
}
