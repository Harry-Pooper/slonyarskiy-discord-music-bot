package ru.discord.bot.listener;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import ru.discord.bot.command.*;
import ru.discord.bot.command.general.HelpCommandHandler;
import ru.discord.bot.command.music.PlayCommandHandler;
import ru.discord.bot.command.music.PlaylistCommandHandler;
import ru.discord.bot.command.music.SkipCommandHandler;
import ru.discord.bot.command.music.StopCommandHandler;
import ru.discord.bot.util.Logger;

import java.util.Map;

public class MessageReceiveListener extends ListenerAdapter {

    private static final Logger log = Logger.getLogger(MessageReceiveListener.class);

    private static final Map<String, CommandHandler> commandHandlers = Map.of(
            "help", new HelpCommandHandler(),
            "play", new PlayCommandHandler(),
            "skip", new SkipCommandHandler(),
            "stop", new StopCommandHandler(),
            "playlist", new PlaylistCommandHandler()
    );

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {

        if (event.getAuthor().isBot()) return;

        log.info("Message received: ".concat(event.getMessage().getContentRaw()));
        log.info("Thread: ".concat(Thread.currentThread().getName()));

        String message = event.getMessage().getContentRaw();

        if (message.startsWith("s!")) {

            String command = message.split(" ")[0].toLowerCase().substring(2);

            CommandHandler handler = commandHandlers.get(command);

            if (handler == null) {

                sendUnknownCommandMessage(event);

                return;
            }

            try {

                handler.onCommand(event);
            } catch (Exception e) {

                event.getMessage()
                        .reply("```Ey boss! Got an error: \n" + e.getMessage() + "```")
                        .queue();
            }
        }
    }

    private void sendUnknownCommandMessage(MessageReceivedEvent event) {

        TextChannel channel = event.getChannel().asTextChannel();

        channel.sendMessage("```Unknown command, bruh. Try calling s!help command```").queue();
    }
}
