package ru.discord.bot;


import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import ru.discord.bot.listener.GuildVoiceUpdateListener;
import ru.discord.bot.listener.MessageReceiveListener;
import ru.discord.bot.util.Logger;

import java.util.Arrays;
import java.util.List;

import static net.dv8tion.jda.api.requests.GatewayIntent.*;
import static net.dv8tion.jda.api.utils.cache.CacheFlag.*;

public class Main {

    private static final Logger log = Logger.getLogger(Main.class);

    private static final List<GatewayIntent> INTENTS = Arrays.asList(
            GUILD_MEMBERS,
            GUILD_PRESENCES,
            GUILD_EXPRESSIONS,
            GUILD_VOICE_STATES,
            GUILD_MESSAGES,
            GUILD_MESSAGE_REACTIONS,
            GUILD_MESSAGE_TYPING,
            MESSAGE_CONTENT
    );

    private static final List<CacheFlag> CACHE = Arrays.asList(
            CLIENT_STATUS,
            ROLE_TAGS,
            VOICE_STATE
    );

    public static void main(String[] args) {

        String token = System.getenv("token");

        JDA jda = JDABuilder
                .createDefault(token, INTENTS)
                .enableCache(CACHE)
                .disableCache(CacheFlag.SCHEDULED_EVENTS)
                .build();

        jda.addEventListener(new MessageReceiveListener());
        jda.addEventListener(new GuildVoiceUpdateListener());
    }
}