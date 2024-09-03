package com.aralozkaya.discordbirthdaybot.guildEvents;

import com.aralozkaya.discordbirthdaybot.dbo.Guild;
import com.aralozkaya.discordbirthdaybot.repositories.GuildsRepository;
import discord4j.core.event.domain.guild.GuildCreateEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Objects;

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
        Long roleID = Objects.requireNonNull(
                Objects.requireNonNull(
                        event.getGuild().getSelfMember()
                                .block()
                ).getRoles().next()
                        .block()
        ).getId().asLong();
        Guild guild = new Guild(guildID, LocalDate.now(), roleID);
        guildsRepository.save(guild);
        return Mono.empty();
    }
}
