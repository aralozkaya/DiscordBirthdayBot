package com.aralozkaya.discordbirthdaybot;

import com.aralozkaya.discordbirthdaybot.commands.BaseCommand;
import discord4j.discordjson.json.ApplicationCommandRequest;
import discord4j.rest.RestClient;
import discord4j.rest.service.ApplicationService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DiscordBirthdayBotRunner implements ApplicationRunner {
    private final RestClient restClient;
    private final List<BaseCommand> commands;

    public DiscordBirthdayBotRunner(RestClient restClient, List<BaseCommand> commands) {
        this.restClient = restClient;
        this.commands = commands;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        final ApplicationService applicationService = restClient.getApplicationService();
        final long applicationId = restClient.getApplicationId().block();
        final List<ApplicationCommandRequest> builtCommands = commands.stream().map(BaseCommand::build).toList();

        System.out.println("Registering global commands");

        applicationService.bulkOverwriteGlobalApplicationCommand(applicationId, builtCommands)
                .doOnNext(cmd -> System.out.println("Successfully registered Global Command " + cmd.name()))
                .doOnError(e -> System.out.println("Failed to register global commands" + e))
                .doOnComplete(() -> System.out.println("Successfully registered all global commands"))
                .subscribe();
    }
}
