package ru.discord.bot.util;

import java.util.ArrayList;

public class ArrayListExtension<E> extends ArrayList<E> {

    // Работает так же как и subList(...).clear() на синтетических тестах, но чувствую я себя так босяво
    public void removeIndexes(int startIdx, int endIdx) {

        removeRange(startIdx, endIdx);
    }
}
