package me.elmosahoe.mobslayerplugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public class TaskCommand implements CommandExecutor {

    private final ConfigurationManager configManager;
    private final TaskTracker taskTracker;

    public TaskCommand(ConfigurationManager configManager, TaskTracker taskTracker) {
        this.configManager = configManager;
        this.taskTracker = taskTracker;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (args.length < 1) {
                // Show usage message
                player.sendMessage("Usage: /task <difficulty>");
                return true;
            }

            if (args[0].equalsIgnoreCase("check")) {
                if (taskTracker.hasPlayerTask(player.getUniqueId())) {
                    Task playerTask = taskTracker.getPlayerTask(player.getUniqueId());
                    player.sendMessage("Task progress: " + playerTask.getCurrentAmount() + "/" + playerTask.getTargetAmount());
                } else {
                    player.sendMessage("You don't have an active task.");
                }
                return true;
            }

            if (taskTracker.hasPlayerTask(player.getUniqueId())) {
                player.sendMessage("You already have a task. You can only have 1 task at a time.");
                return true;
            }

            String difficultyStr = args[0].toUpperCase();

            try {
                DifficultyLevel difficulty = DifficultyLevel.valueOf(difficultyStr);
                Map<String, Object> mobsForDifficulty = configManager.getMobsForDifficulty(difficulty);

                if (mobsForDifficulty.isEmpty()) {
                    player.sendMessage("No tasks available for " + difficulty + " difficulty.");
                } else {
                    // Convert the map values to a list of maps
                    List<Map<?, ?>> mobList = mobsForDifficulty.values().stream().map(obj -> (Map<?, ?>) obj).collect(Collectors.toList());
                    Map<?, ?> selectedMobMap = mobList.get(new Random().nextInt(mobList.size()));
                    String mobType = (String) selectedMobMap.get("mob");
                    int min = (int) selectedMobMap.get("min");
                    int max = (int) selectedMobMap.get("max");
                    int targetAmount = new Random().nextInt(max - min + 1) + min;

                    // Get the reward for the chosen difficulty
                    Reward reward = configManager.getRewardForDifficulty(difficulty);

                    // Assign the task to the player with the associated reward
                    taskTracker.assignTask(player.getUniqueId(), mobType, targetAmount, reward);

                    player.sendMessage("You've been assigned a task: Kill " + targetAmount + " " + mobType + ".");
                }
            } catch (IllegalArgumentException e) {
                player.sendMessage("Invalid difficulty level.");
            }
        } else if (args.length >= 1 && args[0].equalsIgnoreCase("easy")) {
            // Handle task assignment for console or other non-player sources here
            // Make sure to tailor this part to your requirements
        } else {
            sender.sendMessage("This command can only be executed by players.");
        }

        return true;
    }
}
