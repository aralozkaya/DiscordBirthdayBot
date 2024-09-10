package com.aralozkaya.discordbirthdaybot.events;

import com.aralozkaya.discordbirthdaybot.dbo.Guild;
import com.aralozkaya.discordbirthdaybot.repositories.GuildsRepository;
import discord4j.core.event.domain.guild.GuildCreateEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@RequiredArgsConstructor
@Component
public class JoinGuildEvent implements BaseEvent<GuildCreateEvent> {
    private final GuildsRepository guildsRepository;

    @Override
    public Class<GuildCreateEvent> getEventType() {
        return GuildCreateEvent.class;
    }

    @Override
    public Mono<Void> handle(GuildCreateEvent event) {
        Long guildID = event.getGuild().getId().asLong();
        Guild guild = new Guild(guildID, LocalDate.now());
        guildsRepository.save(guild);
        return Mono.empty();
    }
}
