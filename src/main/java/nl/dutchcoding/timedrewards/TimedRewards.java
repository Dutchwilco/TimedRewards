package nl.dutchcoding.timedrewards;

import nl.dutchcoding.timedrewards.commands.TimedRewardsCommand;
import nl.dutchcoding.timedrewards.managers.ConfigManager;
import nl.dutchcoding.timedrewards.managers.RewardManager;
import nl.dutchcoding.timedrewards.placeholders.TimedRewardsPlaceholder;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class TimedRewards extends JavaPlugin {

    private static TimedRewards instance;
    private ConfigManager configManager;
    private RewardManager rewardManager;

    @Override
    public void onEnable() {
        instance = this;

        // Initialize managers
        configManager = new ConfigManager(this);
        configManager.loadConfigs();

        rewardManager = new RewardManager(this);
        rewardManager.startTimers();

        // Register commands
        getCommand("timedrewards").setExecutor(new TimedRewardsCommand(this));

        // Register PlaceholderAPI expansion
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new TimedRewardsPlaceholder(this).register();
            getLogger().info("PlaceholderAPI hook registered!");
        }

        getLogger().info("TimedRewards has been enabled!");
    }

    @Override
    public void onDisable() {
        if (rewardManager != null) {
            rewardManager.stopTimers();
        }
        getLogger().info("TimedRewards has been disabled!");
    }

    public static TimedRewards getInstance() {
        return instance;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public RewardManager getRewardManager() {
        return rewardManager;
    }
}
