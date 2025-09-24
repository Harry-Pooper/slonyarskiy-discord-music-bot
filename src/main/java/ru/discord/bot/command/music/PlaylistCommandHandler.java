package ru.discord.bot.command.music;

import org.jetbrains.annotations.NotNull;
import ru.discord.bot.command.CommandHandler;
import ru.discord.bot.model.CommandModel;
import ru.discord.bot.audioPlayer.AudioPlayerHandler;

import java.util.List;

public class PlaylistCommandHandler extends CommandHandler {

    private final AudioPlayerHandler playerHandler;

    public PlaylistCommandHandler() {

        playerHandler = AudioPlayerHandler.getInstance();
    }

    @Override
    public void handleCommand(@NotNull CommandModel model) {

        botInSameVoiceChannel(model);

        var playlistHandler = playerHandler.getAudioPlayerWrapper(model.getOriginGuild());

        List<String> curPlaylist = playlistHandler.getCurrentPlaylist();

        String result;

        if (curPlaylist.isEmpty()) {

            result = "Playlist is empty";
        } else {

            result = "Currently in playlist:\n".concat(String.join("\n", curPlaylist));
        }

        model.getOriginalMessage().reply("```".concat(result).concat("```")).queue();
    }
}
