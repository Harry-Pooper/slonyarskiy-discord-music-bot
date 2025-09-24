package ru.discord.bot.command.music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import org.jetbrains.annotations.NotNull;
import ru.discord.bot.audioPlayer.AudioConnectionController;
import ru.discord.bot.command.CommandHandler;
import ru.discord.bot.exceptions.AudioConnectionException;
import ru.discord.bot.exceptions.MessageFormatException;
import ru.discord.bot.model.CommandModel;
import ru.discord.bot.audioPlayer.AudioPlayerHandler;
import ru.discord.bot.audioPlayer.AudioPlayerPlaylistHandler;
import ru.discord.bot.util.Logger;

public class PlayCommandHandler extends CommandHandler {

    private static final Logger log = Logger.getLogger(PlayCommandHandler.class);

    private final AudioPlayerHandler playerHandler;
    private final AudioConnectionController acController;

    public PlayCommandHandler() {

        this.acController = AudioConnectionController.getInstance();
        this.playerHandler = AudioPlayerHandler.getInstance();
    }

    public void handleCommand(@NotNull CommandModel model) {

        botInSameVoiceChannel(model);

        VoiceChannel voiceChannel = model.getOriginVoiceChannel();
        Guild guild = model.getOriginGuild();

        String url = getUrlFromMessage(model.getOriginalMessage().getContentRaw());

        try {

            this.acController.openConnection(guild, voiceChannel);
        } catch (Exception e) {

            throw new AudioConnectionException(
                    "Error occurred during connecting to voice channel. " +
                            "Probably insufficient rights to voice channel, idk."
            );
        }

        AudioPlayerPlaylistHandler apPlaylistHandler = playerHandler.getAudioPlayerWrapper(guild);

        playerHandler.getAudioPlayerManager().loadItem(url, new LoadResult(model, apPlaylistHandler));
    }

    private String getUrlFromMessage(String message) {

        String normalizedMessage = message.trim();
        int msgLength = normalizedMessage.length();

        do {
            normalizedMessage = normalizedMessage.replaceAll(" {2}", " ");
        } while (msgLength != normalizedMessage.length());

        String[] split = message.split(" ");

        if (split.length != 2) {

            throw new MessageFormatException("Incorrect format. Should be s!play <url>");
        }

        return split[1];
    }

    static class LoadResult implements AudioLoadResultHandler {

        private final CommandModel commandModel;
        private final AudioPlayerPlaylistHandler playlistHandler;

        public LoadResult(
                CommandModel commandModel,
                AudioPlayerPlaylistHandler playlistHandler
        ) {
            this.commandModel = commandModel;
            this.playlistHandler = playlistHandler;
        }

        @Override
        public void trackLoaded(AudioTrack audioTrack) {

            log.info("Found track, adding to квеве");

            commandModel.getOriginalMessage()
                    .reply("```Found track, adding to queue```")
                    .queue();

            playlistHandler.addToQueue(audioTrack);
        }

        @Override
        public void playlistLoaded(AudioPlaylist audioPlaylist) {

            int size = audioPlaylist.getTracks().size();

            log.info("Found " + audioPlaylist.getTracks().size() + " tracks");

            commandModel.getOriginalMessage()
                    .reply("```Found " + size + " songs. Will start shortly.```")
                    .queue();

            audioPlaylist
                    .getTracks()
                    .forEach(playlistHandler::addToQueue);

            log.info("Added " + audioPlaylist.getTracks().size() + " tracks. EVERYFINK");
        }

        @Override
        public void noMatches() {

            commandModel.getOriginalMessage().reply("```Nofink faund)))```").queue();
        }

        @Override
        public void loadFailed(FriendlyException e) {

            commandModel.getOriginalMessage()
                    .reply("``` Got exception while loading music: " + e.getMessage() + "```")
                    .queue();
        }
    }
}
