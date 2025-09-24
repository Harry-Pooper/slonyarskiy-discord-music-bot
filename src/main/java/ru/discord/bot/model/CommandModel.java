package ru.discord.bot.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommandModel {

    private MessageReceivedEvent event;

    private Message originalMessage;

    private TextChannel originChannel;

    private VoiceChannel originVoiceChannel;

    private Guild originGuild;
}
