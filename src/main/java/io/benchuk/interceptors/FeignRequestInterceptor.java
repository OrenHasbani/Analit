package io.benchuk.interceptors;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static io.benchuk.Consts.SERVICE_NAME_HTTP_HEADER;

@Service
public class FeignRequestInterceptor implements RequestInterceptor {
    @Value("mos.service.name")
    private String serviceName;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header(SERVICE_NAME_HTTP_HEADER, serviceName);
    }
}
