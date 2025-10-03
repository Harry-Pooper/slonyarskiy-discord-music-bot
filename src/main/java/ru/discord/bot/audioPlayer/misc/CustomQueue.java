package ru.discord.bot.audioPlayer.misc;

import net.dv8tion.jda.api.entities.User;
import ru.discord.bot.model.AudioTrackInfoModel;
import ru.discord.bot.util.ArrayListExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

public class CustomQueue {

    private final ArrayListExtension<AudioTrackInfoModel> queue = new ArrayListExtension<>();

    private AudioTrackInfoModel lastRemoved = null;

    /**
     * get and delete first element from queue
     */
    public AudioTrackInfoModel remove() {

        lastRemoved = queue.removeFirst();

        return lastRemoved;
    }

    public void remove(int removeCount) {

        if (removeCount > queue.size()) {

            removeCount = queue.size();
        }

        queue.removeIndexes(0, removeCount);
    }

    public AudioTrackInfoModel current() {

        return lastRemoved;
    }

    public void add(AudioTrackInfoModel track) {

        Random random = new Random();
        random.setSeed(System.currentTimeMillis());

        int randomIndex = random.nextInt(queue.size() + 1);

        queue.add(randomIndex, track);
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
