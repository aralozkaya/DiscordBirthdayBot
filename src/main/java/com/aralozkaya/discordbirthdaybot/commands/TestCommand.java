package com.aralozkaya.discordbirthdaybot.commands;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandOption;
import discord4j.discordjson.json.ApplicationCommandOptionChoiceData;
import discord4j.discordjson.json.ApplicationCommandOptionData;
import discord4j.discordjson.json.ApplicationCommandRequest;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class TestCommand implements BaseCommand {
    private static final List<ApplicationCommandOptionChoiceData> choices = List.of(
            ApplicationCommandOptionChoiceData.builder()
                    .name("test 1")
                    .value("test1")
                    .build(),
            ApplicationCommandOptionChoiceData.builder()
                    .name("test 2")
                    .value("test2")
                    .build()
            );

    @Override
    public String getName() {
        return "test";
    }

    @Override
    public Boolean isAdminCommand() {
        return false;
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {
        return event.reply()
                .withContent("Test Command Works!");
    }

    @Override
    public ApplicationCommandRequest build() {
        return ApplicationCommandRequest.builder()
                .name(getName())
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
}
