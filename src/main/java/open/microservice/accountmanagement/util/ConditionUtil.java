package open.microservice.accountmanagement.util;


import lombok.extern.log4j.Log4j2;
import open.microservice.accountmanagement.model.hibernate.om.Condition;

import static open.microservice.accountmanagement.constant.OperatorConstant.EQUALS;
import static open.microservice.accountmanagement.constant.OperatorConstant.NOT_EQUALS;
import static open.microservice.accountmanagement.constant.SourceTypeConstant.*;


@Log4j2
public class ConditionUtil {
    private ParameterUtil parameterUtil;

    public ConditionUtil(String jsonModel) {
        this.parameterUtil = new ParameterUtil(jsonModel);
    }

    public boolean activeCondition(Condition condition) {
        try {
            String source = getValue(condition);
            String operator = condition.getOperator().getOperatorName();
            String targetValue = condition.getLookupValue();

            return switch (operator) {
                case EQUALS -> StringUtil.equals(source, targetValue);
                case NOT_EQUALS -> !StringUtil.equals(source, targetValue);
                default -> {
                    log.warn("Unknown condition source: {}", condition.getId());
                    yield false;
                }
            };
        } catch (Exception e) {
            log.error("Error evaluating condition: {}", condition.getId(), e);
            return false;
        }
    }

    private String getValue(Condition condition) {
        String type = condition.getSourceType();
        String value = condition.getSource();

        switch (type) {
            case STATIC:
                return value;
            case JSONPATH:
                return parameterUtil.getJsonValue(value);
            case METHOD:
                return parameterUtil.getMethodValue(value);
            default:
                return null;
        }
    }
}
