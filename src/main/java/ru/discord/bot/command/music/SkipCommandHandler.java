package ru.discord.bot.command.music;

import org.jetbrains.annotations.NotNull;
import ru.discord.bot.command.CommandHandler;
import ru.discord.bot.exceptions.MessageFormatException;
import ru.discord.bot.model.CommandModel;
import ru.discord.bot.audioPlayer.AudioPlayerHandler;
import ru.discord.bot.util.Logger;

import static ru.discord.bot.exceptions.enumeration.MessageFormatExceptionTypes.INVALID_ARG_COUNT;
import static ru.discord.bot.exceptions.enumeration.MessageFormatExceptionTypes.INVALID_ARG_FORMAT;
import static ru.discord.bot.util.DiscordMessageUtil.normalizeAndSplitByWhitespaceMessage;
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

        var playlistHandler = playerHandler.getPlaylistHandler(model.getOriginGuild());

        int skipCount = getSkipCount(model.getOriginalMessage().getContentRaw());

        playlistHandler.skipTracks(skipCount);

        sendReply(model.getOriginalMessage(), "```Skipping track```");
    }

    public int getSkipCount(String message) {

        String[] split = normalizeAndSplitByWhitespaceMessage(message);

        if (split.length > 2) {

            throw new MessageFormatException(INVALID_ARG_COUNT.getMessage());
        }

        if (split.length == 1) {

            return 1;
        }

        try {

            return Integer.parseInt(split[1]);
        } catch (NumberFormatException e) {

            throw new MessageFormatException(INVALID_ARG_FORMAT.getMessage());
        }
    }
}
