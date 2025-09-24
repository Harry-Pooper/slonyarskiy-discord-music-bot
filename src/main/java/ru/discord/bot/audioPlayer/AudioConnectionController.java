package ru.discord.bot.audioPlayer;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AudioConnectionController {
    
    private final Map<Guild, VoiceChannel> voiceChannelMap =  new ConcurrentHashMap<>();
    
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

        private static AudioConnectionController INSTANCE;

    }

    public static AudioConnectionController getInstance() {

        if (AudioConnectionController.Holder.INSTANCE == null) {

            AudioConnectionController.Holder.INSTANCE = new AudioConnectionController();

            return AudioConnectionController.Holder.INSTANCE;
        }

        return AudioConnectionController.Holder.INSTANCE;
    }
}
