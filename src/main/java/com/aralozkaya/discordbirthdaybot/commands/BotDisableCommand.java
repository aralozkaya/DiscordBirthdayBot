package com.aralozkaya.discordbirthdaybot.commands;

import com.aralozkaya.discordbirthdaybot.repositories.GuildsRepository;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.discordjson.json.ApplicationCommandOptionData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
@Component
public class BotDisableCommand implements BaseCommand {
    private final GuildsRepository guildsRepository;

    @Override
    public String getName() {
        return "disable";
    }

    @Override
    public String getDescription() {
        return "Disables the bot in the current server.";
    }

    @Override
    public Boolean isAdminCommand() {
        return true;
    }

    @Override
    public List<ApplicationCommandOptionData> getOptions() {
        return List.of();
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {
        Long guildID = event.getInteraction().getGuildId().get().asLong();
        guildsRepository.findByIdAndEnabled(guildID, true)
                .ifPresentOrElse(guild -> {
                    guild.setEnabled(false);
                    guildsRepository.save(guild);
                    event.reply("Bot has been disabled!").block();
                },() -> event.reply("Bot is already disabled!").block());

        return Mono.empty();
    }
}
