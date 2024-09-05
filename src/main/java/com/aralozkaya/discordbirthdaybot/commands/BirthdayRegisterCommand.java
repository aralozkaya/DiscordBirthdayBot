package com.aralozkaya.discordbirthdaybot.commands;

import com.aralozkaya.discordbirthdaybot.dbo.Birthday;
import com.aralozkaya.discordbirthdaybot.dbo.BirthdayId;
import com.aralozkaya.discordbirthdaybot.repositories.BirthdaysRepository;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import discord4j.core.object.command.ApplicationCommandOption;
import discord4j.core.object.entity.Message;
import discord4j.core.spec.InteractionApplicationCommandCallbackSpec;
import discord4j.discordjson.json.ApplicationCommandOptionData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.*;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalField;
import java.util.List;

@RequiredArgsConstructor
@Component
public class BirthdayRegisterCommand implements BaseCommand {
    private final BirthdaysRepository birthdaysRepository;

    @Override
    public String getName() {
        return "birthdayregister";
    }

    @Override
    public String getDescription() {
        return "Enter your birth date to receive a special role on your birthday.";
    }

    @Override
    public Boolean isAdminCommand() {
        return false;
    }

    @Override
    public List<ApplicationCommandOptionData> getOptions() {
        return List.of(
                ApplicationCommandOptionData.builder()
                        .name("day")
                        .description("your birthday day")
                        .required(true)
                        .type(ApplicationCommandOption.Type.INTEGER.getValue())
                        .minValue(1.0)
                        .maxValue(31.0)
                        .build(),
                ApplicationCommandOptionData.builder()
                        .name("month")
                        .description("your birthday month")
                        .required(true)
                        .type(ApplicationCommandOption.Type.INTEGER.getValue())
                        .minValue(1.0)
                        .maxValue(12.0)
                        .build(),
                ApplicationCommandOptionData.builder()
                        .name("year")
                        .description("your birthyear (optional)")
                        .required(false)
                        .type(ApplicationCommandOption.Type.INTEGER.getValue())
                        .minValue(1900.0)
                        .maxValue((double) LocalDateTime.now().getYear())
                        .build()
        );
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {
        int systemHour = LocalTime.now().getHour();

        int userHour = event.getInteraction()
                .getMessage()
                .map(Message::getTimestamp)
                .map(instant -> instant.get(ChronoField.HOUR_OF_DAY))
                .get();

        int timeDifference = userHour - systemHour;

        int day = event.getOption("day")
                .flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asLong)
                .map(Long::intValue)
                .get();

        int month = event.getOption("month")
                .flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asLong)
                .map(Long::intValue)
                .get();

        int year = event.getOption("year")
                .flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asLong)
                .map(Long::intValue)
                .orElse(1900);

        LocalDate birthdate;

        try {
            birthdate = LocalDate.of(year, month, day);
        } catch (Exception e) {
            return event.reply(InteractionApplicationCommandCallbackSpec.builder()
                    .content("Invalid Date!")
                    .ephemeral(true)
                    .build());
        }



        Long guildID = event.getInteraction().getGuildId().get().asLong();
        Long userID = event.getInteraction().getUser().getId().asLong();

        BirthdayId birthdayId = new BirthdayId(guildID, userID);

        birthdaysRepository.findById(birthdayId)
                .ifPresentOrElse(
                        birthday -> {
                            birthday.setBirthday(birthdate);
                            birthdaysRepository.save(birthday);
                        },
                        () -> {
                            Birthday birthday = new Birthday(birthdayId, birthdate, timeDifference);
                            birthdaysRepository.save(birthday);
                        }
                );

        return event.reply(InteractionApplicationCommandCallbackSpec.builder()
                .content("Your birthday is set as: " + day + "th " + Month.of(month).name())
                .ephemeral(true)
                .build());
    }
}
