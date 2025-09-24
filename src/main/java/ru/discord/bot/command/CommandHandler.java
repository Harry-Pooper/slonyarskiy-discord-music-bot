package ru.discord.bot.command;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import ru.discord.bot.exceptions.AudioConnectionException;
import ru.discord.bot.model.CommandModel;
import ru.discord.bot.util.DiscordMessageUtil;

public abstract class CommandHandler {

    public void onCommand(@NotNull MessageReceivedEvent event) {

        TextChannel channel = event.getChannel().asTextChannel();
        VoiceChannel voiceChannel = requiresVoiceChannel()
                ? DiscordMessageUtil.getVoiceChannel(event)
                : null;
        Guild guild = event.getGuild();
        Message message = event.getMessage();

        CommandModel model = CommandModel.builder()
                .originChannel(channel)
                .originVoiceChannel(voiceChannel)
                .originGuild(guild)
                .originalMessage(message)
                .build();

        handleCommand(model);
    }

    protected boolean requiresVoiceChannel() {

        return true;
    }

    protected void botInSameVoiceChannel(CommandModel model) {

        VoiceChannel voiceChannel = model.getOriginVoiceChannel();
        Guild guild = model.getOriginGuild();

        VoiceChannel botVoiceChannel = guild.getAudioManager().getConnectedChannel() == null
                ? null
                : guild.getAudioManager().getConnectedChannel().asVoiceChannel();

        if (botVoiceChannel != null && !voiceChannel.getId().equals(botVoiceChannel.getId())) {

            throw new AudioConnectionException("Bot is already in use in another channel. " +
                    "Please go to that channel and then use s!stop command.");
        }
    }

    public abstract void handleCommand(@NotNull CommandModel model);
}
