package ru.discord.bot.command.admin;

import org.jetbrains.annotations.NotNull;
import ru.discord.bot.command.AdminCommandHandler;
import ru.discord.bot.model.CommandModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static ru.discord.bot.util.DiscordMessageUtil.sendReply;

public class ListRoleCommandHandler extends AdminCommandHandler {

    @Override
    public void handleCommand(@NotNull CommandModel model) {

        Set<String> roles = roleController.getRoles(model.getOriginGuild());

        List<String> roleNames = new ArrayList<>();

        model.getOriginGuild().getRoles().forEach(role -> {

            if (roles.contains(role.getId())) {

                roleNames.add(role.getName());
            }
        });

        sendReply(model.getOriginalMessage(), "```Added Roles: \n" + String.join("\n", roleNames) + "```" );
    }
}
