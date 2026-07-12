package open.microservice.accountmanagement.util;


import open.microservice.accountmanagement.model.om.ErrorModel;
import open.microservice.accountmanagement.model.request.Phone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static open.microservice.accountmanagement.constant.ParameterConstance.*;


@Component
public class ValidateUtil {
    @Autowired
    private ExceptionUtil exceptionUtil;

    private static final String ER404 = "er404";
    private static final String ER400 = "er400";

    public void validateMandatory(String value, List<ErrorModel> errorList, String... fieldName) {
        if (StringUtil.isEmpty(value)) {
            addErrorNotFound(errorList, fieldName);
        }
    }

    public void addErrorNotFound(List<ErrorModel> errorList, String... parameterName) {
        exceptionUtil.addErrMsg(ER404, errorList, parameterName);
    }

    public void addErrorInvalid(List<ErrorModel> errorList, String... parameterName) {
        exceptionUtil.addErrMsg(ER400, errorList, parameterName);
    }

    public boolean isValidMobileNumber(String phoneNumber) {
        return phoneNumber.matches("^(06|08|09)\\d{8}$");
    }

    public boolean isValidHomeNumber(String phoneNumber) {
        return phoneNumber.matches("^(0[2-9])\\d{7}$");
    }

    public void validatePhone(Phone phone, List<ErrorModel> errorList) {
        if (StringUtil.equals(phone.getPhoneType(), MOBLE)) {
            if (!isValidMobileNumber(phone.getPhoneNumber())) {
                addErrorInvalid(errorList, PHONE_NUMBER);
            }
        } else if (StringUtil.equals(phone.getPhoneType(), HOME)) {
            if (!isValidHomeNumber(phone.getPhoneNumber())) {
                addErrorInvalid(errorList, PHONE_NUMBER);
            }
        }
    }
}
