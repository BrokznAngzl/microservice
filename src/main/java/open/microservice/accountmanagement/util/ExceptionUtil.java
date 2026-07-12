package open.microservice.accountmanagement.util;


import open.microservice.accountmanagement.model.hibernate.om.ErrorMessage;
import open.microservice.accountmanagement.model.om.ErrorModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ExceptionUtil {
    @Autowired
    private CacheUtil cacheUtil;

    public void addErrMsg(String errorId, List<ErrorModel> errorlist, String... parameterName) {
        buildErrMsg(errorId, errorlist, parameterName);
    }

    private void buildErrMsg(String errorId, List<ErrorModel> errorlist, String... parameterName) {
        ErrorMessage errMsg = cacheUtil.getErrorMessage(errorId);
        String errId = errMsg.getId();
        String rawMsg = errMsg.getMessage();
//        String code = errMsg.getStatusCode();
        String errorMsg = StringUtil.format(rawMsg, parameterName);
        errorlist.add(ErrorModel.builder().id(errId).statement(errorMsg).build());
    }

    public ErrorMessage getMockErrorMessage() {
        ErrorMessage errMsg = new ErrorMessage();
        errMsg.setId("er404");
        errMsg.setMessage("not found {0}");
        errMsg.setStatusCode("400");
        return errMsg;
    }
}
