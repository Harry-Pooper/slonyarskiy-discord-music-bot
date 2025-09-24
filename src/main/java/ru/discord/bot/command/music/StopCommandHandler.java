package ru.discord.bot.command.music;

import org.jetbrains.annotations.NotNull;
import ru.discord.bot.command.CommandHandler;
import ru.discord.bot.model.CommandModel;
import ru.discord.bot.audioPlayer.AudioPlayerHandler;
import ru.discord.bot.util.Logger;

public class StopCommandHandler extends CommandHandler {

    private static final Logger log = Logger.getLogger(StopCommandHandler.class);

    private final AudioPlayerHandler playerHandler;

    public StopCommandHandler() {

        playerHandler = AudioPlayerHandler.getInstance();
    }

    @Override
    public void handleCommand(@NotNull CommandModel model) {

        botInSameVoiceChannel(model);

        var playlistHandler = playerHandler.getAudioPlayerWrapper(model.getOriginGuild());

        playlistHandler.fullStop();

        model.getOriginalMessage().reply("```Stopping```").queue();
    }
}
