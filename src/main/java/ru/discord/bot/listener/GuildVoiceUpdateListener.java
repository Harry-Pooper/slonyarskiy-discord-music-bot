package ru.discord.bot.listener;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import ru.discord.bot.audioPlayer.AudioConnectionController;
import ru.discord.bot.audioPlayer.AudioPlayerHandler;
import ru.discord.bot.util.Logger;

import java.util.concurrent.atomic.AtomicBoolean;

public class GuildVoiceUpdateListener extends ListenerAdapter {

    private final Logger log = Logger.getLogger(GuildVoiceUpdateListener.class);

    private final AudioConnectionController acController;
    private final AudioPlayerHandler playerHandler;

    public GuildVoiceUpdateListener() {

        this.acController = AudioConnectionController.getInstance();
        this.playerHandler = AudioPlayerHandler.getInstance();
    }

    @Override
    public void onGuildVoiceUpdate(@NotNull GuildVoiceUpdateEvent event) {

        log.info("Received Voice Update: " + event.getMember().getUser().getAsTag());
        log.info("Thread: " + Thread.currentThread().getName());

        Guild guild = event.getGuild();

        VoiceChannel voiceChannel = acController.getBotVoiceChannel(guild);

        if (checkVoiceChannelIsEmpty(voiceChannel)) {

            playerHandler.getAudioPlayerWrapper(guild).fullStop();
        }
    }

    private boolean checkVoiceChannelIsEmpty(VoiceChannel voiceChannel) {

        if (voiceChannel == null) {

            return false;
        }

        AtomicBoolean onlyClankers = new AtomicBoolean(true);

        voiceChannel.getMembers().forEach(member -> {
            if (!member.getUser().isBot()) {

                onlyClankers.set(false);
            }
        });

        return onlyClankers.get();
    }
}
