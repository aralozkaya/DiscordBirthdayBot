package com.aralozkaya.discordbirthdaybot.events;

import com.aralozkaya.discordbirthdaybot.dbo.Guild;
import com.aralozkaya.discordbirthdaybot.repositories.GuildsRepository;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Component
public class ClientReadyEvent implements BaseEvent<ReadyEvent> {
   private final GuildsRepository guildsRepository;

    @Override
    public Class<ReadyEvent> getEventType() {
        return ReadyEvent.class;
    }

    @Override
    public Mono<Void> handle(ReadyEvent event) {
        List<Long> guildIDs = event.getGuilds()
                .stream()
                .map(guild -> guild.getId().asLong())
                .toList();

        guildIDs.forEach(guildID -> {
            if (!guildsRepository.existsById(guildID)) {
                Guild guild = new Guild(guildID, LocalDate.now());
                guildsRepository.save(guild);
            }
        });

        return Mono.empty();
    }
}
