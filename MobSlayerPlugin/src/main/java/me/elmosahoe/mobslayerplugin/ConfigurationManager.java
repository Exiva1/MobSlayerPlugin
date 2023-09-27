package me.elmosahoe.mobslayerplugin;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.util.*;

public class ConfigurationManager {

    private final Plugin plugin;
    private FileConfiguration config;

    public ConfigurationManager(Plugin plugin) {
        this.plugin = plugin;
        // Load and save default config
        plugin.saveDefaultConfig();
        config = plugin.getConfig();
    }

    public Map<String, Object> getMobsForDifficulty(DifficultyLevel difficulty) {
        ConfigurationSection section = config.getConfigurationSection(difficulty.name().toLowerCase());

        if (section == null) {
            // Log a warning about the missing section
            Bukkit.getLogger().warning("Configuration section for difficulty '" + difficulty.name() + "' not found!");
            return Collections.emptyMap();
        }

        return section.getValues(false);
    }

    public Reward getRewardForDifficulty(DifficultyLevel difficulty) {
        ConfigurationSection rewardSection = config.getConfigurationSection("rewards." + difficulty.name().toLowerCase());

        if (rewardSection != null) {
            int xp = rewardSection.getInt("xp", 0);
            int money = rewardSection.getInt("money", 0);
            List<String> itemsList = rewardSection.getStringList("items");

            // Randomly select an item from the list
            String randomItem = itemsList.get(new Random().nextInt(itemsList.size()));

            return new Reward(xp, money, Collections.singletonList(randomItem));
        }

        return null;
    }
}