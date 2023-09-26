package me.elmosahoe.mobslayerplugin;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class MobSlayerPlugin extends JavaPlugin {

    private ConfigurationManager configManager;
    private TaskTracker taskTracker;
    private Economy economy;

    @Override
    public void onEnable() {
        // Initialize the plugin
        configManager = new ConfigurationManager(this);

        // Initialize Vault economy
        if (getServer().getPluginManager().getPlugin("Vault") != null) {
            economy = getServer().getServicesManager().getRegistration(Economy.class).getProvider();
        }

        // Initialize the task tracker with the economy instance
        taskTracker = new TaskTracker(economy);

        // Register event listeners
        getServer().getPluginManager().registerEvents(new EventListener(configManager, taskTracker), this);

        // Register the /task command
        getCommand("task").setExecutor(new TaskCommand(configManager, taskTracker));

        // Register the /taskcheck command
        getCommand("taskcheck").setExecutor(new CommandExecutor() {
            @Override
            public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
                if (args.length >= 1 && args[0].equalsIgnoreCase("check")) {
                    if (sender instanceof Player) {
                        Player player = (Player) sender;
                        Task playerTask = taskTracker.getPlayerTask(player.getUniqueId());
                        if (playerTask != null) {
                            player.sendMessage("Task progress: " + playerTask.getCurrentAmount() + "/" + playerTask.getTargetAmount());
                        } else {
                            player.sendMessage("You don't have an active task.");
                        }
                    } else {
                        sender.sendMessage("This command can only be executed by players.");
                    }
                } else {
                    sender.sendMessage("Usage: /taskcheck check");
                }
                return true;
            }
        });
    }

    public ConfigurationManager getConfigManager() {
        return configManager;
    }

    public TaskTracker getTaskTracker() {
        return taskTracker;
    }
}