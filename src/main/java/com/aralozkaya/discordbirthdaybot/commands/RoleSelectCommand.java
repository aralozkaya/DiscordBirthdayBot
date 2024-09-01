package com.aralozkaya.discordbirthdaybot.commands;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandOption;
import discord4j.discordjson.json.ApplicationCommandOptionData;
import discord4j.discordjson.json.ApplicationCommandRequest;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class RoleSelectCommand implements BaseCommand {
    @Override
    public String getName() {
        return "roleselect";
    }

    @Override
    public Boolean isAdminCommand() {
        return true;
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {
        return null;
    }

    @Override
    public ApplicationCommandRequest build() {
        return ApplicationCommandRequest.builder()
                .name(getName())
                .description("Select the role you want to assign to users who have their birthday daily.")
                .addOption(ApplicationCommandOptionData.builder()
                        .name("role")
                        .description("The role to assign")
                        .required(true)
                        .type(ApplicationCommandOption.Type.ROLE.getValue())
                        .build())
                .build();
    }
}
