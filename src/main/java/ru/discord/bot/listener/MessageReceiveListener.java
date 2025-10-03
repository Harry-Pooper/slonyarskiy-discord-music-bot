package ru.discord.bot.listener;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import ru.discord.bot.command.*;
import ru.discord.bot.command.admin.AddRoleCommandHandler;
import ru.discord.bot.command.admin.AdminHelpCommandHandler;
import ru.discord.bot.command.admin.ListRoleCommandHandler;
import ru.discord.bot.command.admin.RemoveRoleCommandHandler;
import ru.discord.bot.command.general.HelpCommandHandler;
import ru.discord.bot.command.music.*;
import ru.discord.bot.util.Logger;

import java.util.Map;

import static java.util.Map.entry;
import static ru.discord.bot.util.DiscordMessageUtil.sendMessage;
import static ru.discord.bot.util.DiscordMessageUtil.sendReply;

public class MessageReceiveListener extends ListenerAdapter {

    private static final Logger log = Logger.getLogger(MessageReceiveListener.class);

    private static final Map<String, CommandHandler> commandHandlers = Map.ofEntries(
            entry("admin", new AdminHelpCommandHandler()),
            entry("addrole", new AddRoleCommandHandler()),
            entry("removerole", new RemoveRoleCommandHandler()),
            entry("listrole", new ListRoleCommandHandler()),
            entry("help", new HelpCommandHandler()),
            entry("play", new PlayCommandHandler()),
            entry("skip", new SkipCommandHandler()),
            entry("stop", new StopCommandHandler()),
            entry("playlist", new PlaylistCommandHandler()),
            entry("np", new NowPlayingCommandHandler())
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

                sendReply(event.getMessage(), "```Ey boss! Got an error: \n" + e.getMessage() + "```");
            }
        }
    }

    private void sendUnknownCommandMessage(MessageReceivedEvent event) {

        TextChannel channel = event.getChannel().asTextChannel();

        sendMessage(channel, "```Unknown command, bruh. Try calling s!help command```");
    }
}
