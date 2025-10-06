package nl.dutchcoding.timedrewards.commands;

import nl.dutchcoding.timedrewards.TimedRewards;
import nl.dutchcoding.timedrewards.models.TimedReward;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TimedRewardsCommand implements CommandExecutor, TabCompleter {

    private final TimedRewards plugin;

    public TimedRewardsCommand(TimedRewards plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("timedrewards.admin")) {
            sender.sendMessage(plugin.getConfigManager().getMessage("no-permission"));
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(plugin.getConfigManager().getMessage("invalid-usage"));
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "reload":
                plugin.getConfigManager().reloadConfigs();
                plugin.getRewardManager().startTimers();
                sender.sendMessage(plugin.getConfigManager().getMessage("reload-success"));
                break;

            case "list":
                listRewards(sender);
                break;

            case "pause":
                plugin.getRewardManager().setPaused(true);
                sender.sendMessage(plugin.getConfigManager().getMessage("timer-paused"));
                break;

            case "resume":
                plugin.getRewardManager().setPaused(false);
                sender.sendMessage(plugin.getConfigManager().getMessage("timer-resumed"));
                break;

            default:
                sender.sendMessage(plugin.getConfigManager().getMessage("invalid-usage"));
                break;
        }

        return true;
    }

    private void listRewards(CommandSender sender) {
        if (plugin.getRewardManager().getRewards().isEmpty()) {
            sender.sendMessage(plugin.getConfigManager().getMessage("no-rewards"));
            return;
        }

        sender.sendMessage(plugin.getConfigManager().getMessage("list-header"));
        
        for (TimedReward reward : plugin.getRewardManager().getRewards().values()) {
            if (!reward.isEnabled()) continue;
            
            String entry = plugin.getConfigManager().getMessage("list-entry")
                    .replace("{reward_name}", reward.getName())
                    .replace("{time_remaining}", reward.getFormattedTimeRemaining());
            
            if (plugin.getRewardManager().isPaused()) {
                entry += " " + plugin.getConfigManager().getMessage("list-paused");
            }
            
            sender.sendMessage(entry);
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!sender.hasPermission("timedrewards.admin")) {
            return new ArrayList<>();
        }

        if (args.length == 1) {
            return Arrays.asList("reload", "list", "pause", "resume");
        }

        return new ArrayList<>();
    }
}
