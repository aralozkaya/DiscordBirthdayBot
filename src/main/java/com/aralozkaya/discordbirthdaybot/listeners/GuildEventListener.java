package com.aralozkaya.discordbirthdaybot.listeners;

import com.aralozkaya.discordbirthdaybot.events.BaseEvent;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.guild.GuildEvent;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class GuildEventListener {
    private final List<BaseEvent> events;

    public GuildEventListener(GatewayDiscordClient client, List<BaseEvent> events) {
        client.on(GuildEvent.class, this::handle).subscribe();
        this.events = events;
    }

    public Mono handle(GuildEvent event) {
        //Convert our list to a flux that we can iterate through
        return Flux.fromIterable(events)
                //Filter out all commands that don't match the name this event is for
                .filter(e -> e.getEventType().equals(event.getClass()))
                //Get the first (and only) item in the flux that matches our filter
                .next()
                //Have our command class handle all logic related to its specific command.
                .flatMap(e -> e.handle(event));
    }
}
