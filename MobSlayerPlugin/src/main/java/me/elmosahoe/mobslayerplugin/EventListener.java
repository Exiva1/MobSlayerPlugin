package me.elmosahoe.mobslayerplugin;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.Map;

public class EventListener implements Listener {

    private final ConfigurationManager configManager;
    private final TaskTracker taskTracker;

    public EventListener(ConfigurationManager configManager, TaskTracker taskTracker) {
        this.configManager = configManager;
        this.taskTracker = taskTracker;
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity().getType() == EntityType.PLAYER) {
            return; // Ignore player deaths
        }

        // Get the killer
        if (event.getEntity().getKiller() != null) {
            Player player = event.getEntity().getKiller();

            // Check if the player has an active task
            if (taskTracker.hasPlayerTask(player.getUniqueId())) {
                String mobType = event.getEntityType().toString(); // Get the mob type

                // Check if the mobType exists in the configuration
                for (DifficultyLevel difficulty : DifficultyLevel.values()) {
                    Map<String, Object> mobs = configManager.getMobsForDifficulty(difficulty);
                    if (mobs.containsKey(mobType)) {
                        taskTracker.updatePlayerProgress(player.getUniqueId());

                        Task playerTask = taskTracker.getPlayerTask(player.getUniqueId());
                        if (playerTask.isComplete()) {
                            player.sendMessage("You've completed your task!");
                            taskTracker.completePlayerTask(player.getUniqueId());
                        } else {
                            player.sendMessage("Task progress: " + playerTask.getCurrentAmount() + "/" + playerTask.getTargetAmount());
                        }
                        break;
                    }
                }
            }
        }
    }
}