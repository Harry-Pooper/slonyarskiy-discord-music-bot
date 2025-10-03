package ru.discord.bot.command.music;

import org.jetbrains.annotations.NotNull;
import ru.discord.bot.command.CommandHandler;
import ru.discord.bot.model.CommandModel;
import ru.discord.bot.audioPlayer.AudioPlayerHandler;
import ru.discord.bot.util.Logger;

import java.util.List;

import static ru.discord.bot.util.DiscordMessageUtil.normalizeAndSplitByWhitespaceMessage;
import static ru.discord.bot.util.DiscordMessageUtil.sendReply;

public class PlaylistCommandHandler extends CommandHandler {

    private static final Logger log = Logger.getLogger(PlaylistCommandHandler.class);

    private final AudioPlayerHandler playerHandler;

    public PlaylistCommandHandler() {

        playerHandler = AudioPlayerHandler.getInstance();
    }

    @Override
    public void handleCommand(@NotNull CommandModel model) {

        botInSameVoiceChannel(model);

        var playlistHandler = playerHandler.getPlaylistHandler(model.getOriginGuild());

        Integer pageNum = getPageNumber(model.getOriginalMessage().getContentRaw());

        List<String> curPlaylist = playlistHandler.getCurrentPlaylist(pageNum);

        String result;

        if (curPlaylist.isEmpty()) {

            result = "Playlist is empty";
        } else {

            result = "Currently in playlist:\n".concat(String.join("\n", curPlaylist));
        }

        sendReply(model.getOriginalMessage(), "```".concat(result).concat("```"));
    }

    private Integer getPageNumber(String message) {

        String[] split = normalizeAndSplitByWhitespaceMessage(message);

        if (split.length < 2) {

            return 1;
        }

        String pageNumStr = split[1];

        try {

            return pageNumStr.isEmpty()
                    ? 1
                    : Integer.parseInt(pageNumStr);
        } catch (Exception e) {

            log.error("Could not parse page number: " + pageNumStr);

            return 1;
        }
    }
}
