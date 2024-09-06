package com.aralozkaya.discordbirthdaybot.scheduler;

import com.aralozkaya.discordbirthdaybot.dbo.AssignedRole;
import com.aralozkaya.discordbirthdaybot.dbo.BirthdayId;
import com.aralozkaya.discordbirthdaybot.repositories.AssignedRolesRepository;
import com.aralozkaya.discordbirthdaybot.repositories.BirthdaysRepository;
import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@RequiredArgsConstructor
@Component
public class BirthdayRoleSetScheduler {
    private final BirthdaysRepository birthdaysRepository;
    private final AssignedRolesRepository assignedRolesRepository;
    private final GatewayDiscordClient discordClient;

    @Scheduled(cron = "0 * * * *")
    public void setBirthdayRoles() {
        birthdaysRepository.findAll().forEach(birthday -> {
            LocalDate userBirthday = birthday.getBirthday();
            LocalDate now = LocalDate.now();

            if (userBirthday.getDayOfMonth() == now.getDayOfMonth() && userBirthday.getMonth() == now.getMonth()) {
                BirthdayId id = birthday.getId();
                Snowflake guildId = Snowflake.of(id.getGuildId());
                Snowflake userID = Snowflake.of(id.getUserId());

                try {
                    AssignedRole assignedRole = assignedRolesRepository.findById(id.getGuildId()).orElseThrow();
                    Snowflake roleID = Snowflake.of(assignedRole.getRoleId());

                    discordClient.getGuildById(guildId)
                            .flatMap(guild -> guild.getMemberById(userID))
                            .flatMap(member -> member.addRole(roleID))
                            .subscribe();
                }
                catch (Exception ignored) {
                }
            }
        });
    }
}
