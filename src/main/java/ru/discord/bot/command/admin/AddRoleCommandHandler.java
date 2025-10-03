package ru.discord.bot.command.admin;

import org.jetbrains.annotations.NotNull;
import ru.discord.bot.command.AdminCommandHandler;
import ru.discord.bot.exceptions.MessageFormatException;
import ru.discord.bot.model.CommandModel;

import static ru.discord.bot.exceptions.enumeration.MessageFormatExceptionTypes.INVALID_ARG_COUNT;
import static ru.discord.bot.util.DiscordMessageUtil.normalizeAndSplitByWhitespaceMessage;
import static ru.discord.bot.util.DiscordMessageUtil.sendReply;

public class AddRoleCommandHandler extends AdminCommandHandler {

    @Override
    public void handleCommand(@NotNull CommandModel model) {

        String[] command = normalizeAndSplitByWhitespaceMessage(model.getOriginalMessage().getContentRaw());

        if (command.length != 2) {

            throw new MessageFormatException(INVALID_ARG_COUNT.getMessage());
        }

        roleController.addRole(model.getOriginGuild(), command[1]);

        sendReply(model.getOriginalMessage(), "```Added role " + command[1] + "```");
    }
}
