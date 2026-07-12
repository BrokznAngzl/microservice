package open.microservice.accountmanagement.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Parameter {

    private String name;
    private Object value;

    public Parameter(String name, Object data) {
        this.name = name;
        this.value = data;
    }
}