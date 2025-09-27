package ru.discord.bot.util;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.requests.restaction.MessageCreateAction;
import org.jetbrains.annotations.NotNull;
import ru.discord.bot.async.SentMessageBaseRunnable;
import ru.discord.bot.bot.BotStateController;
import ru.discord.bot.exceptions.VoiceChannelNotFound;
import ru.discord.bot.model.LastSentMessageModel;

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

    public static String getSongProgressLine(@NotNull final AudioTrack track) {

        long trackDurationInSec = track.getDuration() / 1000;
        long trackPositionInSec = track.getPosition() / 1000;

        String trackDurationFormatted = trackDurationInSec / 60 + ":" + trackDurationInSec % 60;
        String trackPositionFormatted = trackPositionInSec / 60 + ":" + trackPositionInSec % 60;

        long symbolsToDraw = trackPositionInSec * 50 / trackDurationInSec;

        String result = "";

        for (int i = 0; i < symbolsToDraw; i++) {

            result = result.concat("▒");
        }

        for (int i = 0; i < 50 - symbolsToDraw; i++) {

            result = result.concat("◇");
        }

        return "[".concat(trackPositionFormatted).concat("]").concat(result).concat("[").concat(trackDurationFormatted).concat("]");
    }

    public static void sendMessage(TextChannel channel, String message) {

        sendMessage(channel, message, null);
    }

    /**
     * Send message to text channel
     *
     * @param channel text channel to send message to
     * @param message message content
     * @param commandToRun this runnable will be run until another message is sent in different thread
     */
    public static void sendMessage(TextChannel channel, String message, SentMessageBaseRunnable commandToRun) {

        modifyAndQueue(channel.sendMessage(message), commandToRun, channel.getGuild());
    }

    public static void sendReply(Message messageToReply, String message) {

        sendReply(messageToReply, message, null);
    }

    /**
     * Send reply to message
     *
     * @param messageToReply message to reply
     * @param message message content
     * @param commandToRun this runnable will be run until another message is sent in different thread
     */
    public static void sendReply(Message messageToReply, String message, SentMessageBaseRunnable commandToRun) {

        modifyAndQueue(messageToReply.reply(message), commandToRun, messageToReply.getGuild());
    }

    private static void modifyAndQueue(
            MessageCreateAction messageCreateAction,
            SentMessageBaseRunnable commandToRun,
            Guild guild
    ) {
        messageCreateAction
                .onSuccess(sentMessage -> {

                    BotStateController botStateController = BotStateController.getInstance();

                    LastSentMessageModel lastSentMessage = botStateController.getLastSentMessage();

                    if (lastSentMessage != null && lastSentMessage.getThread() != null) {

                        lastSentMessage.getThread().interrupt();
                    }

                    Thread thread = null;

                    if (commandToRun != null) {

                        commandToRun.setInitialMessage(guild, sentMessage);

                        thread = Thread.ofVirtual().start(commandToRun);
                    }

                    lastSentMessage = LastSentMessageModel.builder()
                            .message(sentMessage)
                            .thread(thread)
                            .runnable(commandToRun)
                            .build();

                    botStateController.setLastSentMessage(lastSentMessage);
                })
                .queue();
    }
}
