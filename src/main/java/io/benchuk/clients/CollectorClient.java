package io.benchuk.clients;


import io.benchuk.dtos.ServiceAggregatedApiEvents;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "benchukService", url = "${benchuk.api.host}")
public interface CollectorClient {

    @RequestMapping(method = RequestMethod.POST, value = "/aggregated-api", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    void reportAggregatedAPIs(@RequestBody ServiceAggregatedApiEvents dto);

}
