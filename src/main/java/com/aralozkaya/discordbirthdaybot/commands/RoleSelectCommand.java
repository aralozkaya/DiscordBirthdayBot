package com.aralozkaya.discordbirthdaybot.commands;

import com.aralozkaya.discordbirthdaybot.dbo.AssignedRole;
import com.aralozkaya.discordbirthdaybot.dbo.Guild;
import com.aralozkaya.discordbirthdaybot.repositories.AssignedRolesRepository;
import com.aralozkaya.discordbirthdaybot.repositories.GuildsRepository;
import discord4j.common.util.Snowflake;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import discord4j.core.object.command.ApplicationCommandOption;
import discord4j.core.object.entity.PartialMember;
import discord4j.core.object.entity.Role;
import discord4j.discordjson.json.ApplicationCommandOptionData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
@Component
public class RoleSelectCommand implements BaseCommand {
    private final AssignedRolesRepository assignedRoleRepository;
    private final GuildsRepository guildsRepository;

    @Override
    public String getName() {
        return "roleselect";
    }

    @Override
    public String getDescription() {
        return "Select the role you want to assign to users who have their birthday daily.";
    }

    @Override
    public Boolean isAdminCommand() {
        return true;
    }

    @Override
    public List<ApplicationCommandOptionData> getOptions() {
        return List.of(ApplicationCommandOptionData.builder()
                .name("role")
                .description("The role to assign")
                .required(true)
                .type(ApplicationCommandOption.Type.ROLE.getValue())
                .build());
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {
        Role selectedRole = event.getOption("role")
                .flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asRole)
                .get()
                .block();

        Snowflake guildID = selectedRole.getGuildId();

        Role botRole = event.getClient()
                .getSelfMember(guildID)
                .flatMap(PartialMember::getHighestRole)
                .block();

        if(botRole.getPosition().block() < selectedRole.getPosition().block()) {
            return event.reply()
                    .withContent("You can't select a role higher than the bot's role! " +
                    "Go to the server settings and put the targeted birthday role below the bot role")
                    .withEphemeral(true);
        }

        Long guildIDLong = guildID.asLong();
        Long roleIDLong = selectedRole.getId().asLong();

        assignedRoleRepository.findById(guildIDLong)
                .ifPresentOrElse(
                        assignedRole -> {
                            assignedRole.setRoleId(roleIDLong);
                            assignedRoleRepository.save(assignedRole);
                        },
                        () -> {
                            Guild guild = guildsRepository.findById(guildIDLong).orElseThrow();
                            AssignedRole assignedRole = new AssignedRole(guildIDLong, guild, roleIDLong);
                            assignedRoleRepository.save(assignedRole);
                        }
                );
        return event.reply()
                .withContent("Role: " + selectedRole.getName() + " is selected!")
                .withEphemeral(true);
    }
}
