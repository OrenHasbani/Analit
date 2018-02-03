package io.analit.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@Data
public class ApiEvent {
    private String controllerPath;
    private String methodPath;
    private String restMethod;
    private String methodName;
    private String controllerClass;
    private String fromService;

    public ApiEvent() {

    }

    public boolean allValued() {
        return controllerPath != null
                && restMethod != null
                && methodName != null
                && controllerClass != null
                && fromService != null;
    }

}
