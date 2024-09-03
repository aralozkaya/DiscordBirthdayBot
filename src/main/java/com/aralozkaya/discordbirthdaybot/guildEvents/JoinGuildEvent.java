package com.aralozkaya.discordbirthdaybot.guildEvents;

import discord4j.core.event.domain.guild.GuildCreateEvent;
import discord4j.core.event.domain.guild.GuildEvent;
import reactor.core.publisher.Mono;

public class JoinGuildEvent implements BaseEvent {
    @Override
    public Class<? extends GuildEvent> eventClass() {
        return GuildCreateEvent.class;
    }

    @Override
    public Mono<Void> handle(GuildEvent event) {
        return null;
    }
}
