package open.microservice.accountmanagement.util;


import jakarta.persistence.EntityNotFoundException;
import open.microservice.accountmanagement.model.hibernate.om.ErrorMessage;
import open.microservice.accountmanagement.model.hibernate.om.External;
import open.microservice.accountmanagement.model.hibernate.om.ExternalParam;
import open.microservice.accountmanagement.repository.om.ErrorMessageRepository;
import open.microservice.accountmanagement.repository.om.ExternalParamRepository;
import open.microservice.accountmanagement.repository.om.ExternalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


@Component
public class CacheUtil {
    @Autowired
    private ExternalRepository externalRepository;
    @Autowired
    private ErrorMessageRepository errorMessageRepository;

    private HashMap<String, List<External>> externals = new HashMap<>();
    private HashMap<String, List<ExternalParam>> externalParams = new HashMap<>();
    private HashMap<String, ErrorMessage> errorMessages = new HashMap<>();
    @Autowired
    private ExternalParamRepository externalParamRepository;

    public ErrorMessage getErrorMessage(String id) {
        return getObject(errorMessages, id, "Error message not found for id: ");
    }

    public List<External> getExternal(String key) {
        return getObject(externals, key, "External not found for id: ");
    }

    public List<ExternalParam> getExternalParam(String externalId) {
        return fetchExternalParam(externalId);
    }

    private List<ExternalParam> fetchExternalParam(String externalId) {
        List<ExternalParam> exParams = externalParams.get(externalId);

        if (ObjectUtil.isEmpty(exParams)) {
            exParams = externalParamRepository.findByExternalId(externalId);

            if (ObjectUtil.isNotEmpty(exParams)) {
                externalParams.put(externalId, exParams);
            }
        }

        if (ObjectUtil.isEmpty(exParams)) {
            throw new EntityNotFoundException("ExternalParam not found for externalId: " + externalId);
        }

        return exParams;
    }

    private <T> T getObject(Map<String, T> map, String id, String notFoundMessage) {
        T value = map.get(id);
        if (ObjectUtil.isEmpty(value)) {
            throw new EntityNotFoundException(notFoundMessage + id);
        }
        return value;
    }

    public void loadOperatorManagementCache() {
        List<External> externals = externalRepository.findAll();
        List<ErrorMessage> errorMsgs = errorMessageRepository.findAll();

        if (ObjectUtil.isNotEmpty(externals)) {
            for (External external : externals) {
                String key = external.getAction() + "|" + external.getApi();
                if (this.externals.containsKey(key)) {
                    this.externals.get(key).add(external);
                } else {
                    this.externals.put(key, List.of(external));
                }
            }
        }

        if (ObjectUtil.isNotEmpty(errorMsgs)) {
            this.errorMessages = (HashMap<String, ErrorMessage>) errorMsgs.stream()
                    .collect(Collectors.toMap(ErrorMessage::getId, Function.identity()));
        }

    }

    /*
    public static List<External> getMockUpExternal() {
        External pfExternal = new External();
        ExternalNode pfNode = new ExternalNode();
        pfNode.setName("profile");
        pfExternal.setExternalNode(pfNode);
        pfExternal.setOrderName("create profile");
        Condition pfCondition = new Condition();
        pfCondition.setId(1);
        pfCondition.setSource("$.activity");
        pfCondition.setTargetValue("active");
        Operator pfOperator = new Operator();
        pfOperator.setOperatorName(EQUALS);
        SourcType pfSourceType = new SourcType();
        pfSourceType.setType(JSONPATH);
        pfCondition.setOperator(pfOperator);
        pfCondition.setSourcType(pfSourceType);
        pfExternal.setCondition(pfCondition);

        External poExternal = new External();
        ExternalNode poNode = new ExternalNode();
        poNode.setName("product order");
        poExternal.setExternalNode(poNode);
        poExternal.setOrderName("add on top");
        Condition poCondition = new Condition();
        poCondition.setId(2);
        poCondition.setSource("$.address");
//        poCondition.setTargetValue(null);
        Operator poOperator = new Operator();
        poOperator.setOperatorName(NOT_EQUALS);
        SourcType poSourceType = new SourcType();
        poSourceType.setType(JSONPATH);
        poCondition.setOperator(poOperator);
        poCondition.setSourcType(poSourceType);
        poCondition.setOperator(poOperator);
        poExternal.setCondition(poCondition);

        List<External> externals = new ArrayList<>();
        externals.add(pfExternal);
        externals.add(poExternal);

        return externals;
    }
     */
}
