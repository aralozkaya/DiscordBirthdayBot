package com.aralozkaya.discordbirthdaybot.listeners;

import com.aralozkaya.discordbirthdaybot.events.BaseEvent;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.lifecycle.GatewayLifecycleEvent;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class ClientEventListener {
    private final List<BaseEvent> events;

    public ClientEventListener(GatewayDiscordClient client, List<BaseEvent> events) {
        client.on(GatewayLifecycleEvent.class, this::handle).subscribe();
        this.events = events;
    }

    public Mono handle(GatewayLifecycleEvent event) {
        return Flux.fromIterable(events)
                .filter(e -> e.getEventType().equals(event.getClass()))
                .next()
                .flatMap(e -> e.handle(event));
    }
}
