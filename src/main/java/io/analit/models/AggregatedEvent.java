package io.analit.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AggregatedEvent {
    private ApiEvent event;
    private int count;

    public AggregatedEvent(ApiEvent apiEvent) {
        this.event = apiEvent;
        this.count = 1;
    }

    public void inc() {
        this.count ++;
    }
}
