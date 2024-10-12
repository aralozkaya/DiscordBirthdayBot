package com.aralozkaya.discordbirthdaybot.scheduler;

import com.aralozkaya.discordbirthdaybot.dbo.*;
import com.aralozkaya.discordbirthdaybot.repositories.AssignableBirthdayUserRepository;
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
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class BirthdayRoleSetScheduler {
    private final AssignableBirthdayUserRepository assignableBirthdayUserRepository;
    private final CurrentBirthdayAssigneesRepository currentBirthdayAssigneesRepository;
    private final GatewayDiscordClient discordClient;

    @Scheduled(cron = "0 * * * * *")
    public void setBirthdayRoles() {
        Iterable<AssignableBirthdayUser> assignableBirthdayUsersRaw = assignableBirthdayUserRepository.findAll();

        HashMap<Long, List<AssignableBirthdayUser>> assignableBirthdayUsers = new HashMap<>();
        assignableBirthdayUsersRaw.forEach(assignableBirthdayUser -> {
            LocalDate now = LocalDate.now();
            LocalDate birthday = assignableBirthdayUser.getBirthday();
            if (now.getMonth() == birthday.getMonth() && now.getDayOfMonth() == birthday.getDayOfMonth()) {
                long guildId = assignableBirthdayUser.getId().getGuildId();
                if (!assignableBirthdayUsers.containsKey(guildId)) {
                    assignableBirthdayUsers.put(guildId, new ArrayList<>());
                }
                assignableBirthdayUsers.get(guildId).add(assignableBirthdayUser);
            }
        });

        assignableBirthdayUsers.forEach((guildID, assignableBirthdayUser) -> {
            Snowflake guildIdSnowflake = Snowflake.of(guildID);
            Guild discordGuild = discordClient.getGuildById(guildIdSnowflake).block();
            if (discordGuild == null) {
                return;
            }

            List<Long> successfullyAssigned = new ArrayList<>();

            assignableBirthdayUser.forEach(user -> {
                Long userID = user.getId().getUserId();
                Long roleID = user.getRoleId();

                Snowflake userIDSnowflake = Snowflake.of(userID);
                Snowflake roleIDSnowflake = Snowflake.of(roleID);

                try {
                    discordGuild.getMemberById(userIDSnowflake)
                            .flatMap(member -> member.addRole(roleIDSnowflake))
                            .block();

                    com.aralozkaya.discordbirthdaybot.dbo.Guild guild = new com.aralozkaya.discordbirthdaybot.dbo.Guild();
                    guild.setId(guildID);

                    Birthday.BirthdayId birthdayId = new Birthday.BirthdayId(guildID, userID);
                    Birthday birthday = new Birthday(birthdayId, guild, user.getBirthday());

                    CurrentBirthdayAssignee.CurrentBirthdayAssigneeId currentBirthdayAssigneeId =
                            new CurrentBirthdayAssignee.CurrentBirthdayAssigneeId(guildID, userID);
                    CurrentBirthdayAssignee currentBirthdayAssignee =
                            new CurrentBirthdayAssignee(
                                    currentBirthdayAssigneeId,
                                    birthday,
                                    LocalDate.now(),
                                    roleID
                            );
                    currentBirthdayAssigneesRepository.save(currentBirthdayAssignee);

                    successfullyAssigned.add(user.getId().getUserId());
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
            });

            if (!successfullyAssigned.isEmpty()) {
                String mentions = successfullyAssigned.stream().map(aLong -> "<@" + aLong + ">\n").collect(Collectors.joining());
                String message = "\uD83C\uDF89 Ayoo! Say happy birthday to these lovely people \uD83C\uDF89:\n" + mentions;

                discordGuild.getSystemChannel()
                        .flatMap(channel -> channel.createMessage(message))
                        .subscribe();
            }
        });
    }
}
