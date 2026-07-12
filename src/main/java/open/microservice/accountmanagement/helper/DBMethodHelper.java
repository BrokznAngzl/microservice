package open.microservice.accountmanagement.helper;

import com.jayway.jsonpath.JsonPath;
import lombok.extern.log4j.Log4j2;

import java.lang.reflect.Method;
import java.util.Arrays;

@Log4j2
public class DBMethodHelper {
    private final Object target;
    private String jsonModel;

    public DBMethodHelper(Object target, String jsonModel) {
        this.target = target;
        this.jsonModel = jsonModel;
    }

    public String invoke(String methodCall) throws Exception {
        String[] parts = methodCall.split("\\|");
        String methodName = parts[0];
        String[] args = Arrays.copyOfRange(parts, 1, parts.length);
        for (int i = 0; i < args.length; i++) {
            if (args[i].startsWith("$.")) {
                args[i] = JsonPath.read(jsonModel, args[i]);
            }
        }
        Object[] input = new Object[]{args};

        Method method = findMethod(methodName, input);
        if (method == null) {
            log.error("Method '{}' not found in target class '{}'", methodName, target.getClass().getName());
            return null;
        }

        Object result = method.invoke(target, input);
        return result != null ? result.toString() : null;
    }

    /* need implement */
    private Method findMethod(String name, Object... args) {
        for (Method method : target.getClass().getMethods()) {
            if (method.getName().equals(name)) {
                Class<?>[] paramTypes = method.getParameterTypes();
                if (paramTypes.length == args.length) {
                    return method;
                }
            }
        }
        return null;
    }
}
