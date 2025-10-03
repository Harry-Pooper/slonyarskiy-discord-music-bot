package ru.discord.bot.audioPlayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;
import net.dv8tion.jda.api.audio.AudioSendHandler;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.Nullable;
import ru.discord.bot.audioPlayer.misc.CustomQueue;
import ru.discord.bot.bot.BotStateController;
import ru.discord.bot.model.AudioTrackInfoModel;
import ru.discord.bot.util.Logger;

import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason.REPLACED;

public class AudioPlayerPlaylistHandler extends AudioEventAdapter implements AudioSendHandler {

    private static final Logger log = Logger.getLogger(AudioPlayerPlaylistHandler.class);

    private static final Set<AudioTrackEndReason> UNSKIPPABLE_REASONS = Set.of(
            AudioTrackEndReason.FINISHED
    );

    private static final int PAGE_SIZE = 10;

    private final BotStateController bsController;
    private final CustomQueue tracks;
    private final AudioPlayer audioPlayer;
    private final Guild guild;

    private AudioFrame lastFrame;

    public AudioPlayerPlaylistHandler(
            AudioPlayer audioPlayer,
            Guild guild
    ) {
        this.bsController = BotStateController.getInstance();
        this.audioPlayer = audioPlayer;
        this.guild = guild;
        this.tracks = new CustomQueue();
    }

    public void addToQueue(User author, AudioTrack track) {

        AudioTrackInfoModel trackInfo = AudioTrackInfoModel.builder()
                .author(author)
                .audioTrack(track)
                .build();

        tracks.add(trackInfo);

        if (audioPlayer.getPlayingTrack() == null) {

            log.info("No current track playing, starting new track");

            audioPlayer.playTrack(tracks.remove().getAudioTrack());
        }
    }

    public void skipTracks(int skipCount) {

        if (audioPlayer.getPlayingTrack() == null) {

            log.info("No current track playing, can't skip you daft bastard");

            return;
        }

        skipCount = skipCount > 0
                ? skipCount - 1
                : 0;

        tracks.remove(skipCount);

        if (!tracks.isEmpty()) {

            audioPlayer.playTrack(tracks.remove().getAudioTrack());

            return;
        }

        fullStop();
    }

    public void fullStop() {

        tracks.clear();
        audioPlayer.stopTrack();
        bsController.closeConnection(guild);
    }

    public List<String> getCurrentPlaylist(int pageNum) {

        pageNum -= 1;

        if (pageNum < 0) {

            pageNum = 0;
        }

        AudioTrackInfoModel currentlyPlaying = tracks.current();

        List<String> currentPlaylist = new ArrayList<>();

        AtomicInteger index = new AtomicInteger(1);

        if (currentlyPlaying != null) {

            currentPlaylist.add(formatTrack(currentlyPlaying, index).concat(" (currently plaing)"));
        }

        tracks.forEach(track -> currentPlaylist.add(formatTrack(track, index)));

        if ((pageNum + 1) * PAGE_SIZE > currentPlaylist.size()) {

            pageNum = currentPlaylist.size() / 10;

            if (currentPlaylist.size() % 10 == 0) {

                pageNum -= 1;
            }
        }

        int firstElemIdx = pageNum * PAGE_SIZE;
        int lastElemIdx = Math.min((pageNum + 1) * PAGE_SIZE, currentPlaylist.size());

        return currentPlaylist.subList(firstElemIdx, lastElemIdx);
    }

    public AudioTrack getNowPlaying() {

        AudioTrack currentlyPlaying = audioPlayer.getPlayingTrack();

        if (currentlyPlaying == null) {

            return null;
        }

        return currentlyPlaying;
    }

    private String formatTrack(AudioTrackInfoModel trackInfo, AtomicInteger index) {

        StringJoiner sb = new StringJoiner(" - ");

        AudioTrack track = trackInfo.getAudioTrack();
        User author = trackInfo.getAuthor();

        sb.add(getStringOrNull(track.getInfo().author));
        sb.add(getStringOrNull(track.getInfo().title));

        return String
                .valueOf(index.getAndAdd(1))
                .concat(". ")
                .concat(sb.toString())
                .concat(" [ordered by ")
                .concat(author.getName())
                .concat("]");
    }

    private String getStringOrNull(String str) {

        return str == null || str.isEmpty() ? "null" : str;
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {

        // Вызывается при окончании песни в плейлисте
        if (UNSKIPPABLE_REASONS.contains(endReason) && !tracks.isEmpty()) {

            audioPlayer.playTrack(tracks.remove().getAudioTrack());

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
