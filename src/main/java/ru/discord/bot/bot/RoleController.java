package ru.discord.bot.bot;

import lombok.Getter;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import ru.discord.bot.config.Configuration;
import ru.discord.bot.exceptions.InsufficientPermissionException;
import ru.discord.bot.exceptions.MessageFormatException;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RoleController {

    @Getter
    private static RoleController instance;

    private final Configuration configuration;

    public RoleController(Configuration configuration) {
        this.configuration = configuration;
    }

    public void validateRole(Guild guild, Member member) {

        if (member.hasPermission(Permission.ADMINISTRATOR)) {

            return;
        }

        List<Role> memberRoles = member.getRoles();

        String guildId = guild.getId();

        Set<String> approvedRoleIds = configuration.getRoles().get(guildId);

        if (approvedRoleIds == null || approvedRoleIds.isEmpty()) {

            return;
        }

        for (Role role : memberRoles) {

            if (approvedRoleIds.contains(role.getId())) {

                return;
            }
        }

        throw new InsufficientPermissionException();
    }

    public Set<String> getRoles(Guild guild) {

        Set<String> roles = configuration.getRoles().get(guild.getId());

        return roles == null ? new HashSet<>() : roles;
    }

    public void addRole(Guild guild, String roleName) {

        String searchRoleName = roleName.toLowerCase().trim().replaceAll("\\s+", " ");

        for (Role role: guild.getRoles()) {

            String roleStr = role.getName().toLowerCase().trim().replaceAll("\\s+", " ");

            if (roleStr.equals(searchRoleName)) {

                Map<String, Set<String>> roles = configuration.getRoles();

                Set<String> roleSet = roles.get(guild.getId());

                if (roleSet == null) {

                    roleSet = new HashSet<>();
                }

                roleSet.add(role.getId());

                roles.put(guild.getId(), roleSet);

                configuration.updateRoles(roles);

                return;
            }
        }

        throw new MessageFormatException("Role '" + roleName + "' not found");
    }

    public void removeRole(Guild guild, String roleName) {

        String searchRoleName = roleName.toLowerCase().trim().replaceAll("\\s+", " ");

        for (Role role: guild.getRoles()) {

            String roleStr = role.getName().toLowerCase().trim().replaceAll("\\s+", " ");

            if (roleStr.equals(searchRoleName)) {

                Map<String, Set<String>> roles = configuration.getRoles();

                Set<String> roleSet = roles.get(guild.getId());

                if (roleSet == null) {

                    roleSet = new HashSet<>();
                }

                roleSet.remove(role.getId());

                configuration.updateRoles(roles);

                return;
            }
        }
    }

    public static void setInstance(final RoleController instance) {
        RoleController.instance = instance;
    }
}
