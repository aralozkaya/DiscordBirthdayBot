package com.aralozkaya.discordbirthdaybot.scheduler;

import com.aralozkaya.discordbirthdaybot.dbo.*;
import com.aralozkaya.discordbirthdaybot.repositories.AssignedRolesRepository;
import com.aralozkaya.discordbirthdaybot.repositories.BirthdaysRepository;
import com.aralozkaya.discordbirthdaybot.repositories.CurrentBirthdayAssigneesRepository;
import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.entity.Guild;
import discord4j.rest.http.client.ClientException;
import io.netty.handler.codec.http.HttpResponseStatus;
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
    private final CurrentBirthdayAssigneesRepository currentBirthdayAssigneesRepository;

    @Scheduled(cron = "0 * * * * *")
    public void setBirthdayRoles() {
        birthdaysRepository.getAllByGuild_Enabled(true).forEach(birthday -> {
            LocalDate userBirthday = birthday.getBirthday();
            LocalDate now = LocalDate.now();

            if (userBirthday.getDayOfMonth() == now.getDayOfMonth() && userBirthday.getMonth() == now.getMonth()) {
                Birthday.BirthdayId id = birthday.getId();
                Snowflake guildId = Snowflake.of(id.getGuildId());
                Snowflake userID = Snowflake.of(id.getUserId());

                try {
                    AssignedRole assignedRole = assignedRolesRepository.findById(id.getGuildId()).orElseThrow();
                    Snowflake roleID = Snowflake.of(assignedRole.getRoleId());
                    Guild discordGuild = discordClient.getGuildById(guildId).block();

                    try {
                        discordGuild.getMemberById(userID)
                                .flatMap(member -> member.addRole(roleID))
                                .block();
                    } catch (ClientException e) {
                        if(e.getStatus().compareTo(HttpResponseStatus.FORBIDDEN) == 0) {
                            discordGuild.getSystemChannel()
                                    .flatMap(channel -> channel.createMessage("I can't assign the birthday role. " +
                                            "The selected role might be higher than my role. " +
                                            "Please check the role hierarchy. " +
                                            "If the hierarchy looks correct, try moving the role up and down again."))
                                    .subscribe();
                        } else {
                            e.printStackTrace();
                        }
                    }

                    CurrentBirthdayAssignee.CurrentBirthdayAssigneeId currentBirthdayAssigneeId =
                            new CurrentBirthdayAssignee.CurrentBirthdayAssigneeId(id.getGuildId(), id.getUserId());
                    CurrentBirthdayAssignee currentBirthdayAssignee =
                            new CurrentBirthdayAssignee(
                                    currentBirthdayAssigneeId,
                                    birthday,
                                    LocalDate.now(),
                                    assignedRole.getRoleId()
                            );

                    currentBirthdayAssigneesRepository.save(currentBirthdayAssignee);
                }
                catch (Exception ignored) {
                }
            }
        });
    }
}
