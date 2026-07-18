package open.microservice.accountmanagement.util;


import open.microservice.accountmanagement.model.exception.ResourceNotFoundException;
import open.microservice.accountmanagement.model.hibernate.om.ErrorMessage;
import open.microservice.accountmanagement.model.hibernate.om.External;
import open.microservice.accountmanagement.model.hibernate.om.ExternalParam;
import open.microservice.accountmanagement.repository.om.ErrorMessageRepository;
import open.microservice.accountmanagement.repository.om.ExternalParamRepository;
import open.microservice.accountmanagement.repository.om.ExternalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static open.microservice.accountmanagement.constant.ErrorConstant.RESOURCE_NOT_FOUND;
import static open.microservice.accountmanagement.constant.ErrorConstant.RESOURCE_NOT_FOUND_DETAIL;


@Component
public class CacheUtil {
    @Autowired
    private ExternalRepository externalRepository;
    @Autowired
    private ErrorMessageRepository errorMessageRepository;
    @Autowired
    private ExternalParamRepository externalParamRepository;

    private HashMap<String, List<External>> externals = new HashMap<>();
    private HashMap<String, List<ExternalParam>> externalParams = new HashMap<>();
    private HashMap<String, ErrorMessage> errorMessages = new HashMap<>();

    public ErrorMessage getErrorMessage(String id) {
        return getObject(errorMessages, id, "Error Message");
    }

    public List<External> getExternal(String key) {
        return getObject(externals, key, "External");
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
            throw new ResourceNotFoundException(RESOURCE_NOT_FOUND, StringUtil.format(RESOURCE_NOT_FOUND_DETAIL, "External Param For External", externalId));
        }

        return exParams;
    }

    private <T> T getObject(Map<String, T> map, String id, String entityType) {
        T value = map.get(id);
        if (ObjectUtil.isEmpty(value)) {
            throw new ResourceNotFoundException(RESOURCE_NOT_FOUND, StringUtil.format(RESOURCE_NOT_FOUND_DETAIL, entityType, id));
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
                    this.externals.put(key, new ArrayList<>(Arrays.asList(external)));
                }
            }
        }

        if (ObjectUtil.isNotEmpty(errorMsgs)) {
            this.errorMessages = (HashMap<String, ErrorMessage>) errorMsgs.stream()
                    .collect(Collectors.toMap(ErrorMessage::getId, Function.identity()));
        }

    }
}
