package com.aralozkaya.discordbirthdaybot.scheduler;

import com.aralozkaya.discordbirthdaybot.dbo.CurrentBirthdayAssignee;
import com.aralozkaya.discordbirthdaybot.repositories.AssignedRolesRepository;
import com.aralozkaya.discordbirthdaybot.repositories.CurrentBirthdayAssigneesRepository;
import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class BirthdayRoleUnregisteredRemoveScheduler {
    private final GatewayDiscordClient discordClient;
    private final CurrentBirthdayAssigneesRepository currentBirthdayAssigneesRepository;
    private final AssignedRolesRepository assignedRolesRepository;

    @Scheduled(cron = "0 * * * * *")
    public void removeUnregisteredBirthdayRoles() {
        assignedRolesRepository.findAllByGuilds_Enabled(true).forEach(assignedRole -> {
            Long guildId = assignedRole.getGuilds().getId();
            Snowflake guildIdSnowflake = Snowflake.of(assignedRole.getGuilds().getId());

            Long roleId = assignedRole.getRoleId();
            Snowflake roleIdSnowflake = Snowflake.of(roleId);

            Guild discordGuild = discordClient.getGuildById(guildIdSnowflake).block();
            if (discordGuild == null) {
                return;
            }

            List<CurrentBirthdayAssignee> assignees = currentBirthdayAssigneesRepository.getAllById_GuildId(guildId);

            List<Member> unregisteredAssignees = discordGuild.getMembers().collectList().block();
            if (unregisteredAssignees == null) {
                return;
            }
            unregisteredAssignees = unregisteredAssignees.stream().filter(member -> assignees.stream().noneMatch(assignee -> assignee.getId().getUserId() == member.getId().asLong()) && member.getRoleIds().contains(roleIdSnowflake)).toList();

            unregisteredAssignees.forEach(member -> {
                try {
                    member.removeRole(roleIdSnowflake).subscribe();
                    member.getPrivateChannel().flatMap(privateChannel -> privateChannel.createMessage("Your birthday role, in the server \"**" + discordGuild.getName() + "**\" is assigned to you outside of the BirthdayBot and is therefore removed. To get the birthday role, please register your birthday within the bot.")).subscribe();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        });
    }
}
