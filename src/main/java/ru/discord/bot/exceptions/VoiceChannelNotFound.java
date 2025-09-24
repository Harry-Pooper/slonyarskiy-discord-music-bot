package ru.discord.bot.exceptions;


public class VoiceChannelNotFound extends RuntimeException {

    public VoiceChannelNotFound(String message) {
        super(message);
    }
}
