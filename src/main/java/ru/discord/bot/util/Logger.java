package ru.discord.bot.util;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Logger {

    private final static DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss:SSSS");
    private final static String LOG_STRING_TEMPLATE = "%s [%s][%s] - %s";

    private final String className;

    public Logger(Class<?> clazz) {

        this.className = clazz.getName();
    }

    public static Logger getLogger(Class<?> clazz) {

        return new Logger(clazz);
    }

    public void info(String message, Object... args) {

        log(Level.INFO, message, args);
    }

    public void warn(String message, Object... args) {

        log(Level.WARN, message, args);
    }

    public void error(String message, Object... args) {

        log(Level.ERROR, message, args);
    }

    private void log(Level level, String message, Object... args) {

        message = message.replaceAll("\\{}", "%s");

        List<String> argsList = new ArrayList<>();

        if (args != null) {

            for (Object arg : args) {

                if (arg != null) {

                    argsList.add(arg.toString());

                    continue;
                }

                argsList.add("null");
            }
        }

        String formattedString = String.format(message, argsList.toArray());
        String currentDt = DATE_FORMAT.format(OffsetDateTime.now());

        String logString = String.format(LOG_STRING_TEMPLATE, currentDt, level.toString(), className, formattedString);

        System.out.println(logString);
    }

    public enum Level {
        INFO,
        WARN,
        ERROR
    }
}
