package ru.discord.bot.command;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import ru.discord.bot.exceptions.InsufficientPermissionException;

public abstract class AdminCommandHandler extends CommandHandler {

    @Override
    protected void validateRoles(MessageReceivedEvent event) {

        Member member = event.getMember();

        if (member == null || !member.hasPermission(Permission.ADMINISTRATOR)) {

            throw new InsufficientPermissionException();
        }
    }
}
