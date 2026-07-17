package open.microservice.accountmanagement.helper;

import com.jayway.jsonpath.JsonPath;
import lombok.extern.log4j.Log4j2;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Log4j2
public class DBMethodHelper {
    private final Object target;
    private String jsonModel;

    public DBMethodHelper(Object target, String jsonModel) {
        this.target = target;
        this.jsonModel = jsonModel;
    }

    public String invoke(String methodCall) throws Exception {
        Matcher matcher = Pattern.compile("^(\\w+)\\((.*)\\)$").matcher(methodCall.trim());

        if (!matcher.matches()) {
            log.error("invalid method call: {}", methodCall);
            return null;
        }

        String methodName = matcher.group(1);
        String argString = matcher.group(2);

        String[] rawArgs = argString.isEmpty()
                ? new String[0]
                : argString.split(",");
//                : argString.split("\\s*,\\s*"); // clear white space around comma

        Object[] args = new Object[rawArgs.length];

        for (int i = 0; i < rawArgs.length; i++) {
            if (rawArgs[i].startsWith("$.")) {
                args[i] = JsonPath.read(jsonModel, rawArgs[i]);
            } else {
                args[i] = rawArgs[i];
            }
        }

        Method method = findMethod(methodName, args);
        if (method == null) {
            log.error("Method '{}' not found in target class '{}'", methodName, target.getClass().getName());
            return null;
        }

//        Object result = method.isVarArgs()
//                ? method.invoke(target, new Object[]{args})
//                : method.invoke(target, args);

        Object result;

        if (args.length == 0) {
            result = method.invoke(target);
        } else if (args.length == 1) {
            result = method.invoke(target, args);
        } else {
            result = method.invoke(target, new Object[]{args});
        }

        return result != null ? String.valueOf(result) : null;

    }

    /*
    public String invoke(String methodCall) throws Exception {
        String[] parts = methodCall.split("\\|");
        String methodName = parts[0];

        Object[] args = new Object[parts.length - 1];

        for (int i = 1; i < parts.length; i++) {
            String arg = parts[i];

            if (arg.startsWith("$.")) {
                args[i - 1] = JsonPath.read(jsonModel, arg);
            } else {
                args[i - 1] = arg;
            }
        }

        Method method = findMethod(methodName, args);
        if (method == null) {
            log.error("Method '{}' not found in target class '{}'",
                    methodName, target.getClass().getName());
            return null;
        }

        Object result = method.invoke(target, args);
        return result != null ? String.valueOf(result) : null;
    }
     */

    /* need implement */
    private Method findMethod(String name, Object... args) {
        for (Method method : target.getClass().getMethods()) {
            if (method.getName().equals(name)) {
                Class<?>[] paramTypes = method.getParameterTypes();

                if (paramTypes.length == 1 && paramTypes[0].isArray()) {
                    return method;
                }

                if (paramTypes.length == args.length) {
                    return method;
                }
            }
        }
        return null;
    }
}
