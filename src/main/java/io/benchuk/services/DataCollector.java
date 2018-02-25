package io.benchuk.services;

import io.benchuk.clients.CollectorClient;
import io.benchuk.dtos.ServiceAggregatedApiEvents;
import io.benchuk.models.AggregatedEvent;
import io.benchuk.models.ApiEvent;
import io.benchuk.utils.EventIdGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DataCollector {

    private Map<String, AggregatedEvent> dataMap;
    private Queue<ApiEvent> pending = new ConcurrentLinkedQueue<>();

    @Value("mos.service.name")
    private String serviceName;

    private final CollectorClient client;

    @PostConstruct
    public void init() {
        this.dataMap = new HashMap<>();
    }

    public void collectApiEvent(ApiEvent apiEvent) {
        if (DataValidatorService.allValued(apiEvent)) {
            pending.add(apiEvent);
        }
    }

    @Scheduled(
            initialDelayString = "${event-processor-scheduler.initial.delay.ms:0}",
            fixedDelayString = "${event-processor-scheduler.delay.ms:60000}"
    )
    public void processData() {
        try {

            ApiEvent event = pending.poll();
            while (event != null) {
                String key = EventIdGenerator.getKey(event);
                if (dataMap.containsKey(key)) {
                    dataMap.get(key).inc();
                } else {
                    dataMap.put(key, new AggregatedEvent(event));
                }

                event = pending.poll();
            }

            client.reportAggregatedAPIs(dtoFromMap(dataMap));
        }
        catch (Exception e) {
            System.out.println("Error occurred while processing aggregated api events" + e.toString());
            e.printStackTrace();
        } finally {
            dataMap.clear();
        }
    }

    private ServiceAggregatedApiEvents dtoFromMap(Map<String, AggregatedEvent> dataMap) {
        return ServiceAggregatedApiEvents.builder()
            .aggregatedEvents(new ArrayList<>(dataMap.values()))
            .serviceName(this.serviceName)
            .build();
    }


}
