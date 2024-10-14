package com.aralozkaya.discordbirthdaybot;

import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.gateway.intent.Intent;
import discord4j.gateway.intent.IntentSet;
import discord4j.rest.RestClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Optional;

@SpringBootApplication
@EnableScheduling
public class DiscordBirthdayBotApplication {
	public static void main(String[] args) {
		SpringApplication.run(DiscordBirthdayBotApplication.class, args);
	}

	@Bean
	public GatewayDiscordClient gatewayDiscordClient() {
		// The token is the bot's token, not the bot's client secret
		final String DISCORD_API_TOKEN = Optional.ofNullable(System.getenv("DISCORD_API_TOKEN"))
				.orElseThrow(() -> new IllegalArgumentException("Environment variable" +
						" DISCORD_API_TOKEN must be set for the bot to start."));

		IntentSet intents = IntentSet.nonPrivileged().or(IntentSet.of(Intent.GUILD_MEMBERS));

		return DiscordClientBuilder.create(DISCORD_API_TOKEN).build()
				.gateway()
				.setEnabledIntents(intents)
				.login()
				.block();
	}

	@Bean
	public RestClient discordRestClient(GatewayDiscordClient client) {
		return client.getRestClient();
	}

	@Bean
	public Long applicationId(RestClient restClient) {
		return restClient.getApplicationId().block();
	}

}
