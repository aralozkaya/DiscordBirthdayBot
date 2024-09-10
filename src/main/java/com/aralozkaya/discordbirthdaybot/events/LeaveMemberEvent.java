package com.aralozkaya.discordbirthdaybot.events;

import com.aralozkaya.discordbirthdaybot.repositories.BirthdaysRepository;
import discord4j.core.event.domain.guild.MemberLeaveEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
public class LeaveMemberEvent implements BaseEvent<MemberLeaveEvent> {
    private final BirthdaysRepository birthdaysRepository;

    @Override
    public Class<MemberLeaveEvent> getEventType() {
        return MemberLeaveEvent.class;
    }

    @Override
    public Mono<Void> handle(MemberLeaveEvent event) {
        Long guildID = event.getGuildId().asLong();
        Long userID = event.getUser().getId().asLong();

        birthdaysRepository.deleteById_GuildIdAndId_UserId(guildID, userID);

        return Mono.empty();
    }
}
