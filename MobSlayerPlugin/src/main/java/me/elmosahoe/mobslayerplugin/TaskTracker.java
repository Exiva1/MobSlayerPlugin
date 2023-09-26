package me.elmosahoe.mobslayerplugin;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TaskTracker {

    private final Map<UUID, Task> playerTasks;
    private final Economy economy;

    public TaskTracker(Economy economy) {
        playerTasks = new HashMap<>();
        this.economy = economy;
    }

    public void assignTask(UUID playerUUID, String mobType, int targetAmount, Reward reward) {
        if (!hasPlayerTask(playerUUID)) {
            playerTasks.put(playerUUID, new Task(mobType, targetAmount, reward));
        }
    }

    public Task getPlayerTask(UUID playerUUID) {
        return playerTasks.get(playerUUID);
    }

    public boolean hasPlayerTask(UUID playerUUID) {
        return playerTasks.containsKey(playerUUID);
    }

    public void updatePlayerProgress(UUID playerUUID) {
        Task task = playerTasks.get(playerUUID);
        if (task != null) {
            task.incrementProgress();
        }
    }

    public void completePlayerTask(UUID playerUUID) {
        Task task = playerTasks.remove(playerUUID);
        if (task != null) {
            Reward reward = task.getReward();

            int xpReward = reward.getXp();
            int moneyReward = reward.getMoney();
            List<String> itemsReward = reward.getItems();

            Player player = Bukkit.getPlayer(playerUUID);
            if (player != null) {
                player.sendMessage("Congratulations! You've completed the task and received the following rewards:");
                player.sendMessage("XP: " + xpReward);
                player.sendMessage("Money: " + moneyReward);
                player.sendMessage("Items: " + itemsReward);

                // Give the rewards to the player
                player.giveExp(xpReward);

                if (economy != null && moneyReward > 0) {
                    economy.depositPlayer(player, moneyReward);
                }

                for (String item : itemsReward) {
                    ItemStack itemStack = ItemUtil.parseItemStack(item);
                    if (itemStack != null) {
                        player.getInventory().addItem(itemStack);
                    }
                }
            }
        }
    }

    public void checkPlayerTaskProgress(UUID playerUUID) {
        Task task = playerTasks.get(playerUUID);
        if (task != null) {
            Player player = Bukkit.getPlayer(playerUUID);
            if (player != null) {
                player.sendMessage("Your task progress: " + task.getCurrentAmount() + "/" + task.getTargetAmount());
            }
        }
    }
}