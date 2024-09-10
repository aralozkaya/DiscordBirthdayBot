package com.aralozkaya.discordbirthdaybot.events;

import discord4j.core.event.domain.Event;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public interface BaseEvent<T extends Event> {
    Class<T> getEventType();

    Mono<Void> handle(T event);
}
