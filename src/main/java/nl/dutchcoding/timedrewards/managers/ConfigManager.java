package nl.dutchcoding.timedrewards.managers;

import nl.dutchcoding.timedrewards.TimedRewards;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigManager {

    private final TimedRewards plugin;
    private FileConfiguration config;
    private FileConfiguration messages;
    private FileConfiguration items;

    public ConfigManager(TimedRewards plugin) {
        this.plugin = plugin;
    }

    public void loadConfigs() {
        // Save default configs if they don't exist
        plugin.saveDefaultConfig();
        saveDefaultConfig("messages.yml");
        saveDefaultConfig("items.yml");

        // Load configurations
        config = plugin.getConfig();
        messages = loadConfig("messages.yml");
        items = loadConfig("items.yml");
    }

    public void reloadConfigs() {
        plugin.reloadConfig();
        config = plugin.getConfig();
        messages = loadConfig("messages.yml");
        items = loadConfig("items.yml");
    }

    private void saveDefaultConfig(String fileName) {
        File file = new File(plugin.getDataFolder(), fileName);
        if (!file.exists()) {
            plugin.saveResource(fileName, false);
        }
    }

    private FileConfiguration loadConfig(String fileName) {
        File file = new File(plugin.getDataFolder(), fileName);
        return YamlConfiguration.loadConfiguration(file);
    }

    public String getMessage(String path) {
        String message = messages.getString(path);
        if (message == null) return "&cMessage not found: " + path;
        
        String prefix = config.getString("prefix", "&8[&6TimedRewards&8]&r");
        return message.replace("{prefix}", prefix).replace("&", "§");
    }

    public String getPrefix() {
        return config.getString("prefix", "&8[&6TimedRewards&8]&r").replace("&", "§");
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public FileConfiguration getMessages() {
        return messages;
    }

    public FileConfiguration getItems() {
        return items;
    }
}
