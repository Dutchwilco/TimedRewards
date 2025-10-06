package nl.dutchcoding.timedrewards.placeholders;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import nl.dutchcoding.timedrewards.TimedRewards;
import nl.dutchcoding.timedrewards.models.TimedReward;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TimedRewardsPlaceholder extends PlaceholderExpansion {

    private final TimedRewards plugin;

    public TimedRewardsPlaceholder(TimedRewards plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "timedrewards";
    }

    @Override
    public @NotNull String getAuthor() {
        return "wilcodwg";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        // %timedrewards_<reward-id>_time%
        // %timedrewards_<reward-id>_time_seconds%
        // %timedrewards_<reward-id>_time_minutes%
        // %timedrewards_<reward-id>_name%
        
        if (params.contains("_")) {
            String[] parts = params.split("_");
            if (parts.length >= 2) {
                String rewardId = parts[0];
                TimedReward reward = plugin.getRewardManager().getReward(rewardId);
                
                if (reward == null) {
                    return "Unknown Reward";
                }

                String type = parts[parts.length - 1];
                
                switch (type.toLowerCase()) {
                    case "time":
                        return reward.getFormattedTimeRemaining();
                    case "seconds":
                        return String.valueOf(reward.getRemainingTime());
                    case "minutes":
                        return String.valueOf(reward.getRemainingTime() / 60);
                    case "name":
                        return reward.getName();
                    default:
                        return reward.getFormattedTimeRemaining();
                }
            }
        }

        return null;
    }
}
