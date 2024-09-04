package com.aralozkaya.discordbirthdaybot.commands;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.discordjson.json.ApplicationCommandOptionData;
import discord4j.discordjson.json.ApplicationCommandRequest;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public interface BaseCommand {
    String getName();

    String getDescription();

    Boolean isAdminCommand();

    List<ApplicationCommandOptionData> getOptions();

    Mono<Void> handle(ChatInputInteractionEvent event);

    default ApplicationCommandRequest build() {
        return ApplicationCommandRequest.builder()
                .name(getName())
                .description(getDescription())
                .addAllOptions(getOptions())
                .build();
    };
}
