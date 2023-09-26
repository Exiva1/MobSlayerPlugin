package me.elmosahoe.mobslayerplugin;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemUtil {

    public static ItemStack parseItemStack(String itemString) {
        String[] parts = itemString.split(" ");
        if (parts.length > 0) {
            String materialName = parts[0].toUpperCase();
            Material material = Material.getMaterial(materialName);
            if (material != null) {
                ItemStack itemStack = new ItemStack(material);

                if (parts.length > 1) {
                    int amount = 1;
                    try {
                        amount = Integer.parseInt(parts[1]);
                    } catch (NumberFormatException ignored) {
                    }
                    itemStack.setAmount(amount);
                }

                if (parts.length > 2) {
                    ItemMeta itemMeta = itemStack.getItemMeta();
                    itemMeta.setDisplayName(parts[2]);
                    itemStack.setItemMeta(itemMeta);
                }

                return itemStack;
            }
        }
        return null;
    }
}