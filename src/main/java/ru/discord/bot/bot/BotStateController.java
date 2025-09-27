package ru.discord.bot.bot;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.requests.restaction.MessageCreateAction;
import ru.discord.bot.async.SentMessageBaseRunnable;
import ru.discord.bot.model.LastSentMessageModel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BotStateController {
    
    private final Map<Guild, VoiceChannel> voiceChannelMap =  new ConcurrentHashMap<>();

    @Setter
    @Getter
    private LastSentMessageModel lastSentMessage = null;
    
    public void openConnection(Guild guild, VoiceChannel voiceChannel) {
        
        guild.getAudioManager().openAudioConnection(voiceChannel);
        voiceChannelMap.put(guild, voiceChannel);
    }
    
    public void closeConnection(Guild guild) {
        
        guild.getAudioManager().closeAudioConnection();
        voiceChannelMap.remove(guild);
    }

    public VoiceChannel getBotVoiceChannel(Guild guild) {

        return voiceChannelMap.get(guild);
    }

    private static class Holder {

        private static BotStateController INSTANCE = null;

    }

    public static BotStateController getInstance() {

        if (BotStateController.Holder.INSTANCE == null) {

            BotStateController.Holder.INSTANCE = new BotStateController();
        }

        return BotStateController.Holder.INSTANCE;
    }
}
