package ru.discord.bot.command.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Message;
import org.jetbrains.annotations.NotNull;
import ru.discord.bot.async.NowPlayingMessageUpdateRunnable;
import ru.discord.bot.audioPlayer.AudioPlayerHandler;
import ru.discord.bot.command.CommandHandler;
import ru.discord.bot.model.CommandModel;
import ru.discord.bot.util.Logger;

import static ru.discord.bot.util.DiscordMessageUtil.getSongProgressLine;
import static ru.discord.bot.util.DiscordMessageUtil.sendReply;

public class NowPlayingCommandHandler extends CommandHandler {

    private static final Logger log = Logger.getLogger(NowPlayingCommandHandler.class);

    private final AudioPlayerHandler playerHandler;

    public NowPlayingCommandHandler() {

        this.playerHandler = AudioPlayerHandler.getInstance();
    }

    @Override
    public void handleCommand(@NotNull CommandModel model) {

        botInSameVoiceChannel(model);

        Message originalMessage = model.getOriginalMessage();

        AudioTrack currentTrack = playerHandler.getPlaylistHandler(model.getOriginGuild()).getNowPlaying();

//        log.info("" + currentTrack.getDuration()); // duration of song in millis
//        log.info("" + currentTrack.getPosition()); // current position
//        log.info("" + currentTrack.getState()); // state

        String message = "```"
                .concat(currentTrack.getInfo().author)
                .concat(currentTrack.getInfo().title)
                .concat("\n")
                .concat(getSongProgressLine(currentTrack))
                .concat("```");

        sendReply(originalMessage, message, new NowPlayingMessageUpdateRunnable());
    }
}
