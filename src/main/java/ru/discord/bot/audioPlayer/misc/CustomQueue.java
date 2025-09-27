package ru.discord.bot.audioPlayer.misc;

import net.dv8tion.jda.api.entities.User;
import ru.discord.bot.model.AudioTrackInfoModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

public class CustomQueue {

    private final List<AudioTrackInfoModel> queue = new ArrayList<>();

    private AudioTrackInfoModel lastRemoved = null;

    /**
     * get and delete first element from queue
     */
    public AudioTrackInfoModel remove() {

        lastRemoved = queue.removeFirst();

        return lastRemoved;
    }

    public AudioTrackInfoModel current() {

        return lastRemoved;
    }

    public void add(AudioTrackInfoModel track) {

        Random random = new Random();
        random.setSeed(System.currentTimeMillis());

        int randomIndex = random.nextInt(queue.size() + 1);

        queue.add(randomIndex, track);

        // TODO - Додоелать (высшая математика)
//        User author = track.getAuthor();
//
//        int idx = 0;
//
//        for (AudioTrackInfoModel i : queue) {
//
//            User trackAuthor = i.getAuthor();
//
//            idx++;
//        }
//
//        int finalIdx = idx == queue.size()
//                ? queue.size()
//                : idx;
//
//        queue.add(finalIdx, track);
    }

    public void forEach(Consumer<? super AudioTrackInfoModel> action) {

        queue.forEach(action);
    }

    public void clear() {

        queue.clear();
        lastRemoved = null;
    }

    public AudioTrackInfoModel getFirst() {

        return queue.getFirst();
    }

    public boolean isEmpty() {

        return queue.isEmpty();
    }
}
