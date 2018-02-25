package io.benchuk.utils;

import io.benchuk.models.ApiEvent;
import lombok.experimental.UtilityClass;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

@UtilityClass
public class EventIdGenerator {

    private static final String DELIMITER = ":";

    public String getKey(ApiEvent data) {

        StringBuilder base = new StringBuilder(data.getControllerClass());
        addInNotEmpty(base, data.getControllerPath());
        addInNotEmpty(base, data.getMethodPath());
        addInNotEmpty(base, data.getRestMethod());
        addInNotEmpty(base, data.getMethodName());
        addInNotEmpty(base, data.getFromService());

        return DigestUtils.md5DigestAsHex(base.toString().getBytes());

    }

    private void addInNotEmpty(StringBuilder base, String str) {
        if (!StringUtils.isEmpty(str)) {
            base.append(str).append(DELIMITER);
        }
    }
}
