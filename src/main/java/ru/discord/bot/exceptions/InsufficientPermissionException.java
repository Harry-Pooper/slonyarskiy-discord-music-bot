package ru.discord.bot.exceptions;

public class InsufficientPermissionException extends RuntimeException {

    public InsufficientPermissionException() {

      super("Insufficient permission of user for this command. Contact admins, idk.");
    }
}
