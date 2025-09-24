package ru.discord.bot.audioPlayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;
import net.dv8tion.jda.api.audio.AudioSendHandler;
import net.dv8tion.jda.api.entities.Guild;
import org.jetbrains.annotations.Nullable;
import ru.discord.bot.util.Logger;

import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import static com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason.REPLACED;

public class AudioPlayerPlaylistHandler extends AudioEventAdapter implements AudioSendHandler {

    private static final Logger log = Logger.getLogger(AudioPlayerPlaylistHandler.class);
    private static final Set<AudioTrackEndReason> UNSKIPPABLE_REASONS = Set.of(
            AudioTrackEndReason.FINISHED
    );

    private final AudioConnectionController acController;
    private final Queue<AudioTrack> tracks;
    private final AudioPlayer audioPlayer;
    private final Guild guild;

    private AudioFrame lastFrame;

    public AudioPlayerPlaylistHandler(
            AudioPlayer audioPlayer,
            Guild guild
    ) {
        this.acController = AudioConnectionController.getInstance();
        this.audioPlayer = audioPlayer;
        this.guild = guild;
        this.tracks = new ConcurrentLinkedQueue<>();
    }

    public void addToQueue(AudioTrack track) {

        tracks.add(track);

        if (audioPlayer.getPlayingTrack() == null) {

            log.info("No current track playing, starting new track");

            audioPlayer.playTrack(tracks.remove());
        }
    }

    public void skipTrack() {

        if (audioPlayer.getPlayingTrack() == null) {

            log.info("No current track playing, can't skip you daft bastard");

            return;
        }

        if (!tracks.isEmpty()) {

            audioPlayer.playTrack(tracks.remove());
        }
    }

    public void fullStop() {

        tracks.clear();
        audioPlayer.stopTrack();
        acController.closeConnection(guild);
    }

    public List<String> getCurrentPlaylist() {

        AudioTrack currentlyPlaying = audioPlayer.getPlayingTrack();

        List<String> currentPlaylist = new ArrayList<>();

        AtomicInteger index = new AtomicInteger(1);

        if (currentlyPlaying != null) {

            currentPlaylist.add(formatTrack(currentlyPlaying, index).concat(" (currently plaing)"));
        }

        tracks.forEach(track -> currentPlaylist.add(formatTrack(track, index)));

        return currentPlaylist;
    }

    private String formatTrack(AudioTrack track, AtomicInteger index) {

        StringJoiner sb = new StringJoiner(" - ");

        sb.add(getStringOrNull(track.getInfo().author));
        sb.add(getStringOrNull(track.getInfo().title));

        return String.valueOf(index.getAndAdd(1)).concat(" ").concat(sb.toString());
    }

    private String getStringOrNull(String str) {

        return str == null || str.isEmpty() ? "null" : str;
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {

        // Вызывается при окончании песни в плейлисте
        if (UNSKIPPABLE_REASONS.contains(endReason) && !tracks.isEmpty()) {

            audioPlayer.playTrack(tracks.remove());

            return;
        }

        // Вызывается при скипе
        if (REPLACED.equals(endReason)) {

            return;
        }

        fullStop();
    }

    @Override
    public void onTrackException(AudioPlayer player, AudioTrack track, FriendlyException exception) {
        log.error("Occurred while playing track: {}", exception.getMessage());
    }

    @Override
    public boolean isOpus() {

        return true;
    }

    @Override
    public boolean canProvide() {

        lastFrame = audioPlayer.provide();

        return lastFrame != null;
    }

    @Nullable
    @Override
    public ByteBuffer provide20MsAudio() {

        return ByteBuffer.wrap(lastFrame.getData());
    }
}
