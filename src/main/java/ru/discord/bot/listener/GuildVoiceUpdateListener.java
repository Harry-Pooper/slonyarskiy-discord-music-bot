package ru.discord.bot.listener;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import ru.discord.bot.bot.BotStateController;
import ru.discord.bot.audioPlayer.AudioPlayerHandler;
import ru.discord.bot.util.Logger;

public class GuildVoiceUpdateListener extends ListenerAdapter {

    private final Logger log = Logger.getLogger(GuildVoiceUpdateListener.class);

    private final BotStateController bsController;
    private final AudioPlayerHandler playerHandler;

    public GuildVoiceUpdateListener() {

        this.bsController = BotStateController.getInstance();
        this.playerHandler = AudioPlayerHandler.getInstance();
    }

    @Override
    public void onGuildVoiceUpdate(@NotNull GuildVoiceUpdateEvent event) {

        log.info("Received Voice Update: " + event.getMember().getUser().getAsTag());
        log.info("Thread: " + Thread.currentThread().getName());

        Guild guild = event.getGuild();

        if (event.getMember().getIdLong() == event.getJDA().getSelfUser().getIdLong()) {

            var newVoiceChannel = event.getChannelJoined() == null
                    ? null
                    : event.getChannelJoined().asVoiceChannel();

            bsController.updateBotVoiceChannel(guild, newVoiceChannel);
        }

        VoiceChannel voiceChannel = bsController.getBotVoiceChannel(guild);

        if (onlyClankersInChannel(voiceChannel)) {

            playerHandler.getPlaylistHandler(guild).fullStop();
        }
    }

    private boolean onlyClankersInChannel(VoiceChannel voiceChannel) {

        if (voiceChannel == null) {

            return false;
        }

        for (Member member : voiceChannel.getMembers()) {

            if (!member.getUser().isBot()) {

                return false;
            }
        }

        return true;
    }
}
