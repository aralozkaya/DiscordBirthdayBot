package com.aralozkaya.discordbirthdaybot.commands;

import com.aralozkaya.discordbirthdaybot.dbo.BirthdayId;
import com.aralozkaya.discordbirthdaybot.repositories.BirthdaysRepository;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.spec.InteractionApplicationCommandCallbackSpec;
import discord4j.discordjson.json.ApplicationCommandOptionData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
@Component
public class BirthdayDeleteCommand implements BaseCommand {
    private final BirthdaysRepository birthdaysRepository;

    @Override
    public String getName() {
        return "birthdaydelete";
    }

    @Override
    public String getDescription() {
        return "Remove your birthday data from the bot.";
    }

    @Override
    public Boolean isAdminCommand() {
        return false;
    }

    @Override
    public List<ApplicationCommandOptionData> getOptions() {
        return List.of();
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {
        Long guildID = event.getInteraction().getGuildId().get().asLong();
        Long userID = event.getInteraction().getUser().getId().asLong();

        BirthdayId birthdayId = new BirthdayId(guildID, userID);

        if(birthdaysRepository.existsById(birthdayId)) {
            birthdaysRepository.deleteById(birthdayId);
            return event.reply(InteractionApplicationCommandCallbackSpec.builder()
                    .content("Your birthday data has been removed.")
                    .ephemeral(true)
                    .build());
        } else {
            return event.reply(InteractionApplicationCommandCallbackSpec.builder()
                    .content("You have not registered your birthday yet!")
                    .ephemeral(true)
                    .build());
        }
    }
}
