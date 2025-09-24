package ru.discord.bot.audioPlayer;

import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import dev.lavalink.youtube.YoutubeAudioSourceManager;

public class AudioPlayerManager extends DefaultAudioPlayerManager {

    public void init() {

        YoutubeAudioSourceManager yt = new YoutubeAudioSourceManager();
        yt.setPlaylistPageCount(30);
        registerSourceManager(yt);

    }
}
