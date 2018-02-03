package io.analit.services;

import io.analit.utils.EventIdGenerator;
import io.analit.models.AggregatedEvent;
import io.analit.models.ApiEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DataCollector {

    private Map<String, AggregatedEvent> dataMap;
    private Queue<ApiEvent> pending = new ConcurrentLinkedQueue<>();

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

        // todo send dataMap to server
    }


}
