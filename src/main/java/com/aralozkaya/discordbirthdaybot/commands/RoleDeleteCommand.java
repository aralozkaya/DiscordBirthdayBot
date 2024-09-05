package com.aralozkaya.discordbirthdaybot.commands;

import com.aralozkaya.discordbirthdaybot.dbo.AssignedRole;
import com.aralozkaya.discordbirthdaybot.repositories.AssignedRolesRepository;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import discord4j.core.object.command.ApplicationCommandOption;
import discord4j.core.object.entity.Role;
import discord4j.discordjson.json.ApplicationCommandOptionData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
@Component
public class RoleDeleteCommand implements BaseCommand {
    private final AssignedRolesRepository assignedRoleRepository;

    @Override
    public String getName() {
        return "roledelete";
    }

    @Override
    public String getDescription() {
        return "Remove the previously selected role to assign users who have their birthday.";
    }

    @Override
    public Boolean isAdminCommand() {
        return true;
    }

    @Override
    public List<ApplicationCommandOptionData> getOptions() {
        return List.of();
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {
        Long guildID = event.getInteraction().getGuildId().get().asLong();
        boolean exists = assignedRoleRepository.existsById(guildID);
        if (exists) {
            assignedRoleRepository.deleteById(guildID);
            return event.reply("Role has been removed!");
        } else {
            return event.reply("No role was selected before!");
        }
    }
}
