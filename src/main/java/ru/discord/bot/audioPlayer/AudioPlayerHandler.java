package ru.discord.bot.audioPlayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import lombok.Getter;
import net.dv8tion.jda.api.audio.AudioSendHandler;
import net.dv8tion.jda.api.entities.Guild;

public class AudioPlayerHandler {

    @Getter
    private AudioPlayerManager audioPlayerManager;

    public AudioPlayerHandler() {

        init();
    }

    private void init() {

        AudioPlayerManager audioPlayerManager = new AudioPlayerManager();
        audioPlayerManager.init();
        AudioSourceManagers.registerRemoteSources(audioPlayerManager);

        this.audioPlayerManager = audioPlayerManager;
    }

    public AudioPlayerPlaylistHandler getPlaylistHandler(Guild guild) {

        AudioSendHandler audioSendHandler = guild.getAudioManager().getSendingHandler();

        if (!(audioSendHandler instanceof AudioPlayerPlaylistHandler)) {

            AudioPlayer audioPlayer = audioPlayerManager.createPlayer();
            audioPlayer.setVolume(100);

            audioSendHandler = new AudioPlayerPlaylistHandler(audioPlayer, guild);
            audioPlayer.addListener((AudioPlayerPlaylistHandler) audioSendHandler);

            guild.getAudioManager().setSendingHandler(audioSendHandler);

            return (AudioPlayerPlaylistHandler) audioSendHandler;
        }

        return (AudioPlayerPlaylistHandler) guild.getAudioManager().getSendingHandler();
    }

    private static class Holder {

        private static AudioPlayerHandler INSTANCE;

    }

    public static AudioPlayerHandler getInstance() {

        if (Holder.INSTANCE == null) {

            Holder.INSTANCE = new AudioPlayerHandler();
        }

        return Holder.INSTANCE;
    }
}
