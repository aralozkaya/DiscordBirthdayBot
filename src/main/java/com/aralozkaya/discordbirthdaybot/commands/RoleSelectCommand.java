package com.aralozkaya.discordbirthdaybot.commands;

import com.aralozkaya.discordbirthdaybot.dbo.AssignedRole;
import com.aralozkaya.discordbirthdaybot.repositories.AssignedRolesRepository;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import discord4j.core.object.command.ApplicationCommandOption;
import discord4j.core.object.entity.Role;
import discord4j.discordjson.json.ApplicationCommandOptionData;
import discord4j.discordjson.json.ApplicationCommandRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
public class RoleSelectCommand implements BaseCommand {
    private final AssignedRolesRepository assignedRoleRepository;

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
        Role role = event.getOption("role")
                .flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asRole)
                .get()
                .block();

        Long guildID = role.getGuildId().asLong();
        Long roleID = role.getId().asLong();

        assignedRoleRepository.findById(guildID)
                .ifPresentOrElse(
                        assignedRole -> {
                            assignedRole.setRoleId(roleID);
                            assignedRoleRepository.save(assignedRole);
                        },
                        () -> {
                            AssignedRole assignedRole = new AssignedRole(guildID, roleID);
                            assignedRoleRepository.save(assignedRole);
                        }
                );
        return Mono.empty();
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
