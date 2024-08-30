package com.aralozkaya.discordbirthdaybot.commands;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.discordjson.json.ApplicationCommandRequest;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Component;

@Component
public interface BotCommand {
    Publisher<?> handler(ChatInputInteractionEvent event);

    Publisher<?> handler();

    ApplicationCommandRequest build();
    void register();
}
