package nl.dutchcoding.timedrewards.models;

import org.bukkit.inventory.ItemStack;

import java.util.List;

public class TimedReward {

    private final String id;
    private final String name;
    private final int intervalMinutes;
    private final int intervalSeconds;
    private final List<ItemStack> items;
    private final List<String> commands;
    private final boolean enabled;
    private int remainingTime;

    public TimedReward(String id, String name, int intervalMinutes, List<ItemStack> items, List<String> commands, boolean enabled) {
        this.id = id;
        this.name = name;
        this.intervalMinutes = intervalMinutes;
        this.intervalSeconds = intervalMinutes * 60;
        this.items = items;
        this.commands = commands;
        this.enabled = enabled;
        this.remainingTime = intervalSeconds;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name.replace("&", "§");
    }

    public int getIntervalMinutes() {
        return intervalMinutes;
    }

    public int getIntervalSeconds() {
        return intervalSeconds;
    }

    public List<ItemStack> getItems() {
        return items;
    }

    public List<String> getCommands() {
        return commands;
    }

    public boolean hasItems() {
        return items != null && !items.isEmpty();
    }

    public boolean hasCommands() {
        return commands != null && !commands.isEmpty();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public int getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(int remainingTime) {
        this.remainingTime = remainingTime;
    }

    public void decrementTime() {
        if (remainingTime > 0) {
            remainingTime--;
        }
    }

    public void resetTimer() {
        this.remainingTime = intervalSeconds;
    }

    public String getFormattedTimeRemaining() {
        int hours = remainingTime / 3600;
        int minutes = (remainingTime % 3600) / 60;
        int seconds = remainingTime % 60;

        if (hours > 0) {
            return String.format("%dh %dm %ds", hours, minutes, seconds);
        } else if (minutes > 0) {
            return String.format("%dm %ds", minutes, seconds);
        } else {
            return String.format("%ds", seconds);
        }
    }
}
