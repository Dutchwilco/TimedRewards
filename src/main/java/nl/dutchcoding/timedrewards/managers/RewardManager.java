package nl.dutchcoding.timedrewards.managers;

import nl.dutchcoding.timedrewards.TimedRewards;
import nl.dutchcoding.timedrewards.models.TimedReward;
import nl.dutchcoding.timedrewards.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class RewardManager {

    private final TimedRewards plugin;
    private final Map<String, TimedReward> rewards;
    private final Map<String, BukkitTask> tasks;
    private boolean paused = false;

    public RewardManager(TimedRewards plugin) {
        this.plugin = plugin;
        this.rewards = new HashMap<>();
        this.tasks = new HashMap<>();
    }

    public void startTimers() {
        stopTimers();
        loadRewards();

        for (TimedReward reward : rewards.values()) {
            if (!reward.isEnabled()) continue;

            BukkitTask task = new BukkitRunnable() {
                @Override
                public void run() {
                    if (paused) return;
                    
                    if (reward.getRemainingTime() <= 0) {
                        giveReward(reward);
                        reward.resetTimer();
                    } else {
                        reward.decrementTime();
                    }
                }
            }.runTaskTimer(plugin, 20L, 20L); // Run every second

            tasks.put(reward.getId(), task);
        }
    }

    public void stopTimers() {
        for (BukkitTask task : tasks.values()) {
            task.cancel();
        }
        tasks.clear();
    }

    private void loadRewards() {
        rewards.clear();
        ConfigurationSection itemsSection = plugin.getConfigManager().getItems();

        if (itemsSection == null) return;

        for (String key : itemsSection.getKeys(false)) {
            ConfigurationSection rewardSection = itemsSection.getConfigurationSection(key);
            if (rewardSection == null) continue;

            boolean enabled = rewardSection.getBoolean("enabled", true);
            String name = rewardSection.getString("name", key);
            int intervalMinutes = rewardSection.getInt("interval", 60);

            List<ItemStack> items = new ArrayList<>();
            if (rewardSection.contains("items")) {
                for (Object obj : rewardSection.getList("items", new ArrayList<>())) {
                    if (obj instanceof Map) {
                        @SuppressWarnings("unchecked")
                        Map<String, Object> itemMap = (Map<String, Object>) obj;
                        ItemStack item = ItemBuilder.fromMap(itemMap);
                        if (item != null) {
                            items.add(item);
                        }
                    }
                }
            }

            List<String> commands = rewardSection.getStringList("commands");

            TimedReward reward = new TimedReward(key, name, intervalMinutes, items, commands, enabled);
            rewards.put(key, reward);
        }
    }

    private void giveReward(TimedReward reward) {
        Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();
        
        if (onlinePlayers.isEmpty()) return;

        for (Player player : onlinePlayers) {
            // Give items if reward has items
            if (reward.hasItems()) {
                for (ItemStack item : reward.getItems()) {
                    player.getInventory().addItem(item.clone());
                }
            }

            // Execute commands if reward has commands
            if (reward.hasCommands()) {
                for (String command : reward.getCommands()) {
                    String processedCommand = command.replace("%player%", player.getName());
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), processedCommand);
                }
            }

            // Send message to player
            String message = plugin.getConfigManager().getMessage("reward-received");
            player.sendMessage(message);

            // Play sound
            if (plugin.getConfig().getBoolean("play-sound.enabled", true)) {
                try {
                    String soundName = plugin.getConfig().getString("play-sound.sound", "ENTITY_PLAYER_LEVELUP");
                    Sound sound = Sound.valueOf(soundName);
                    float volume = (float) plugin.getConfig().getDouble("play-sound.volume", 1.0);
                    float pitch = (float) plugin.getConfig().getDouble("play-sound.pitch", 1.0);
                    player.playSound(player.getLocation(), sound, volume, pitch);
                } catch (IllegalArgumentException e) {
                    plugin.getLogger().warning("Invalid sound name in config!");
                }
            }
        }

        // Broadcast message
        if (plugin.getConfig().getBoolean("broadcast-reward", true)) {
            String broadcast = plugin.getConfigManager().getMessage("reward-broadcast")
                    .replace("{reward_name}", reward.getName());
            Bukkit.broadcastMessage(broadcast);
        }
    }

    public Map<String, TimedReward> getRewards() {
        return rewards;
    }

    public TimedReward getReward(String id) {
        return rewards.get(id);
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }
}
