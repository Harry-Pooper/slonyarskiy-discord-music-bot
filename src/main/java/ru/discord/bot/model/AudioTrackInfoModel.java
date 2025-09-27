package ru.discord.bot.model;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import lombok.*;
import net.dv8tion.jda.api.entities.User;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AudioTrackInfoModel {

    private AudioTrack audioTrack;

    private User author;
}
