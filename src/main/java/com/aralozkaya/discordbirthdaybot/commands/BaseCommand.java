package com.aralozkaya.discordbirthdaybot.commands;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.discordjson.json.ApplicationCommandRequest;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public interface BaseCommand {
    String getName();

    Boolean isAdminCommand();

    Mono<Void> handle(ChatInputInteractionEvent event);

    ApplicationCommandRequest build();
}
