package ru.discord.bot.command.music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import org.jetbrains.annotations.NotNull;
import ru.discord.bot.bot.BotStateController;
import ru.discord.bot.command.CommandHandler;
import ru.discord.bot.exceptions.AudioConnectionException;
import ru.discord.bot.exceptions.MessageFormatException;
import ru.discord.bot.model.CommandModel;
import ru.discord.bot.audioPlayer.AudioPlayerHandler;
import ru.discord.bot.audioPlayer.AudioPlayerPlaylistHandler;
import ru.discord.bot.util.Logger;

import static ru.discord.bot.exceptions.enumeration.MessageFormatExceptionTypes.INVALID_ARG_COUNT;
import static ru.discord.bot.util.DiscordMessageUtil.normalizeAndSplitByWhitespaceMessage;
import static ru.discord.bot.util.DiscordMessageUtil.sendReply;

public class PlayCommandHandler extends CommandHandler {

    private static final Logger log = Logger.getLogger(PlayCommandHandler.class);

    private final AudioPlayerHandler playerHandler;
    private final BotStateController bsController;

    public PlayCommandHandler() {

        this.bsController = BotStateController.getInstance();
        this.playerHandler = AudioPlayerHandler.getInstance();
    }

    public void handleCommand(@NotNull CommandModel model) {

        botInSameVoiceChannel(model);

        VoiceChannel voiceChannel = model.getOriginVoiceChannel();
        Guild guild = model.getOriginGuild();

        String url = getUrlFromMessage(model.getOriginalMessage().getContentRaw());

        try {

            this.bsController.openConnection(guild, voiceChannel);
        } catch (Exception e) {

            throw new AudioConnectionException(
                    "Error occurred during connecting to voice channel. " +
                            "Probably insufficient rights to voice channel, idk."
            );
        }

        AudioPlayerPlaylistHandler apPlaylistHandler = playerHandler.getPlaylistHandler(guild);

        playerHandler.getAudioPlayerManager().loadItem(url, new LoadResult(model, apPlaylistHandler));
    }

    private String getUrlFromMessage(String message) {

        String[] split = normalizeAndSplitByWhitespaceMessage(message);

        if (split.length != 2) {

            throw new MessageFormatException(INVALID_ARG_COUNT.getMessage());
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

            sendReply(commandModel.getOriginalMessage(), "```Found track, adding to queue```");

            User user = commandModel.getOriginalMessage().getAuthor();

            playlistHandler.addToQueue(user, audioTrack);
        }

        @Override
        public void playlistLoaded(AudioPlaylist audioPlaylist) {

            int size = audioPlaylist.getTracks().size();

            log.info("Found " + audioPlaylist.getTracks().size() + " tracks");

            sendReply(commandModel.getOriginalMessage(), "```Found " + size + " songs. Will start shortly.```");

            User author = commandModel.getOriginalMessage().getAuthor();

            audioPlaylist
                    .getTracks()
                    .forEach(track -> playlistHandler.addToQueue(author, track));

            log.info("Added " + audioPlaylist.getTracks().size() + " tracks. EVERYFINK");
        }

        @Override
        public void noMatches() {

            sendReply(commandModel.getOriginalMessage(), "```Nofink faund)))```");

            playlistHandler.fullStop();
        }

        @Override
        public void loadFailed(FriendlyException e) {

            sendReply(commandModel.getOriginalMessage(), "``` Got exception while loading music: " + e.getMessage() + "```");

            playlistHandler.fullStop();
        }
    }
}
