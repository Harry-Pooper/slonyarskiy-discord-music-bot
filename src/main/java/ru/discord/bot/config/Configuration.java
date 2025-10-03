package ru.discord.bot.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.discord.bot.model.ConfigurationModel;
import ru.discord.bot.util.Logger;

import java.io.*;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Configuration {

    private static Logger log = Logger.getLogger(Configuration.class);

    private static Configuration INSTANCE;

    private final ObjectMapper mapper = new ObjectMapper();

    private final String configFileName;

    private ConfigurationModel cfgModel = null;

    public Configuration(String configFileName) {
        this.configFileName = configFileName;
    }

    public void loadFromConfigFile() {

        File configFile = new File(configFileName);

        createFileIfNotExists(configFile);

        try (FileInputStream fis = new FileInputStream(configFile)) {

            byte[] data = fis.readAllBytes();

            try {

                cfgModel = mapper.readValue(data, ConfigurationModel.class);
            } catch (Exception e) {

                log.warn("Could not load configuration from file, creating new configuration. " +
                        "All previous configurations are not saved.");

                cfgModel = new ConfigurationModel();
            }
        } catch (Exception e) {

            throw new IllegalStateException(
                    "Failed to load config file: " + configFileName + "\n Error: " + e.getMessage()
            );
        }
    }

    public void updateConfigFile() {

        File configFile = new File(configFileName);

        createFileIfNotExists(configFile);

        try (FileOutputStream fos = new FileOutputStream(configFile)) {

            byte[] data = mapper.writeValueAsBytes(cfgModel);

            fos.write(data);

            fos.flush();
        } catch (Exception e) {

            throw new IllegalStateException(
                    "Failed to update config file: " + configFileName + "\n Error: " + e.getMessage()
            );
        }
    }

    private void createFileIfNotExists(File file) {

        if (Files.exists(file.toPath())) {

            return;
        }

        log.info("Config file does not exist, creating new");

        try {

            createFile(file, false);
        } catch (IOException e) {

            throw new IllegalStateException("Could not create config file. Error: " + e.getMessage());
        }
    }

    public Map<String, Set<String>> getRoles() {

        return cfgModel.getRoles();
    }

    public void updateRoles(Map<String, Set<String>> newRoles) {

        cfgModel.setRoles(newRoles);

        updateConfigFile();
    }

    private boolean createFile(File file, boolean dir) throws IOException {

        File parent = file.getParentFile();

        if (parent != null && !Files.exists(parent.toPath())) {

            createFile(parent, true);
        }

        if (dir) {

            return file.mkdir();
        }

        return file.createNewFile();
    }

    public static Configuration getInstance() {

        return INSTANCE;
    }

    public static void setInstance(Configuration config) {

        INSTANCE = config;
    }
}
