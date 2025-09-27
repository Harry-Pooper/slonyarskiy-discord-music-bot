package ru.discord.bot.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import net.dv8tion.jda.api.entities.Message;

@Getter
@Builder
@AllArgsConstructor
public class LastSentMessageModel {

    private Message message;

    private Thread thread;

    private Runnable runnable;
}
