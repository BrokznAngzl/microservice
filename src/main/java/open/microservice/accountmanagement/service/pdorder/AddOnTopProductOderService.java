package open.microservice.accountmanagement.service.pdorder;

import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import open.microservice.accountmanagement.configuration.property.AppConfig;
import open.microservice.accountmanagement.model.Parameter;
import open.microservice.accountmanagement.model.hibernate.OrderPropertyInformation;
import open.microservice.accountmanagement.model.hibernate.om.Condition;
import open.microservice.accountmanagement.model.hibernate.om.ExternalOrder;
import open.microservice.accountmanagement.model.hibernate.om.ExternalParam;
import open.microservice.accountmanagement.model.hibernate.om.Order;
import open.microservice.accountmanagement.service.IComposer;
import open.microservice.accountmanagement.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

import static open.microservice.accountmanagement.constant.ComposeKeyConstant.ADD_ON_TOP;
import static open.microservice.accountmanagement.constant.StatusConstant.PENDING;


@Slf4j
@Service
public class AddOnTopProductOderService implements IComposer {
    @Autowired
    private AppConfig appConfig;
    @Autowired
    ObjectMapper mapper;
    @Autowired
    private CacheUtil cacheUtil;

    @Override
    public boolean canCompose(String value) {
        return StringUtil.equals(value, ADD_ON_TOP);
    }

    @Override
    public void compose(OrderPropertyInformation orderProperty, ExternalOrder externalOrder, Order order) {
        log.info("Composing on top order");
        AppConfig.Pod node = appConfig.getPod();
        String host = node.getHost();
        String uri = node.getUri();

        externalOrder.setEndpoint(host + uri);
        externalOrder.setStatus(PENDING);

        List<ExternalParam> externalParams = cacheUtil.getExternalParam(externalOrder.getExternalId());
        String jsonModel = mapper.writeValueAsString(orderProperty);
        ParameterUtil parameterUtil = new ParameterUtil(jsonModel);
        ConditionUtil conditionUtil = new ConditionUtil(jsonModel);
        List<Parameter> parameters = new ArrayList<>();
        JsonObject requestBody = new JsonObject();

        for (ExternalParam exParam : externalParams) {
            boolean isActive = true;

            Condition condition = exParam.getCondition();
            if (ObjectUtil.isNotEmpty(condition)) {
                isActive = conditionUtil.activeCondition(condition);
            }

            if (!isActive) {
                continue;
            }

            log.info("composing parameter: {}", exParam.getParameterName());
            String paramName = exParam.getParameterName();
            String value = parameterUtil.getValue(exParam);
            parameterUtil.buildParam(parameters, paramName, value);

        }

        parameterUtil.evaluate(parameters, requestBody);
        externalOrder.setRequestInfo(requestBody.toString());
    }
}
