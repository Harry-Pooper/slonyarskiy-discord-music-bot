package ru.discord.bot.async;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import lombok.SneakyThrows;
import ru.discord.bot.audioPlayer.AudioPlayerHandler;
import ru.discord.bot.audioPlayer.AudioPlayerPlaylistHandler;

import static ru.discord.bot.util.DiscordMessageUtil.getSongProgressLine;

public class NowPlayingMessageUpdateRunnable  extends SentMessageBaseRunnable {

    private final AudioPlayerHandler apHandler;

    public NowPlayingMessageUpdateRunnable() {

        apHandler = AudioPlayerHandler.getInstance();
    }

    @Override
    public void run() {

        AudioPlayerPlaylistHandler playlistHandler = apHandler.getPlaylistHandler(guild);

        AudioTrack currentTrack = playlistHandler.getNowPlaying();

        while (currentTrack != null) {

            try {

                Thread.sleep(1000);
            } catch (InterruptedException e) {

                // ignored - tee-hee
            }

            String message = "```"
                    .concat(currentTrack.getInfo().author)
                    .concat(currentTrack.getInfo().title)
                    .concat("\n")
                    .concat(getSongProgressLine(currentTrack))
                    .concat("```");

            initialMessage.editMessage(message).queue();

            currentTrack = playlistHandler.getNowPlaying();
        }
    }
}
