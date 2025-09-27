package ru.discord.bot.async;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;

public abstract class SentMessageBaseRunnable implements Runnable {

    protected Message initialMessage = null;
    protected Guild guild = null;

    @Override
    public abstract void run();

    public void setInitialMessage(final Guild guild, final Message initialMessage) {

        this.guild = guild;
        this.initialMessage = initialMessage;
    }
}
