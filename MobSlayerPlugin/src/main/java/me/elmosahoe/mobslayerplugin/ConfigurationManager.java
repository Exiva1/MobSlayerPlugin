package me.elmosahoe.mobslayerplugin;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.Map;

public class ConfigurationManager {

    private final Plugin plugin;
    private FileConfiguration config;

    public ConfigurationManager(Plugin plugin) {
        this.plugin = plugin;
        // Load and save default config
        plugin.saveDefaultConfig();
        config = plugin.getConfig();
    }

    public List<Map<?, ?>> getMobsForDifficulty(DifficultyLevel difficulty) {
        return config.getMapList(difficulty.name().toLowerCase());
    }

    public Reward getRewardForDifficulty(DifficultyLevel difficulty) {
        ConfigurationSection rewardSection = config.getConfigurationSection("rewards." + difficulty.name().toLowerCase());

        if (rewardSection != null) {
            int xp = rewardSection.getInt("xp", 0);
            int money = rewardSection.getInt("money", 0);
            List<String> items = rewardSection.getStringList("items");
            return new Reward(xp, money, items);
        }

        return null;
    }
}