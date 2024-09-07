package com.aralozkaya.discordbirthdaybot.scheduler;

import com.aralozkaya.discordbirthdaybot.dbo.CurrentBirthdayAssigneeId;
import com.aralozkaya.discordbirthdaybot.repositories.CurrentBirthdayAssigneesRepository;
import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@RequiredArgsConstructor
@Component
public class BirthdayRoleRemoveScheduler {
    private final GatewayDiscordClient discordClient;
    private final CurrentBirthdayAssigneesRepository currentBirthdayAssigneesRepository;

    @Scheduled(cron = "0 * * * * *")
    public void removeBirthdayRoles() {
        currentBirthdayAssigneesRepository.findAll().forEach(assignee -> {
            CurrentBirthdayAssigneeId id = assignee.getId();

            Snowflake guildId = Snowflake.of(id.getGuildId());
            Snowflake userID = Snowflake.of(id.getUserId());
            Snowflake roleID = Snowflake.of(assignee.getRoleId());

            LocalDate assignedDate = assignee.getAssignedOn();
            LocalDate now = LocalDate.now();
            LocalDate birthday = assignee.getBirthdays().getBirthday();

            if(assignedDate.getMonth() != now.getMonth() || assignedDate.getDayOfMonth() != now.getDayOfMonth()
                    || birthday.getMonth() != now.getMonth() || birthday.getDayOfMonth() != now.getDayOfMonth()) {
                try {
                    discordClient.getGuildById(guildId)
                            .flatMap(guild -> guild.getMemberById(userID))
                            .flatMap(member -> member.removeRole(roleID))
                            .block();

                    currentBirthdayAssigneesRepository.delete(assignee);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
