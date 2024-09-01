package com.aralozkaya.discordbirthdaybot.listeners;

import com.aralozkaya.discordbirthdaybot.commands.BaseCommand;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class CommandListener {
    private final List<BaseCommand> commands;

    public CommandListener(GatewayDiscordClient client, List<BaseCommand> commands) {
        client.on(ChatInputInteractionEvent.class, this::handle).subscribe();
        this.commands = commands;
    }

    public Mono<Void> handle(ChatInputInteractionEvent event) {
        //Convert our list to a flux that we can iterate through
        return Flux.fromIterable(commands)
                //Filter out all commands that don't match the name this event is for
                .filter(command -> command.getName().equals(event.getCommandName()))
                //Get the first (and only) item in the flux that matches our filter
                .next()
                //Have our command class handle all logic related to its specific command.
                .flatMap(command -> command.handle(event));
    }
}
