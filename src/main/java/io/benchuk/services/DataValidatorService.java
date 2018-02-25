package io.benchuk.services;

import io.benchuk.models.ApiEvent;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DataValidatorService {

    public boolean allValued(ApiEvent data) {

        return  data != null
                && data.getControllerPath() != null
                && data.getRestMethod() != null
                && data.getMethodName() != null
                && data.getControllerClass() != null
                && data.getFromService() != null;
    }
}
