package ru.discord.bot.command.general;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.jetbrains.annotations.NotNull;
import ru.discord.bot.command.CommandHandler;
import ru.discord.bot.model.CommandModel;
import ru.discord.bot.util.Logger;

import java.util.StringJoiner;

public class HelpCommandHandler extends CommandHandler {

    private static final Logger log = Logger.getLogger(HelpCommandHandler.class);

    private static final String HELP_STRING = new StringJoiner("\n")
            .add("List of available commands:")
            .add("s!help - Prints this menu (duh)")
            .add("s!play <url> - Insert song to current queue")
            .add("s!skip - Skip current song")
            .add("s!stop - Stop playing songs")
            .add("s!playlist - Show current playlist")
            .toString();

    @Override
    public void handleCommand(@NotNull CommandModel model) {

        TextChannel channel = model.getOriginChannel();

        channel.sendMessage("```".concat(HELP_STRING).concat("```")).queue();
    }

    @Override
    protected boolean requiresVoiceChannel() {

        return false;
    }
}
