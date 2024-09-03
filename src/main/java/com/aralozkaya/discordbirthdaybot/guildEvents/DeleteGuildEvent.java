package com.aralozkaya.discordbirthdaybot.guildEvents;

import com.aralozkaya.discordbirthdaybot.repositories.GuildsRepository;
import discord4j.core.event.domain.guild.GuildDeleteEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
public class DeleteGuildEvent implements BaseEvent<GuildDeleteEvent> {
    private final GuildsRepository guildsRepository;

    @Override
    public Class<GuildDeleteEvent> getEventType() {
        return GuildDeleteEvent.class;
    }

    @Override
    public Mono<Void> handle(GuildDeleteEvent event) {
        Long guildID = event.getGuildId().asLong();
        guildsRepository.deleteById(guildID);
        return Mono.empty();
    }
}
