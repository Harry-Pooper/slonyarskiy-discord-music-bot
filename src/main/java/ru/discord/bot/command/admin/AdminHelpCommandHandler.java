package ru.discord.bot.command.admin;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.NotNull;
import ru.discord.bot.command.AdminCommandHandler;
import ru.discord.bot.command.CommandHandler;
import ru.discord.bot.exceptions.InsufficientPermissionException;
import ru.discord.bot.model.CommandModel;

import java.util.StringJoiner;

public class AdminHelpCommandHandler extends AdminCommandHandler {

    private static final String ADMIN_HELP_STRING = new StringJoiner("\n")
            .add("List of available commands:")
            .add("s!admin - Shows commands for administrators of guild")
            .add("s!addRole <role> - Adds discord role to list which role can use bot [role - discord role name, case sensitive]")
            .add("s!removeRole <role> - Removes discord role from list which role can use bot [role - discord role name, case sensitive]")
            .add("s!listRole - Lists all discord roles eligible to use bot")
            .add("-------------------------------------")
            .add("IF NO ROLES SET FOR MUSIC BOT, ANYONE CAN USE THE BOT")
            .toString();

    @Override
    public void handleCommand(@NotNull CommandModel model) {

        Member author = model.getOriginalMessage().getMember();

        User authorUser = author.getUser();

        authorUser.openPrivateChannel()
                .flatMap(channel -> channel.sendMessage("```".concat(ADMIN_HELP_STRING).concat("```")))
                .queue();
    }

    @Override
    protected boolean requiresVoiceChannel() {
        return false;
    }
}
