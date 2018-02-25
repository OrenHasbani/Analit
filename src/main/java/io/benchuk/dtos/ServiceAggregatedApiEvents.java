package io.benchuk.dtos;

import io.benchuk.models.AggregatedEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ServiceAggregatedApiEvents {
    private String serviceName;
    private List<AggregatedEvent> aggregatedEvents;
}
