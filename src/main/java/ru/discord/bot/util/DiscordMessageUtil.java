package ru.discord.bot.util;

import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import ru.discord.bot.exceptions.VoiceChannelNotFound;

public class DiscordMessageUtil {

    public static VoiceChannel getVoiceChannel(@NotNull final MessageReceivedEvent event) {

        var member = event.getMember();

        if (member != null) {

            var voiceState = member.getVoiceState();

            if (voiceState != null && voiceState.inAudioChannel() && voiceState.getChannel() != null) {

                return voiceState.getChannel().asVoiceChannel();
            }
        }

        throw new VoiceChannelNotFound("User voice channel cannot be found");
    }
}
