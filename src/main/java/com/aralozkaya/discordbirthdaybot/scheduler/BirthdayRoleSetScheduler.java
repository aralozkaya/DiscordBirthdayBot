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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RequiredArgsConstructor
@Component
public class BirthdayRoleSetScheduler {
    private final BirthdaysRepository birthdaysRepository;
    private final AssignedRolesRepository assignedRolesRepository;
    private final GatewayDiscordClient discordClient;
    private final CurrentBirthdayAssigneesRepository currentBirthdayAssigneesRepository;

    @Scheduled(cron = "0 * * * * *")
    public void setBirthdayRoles() {
        HashMap<Long, List<Birthday>> birthdaysByGuild = new HashMap<>();
        birthdaysRepository.getAllNotCurrentlyAssignedBirthdaysByGuild_Enabled(true).forEach(birthday -> {
            if(birthdaysByGuild.containsKey(birthday.getId().getGuildId())) {
                birthdaysByGuild.get(birthday.getId().getGuildId()).add(birthday);
            } else {
                birthdaysByGuild.put(birthday.getId().getGuildId(), List.of(birthday));
            }
        });

        birthdaysByGuild.keySet().forEach(guildId -> {
            List<Birthday> birthdays = birthdaysByGuild.get(guildId);
            List<Long> assignedUsers = new ArrayList<>();
            Guild discordGuild = discordClient.getGuildById(Snowflake.of(guildId)).block();

            birthdays.forEach(birthday -> {
                LocalDate userBirthday = birthday.getBirthday();
                LocalDate now = LocalDate.now();

                if (userBirthday.getDayOfMonth() == now.getDayOfMonth() && userBirthday.getMonth() == now.getMonth()) {
                    Birthday.BirthdayId id = birthday.getId();
                    Snowflake userID = Snowflake.of(id.getUserId());

                    try {
                        AssignedRole assignedRole = assignedRolesRepository.findById(guildId).orElseThrow();
                        Snowflake roleID = Snowflake.of(assignedRole.getRoleId());

                        try {
                            discordGuild.getMemberById(userID)
                                    .flatMap(member -> member.addRole(roleID))
                                    .block();
                            assignedUsers.add(userID.asLong());

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
                    }
                    catch (Exception ignored) {
                    }
                }
            });

            String celebrateUserIDs = assignedUsers
                    .stream()
                    .reduce("",
                            (acc, snowflake) -> acc + "<@"+ snowflake + ">\n",
                            String::concat
                    );
            if (!celebrateUserIDs.isEmpty()){
                discordGuild.getSystemChannel()
                        .flatMap(channel -> channel.createMessage("Ayoo! Say happy birthday to:\n" + celebrateUserIDs))
                        .subscribe();
            }
        });
    }
}
