package ru.discord.bot.command.music;

import org.jetbrains.annotations.NotNull;
import ru.discord.bot.command.CommandHandler;
import ru.discord.bot.model.CommandModel;
import ru.discord.bot.audioPlayer.AudioPlayerHandler;
import ru.discord.bot.util.Logger;

import static ru.discord.bot.util.DiscordMessageUtil.sendReply;

public class SkipCommandHandler extends CommandHandler {

    private static final Logger log = Logger.getLogger(SkipCommandHandler.class);

    private final AudioPlayerHandler playerHandler;

    public SkipCommandHandler() {

        playerHandler = AudioPlayerHandler.getInstance();
    }

    @Override
    public void handleCommand(@NotNull CommandModel model) {

        botInSameVoiceChannel(model);

        // TODO - Добавить обработку скипа нескольких песен
        var playlistHandler = playerHandler.getPlaylistHandler(model.getOriginGuild());

        playlistHandler.skipTrack();

        sendReply(model.getOriginalMessage(), "```Skipping track```");
    }
}
