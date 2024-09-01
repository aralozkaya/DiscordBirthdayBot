package com.aralozkaya.discordbirthdaybot.commands;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandOption;
import discord4j.discordjson.json.ApplicationCommandOptionData;
import discord4j.discordjson.json.ApplicationCommandRequest;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class BirthdayRegisterCommand implements BaseCommand {
    private final List<ApplicationCommandOptionData> options = List.of(
            ApplicationCommandOptionData.builder()
                    .name("role")
                    .description("The role to assign")
                    .required(true)
                    .type(ApplicationCommandOption.Type.ROLE.getValue())
                    .build()
            );

    @Override
    public String getName() {
        return "birthdayregister";
    }

    @Override
    public Boolean isAdminCommand() {
        return false;
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {
        return null;
    }

    @Override
    public ApplicationCommandRequest build() {
        return ApplicationCommandRequest.builder()
                .name(getName())
                .description("Enter your birth date to receive a special role on your birthday.")
                .addAllOptions(options)
                .build();
    }
}
