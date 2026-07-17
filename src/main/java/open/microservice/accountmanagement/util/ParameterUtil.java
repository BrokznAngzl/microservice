package open.microservice.accountmanagement.util;


import com.jayway.jsonpath.JsonPath;
import lombok.extern.log4j.Log4j2;
import open.microservice.accountmanagement.helper.DBMethodAction;
import open.microservice.accountmanagement.helper.DBMethodHelper;
import open.microservice.accountmanagement.model.Parameter;
import com.google.gson.JsonObject;
import open.microservice.accountmanagement.model.hibernate.om.ExternalParam;

import java.util.List;

import static open.microservice.accountmanagement.constant.SourceTypeConstant.*;

@Log4j2
public class ParameterUtil {
    private final DBMethodHelper invoker;
    private final String jsonModel;

    public ParameterUtil(String jsonModel) {
        DBMethodAction action = new DBMethodAction();
        this.invoker = new DBMethodHelper(action, jsonModel);
        this.jsonModel = jsonModel;
    }

    public void evaluate(List<Parameter> parameters, JsonObject requestBody) {
        for (Parameter param : parameters) {
            String path = param.getName();
            String[] parts = path.split("\\.");
            JsonObject properties = requestBody;

            for (int i = 0; i < parts.length - 1; i++) {
                if (!properties.has(parts[i]) || !properties.get(parts[i]).isJsonObject()) {
                    JsonObject newChild = new JsonObject();
                    properties.add(parts[i], newChild);
                    properties = newChild;
                } else {
                    properties = properties.getAsJsonObject(parts[i]);
                }
            }

            properties.addProperty(parts[parts.length - 1], param.getValue().toString());
        }
    }

    public void buildParam(List<Parameter> parameters, String name, Object value) {
        if (ObjectUtil.isNotEmpty(value)) {
            parameters.add(new Parameter(name, value));
        }
    }

    public String getJsonValue(String jsonPath) {
        try {
            return StringUtil.toString(JsonPath.read(jsonModel, jsonPath));
        } catch (Exception e) {
            log.error("Error reading JSON path: " + jsonPath);
            return null;
        }
    }

    public String getMethodValue(String source) {
        try {
            return invoker.invoke(source);
        } catch (Exception e) {
            log.error("Error invoking method: " + source, e);
            return null;
        }
    }

    public String getValue(ExternalParam externalParam) {
        String type = externalParam.getType();
        String value = externalParam.getValue();

        switch (type) {
            case STATIC:
                return value;
            case JSONPATH:
                return getJsonValue(value);
            case METHOD:
                return getMethodValue(value);
            default:
                return null;
        }
    }

}
