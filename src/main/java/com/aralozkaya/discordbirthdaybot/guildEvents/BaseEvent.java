package com.aralozkaya.discordbirthdaybot.guildEvents;

import discord4j.core.event.domain.guild.GuildEvent;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public interface BaseEvent {
    Class<? extends GuildEvent> eventClass();

    Mono<Void> handle(GuildEvent event);
}
