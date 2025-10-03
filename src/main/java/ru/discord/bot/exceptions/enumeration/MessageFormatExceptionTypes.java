package ru.discord.bot.exceptions.enumeration;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MessageFormatExceptionTypes {

    INVALID_ARG_COUNT("Invalid count of arguments for this command. Refer to s!help command"),
    INVALID_ARG_FORMAT("Invalid format of arguments for this command. Refer to s!help command");

    private final String message;
}
