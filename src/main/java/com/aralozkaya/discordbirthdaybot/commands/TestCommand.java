package com.aralozkaya.discordbirthdaybot.commands;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandOption;
import discord4j.discordjson.json.ApplicationCommandOptionChoiceData;
import discord4j.discordjson.json.ApplicationCommandOptionData;
import discord4j.discordjson.json.ApplicationCommandRequest;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

import java.util.List;

public class TestCommand implements BotCommand {
    private static final List<ApplicationCommandOptionChoiceData> choices = List.of(
            ApplicationCommandOptionChoiceData.builder()
                    .name("test")
                    .value("test")
                    .build()
    );;

    @Override
    public Publisher<?> handler(ChatInputInteractionEvent event) {
        return null;
    }

    @Override
    public Publisher<?> handler() {
        return Mono.empty();
    }

    @Override
    public ApplicationCommandRequest build() {
        return ApplicationCommandRequest.builder()
                .name("test")
                .description("A test command")
                .addOption(ApplicationCommandOptionData.builder()
                        .name("test")
                        .description("A test option")
                        .required(true)
                        .type(ApplicationCommandOption.Type.STRING.getValue())
                        .choices(choices)
                        .build())
                .build();
    }

    @Override
    public void register() {

    }
}
