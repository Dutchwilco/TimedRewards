package nl.dutchcoding.timedrewards.utils;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ItemBuilder {

    public static ItemStack fromMap(Map<String, Object> map) {
        try {
            String materialName = (String) map.get("material");
            Material material = Material.valueOf(materialName.toUpperCase());
            
            int amount = map.containsKey("amount") ? (int) map.get("amount") : 1;
            
            ItemStack item = new ItemStack(material, amount);
            ItemMeta meta = item.getItemMeta();
            
            if (meta == null) return item;
            
            // Set display name
            if (map.containsKey("name")) {
                String name = (String) map.get("name");
                meta.setDisplayName(name.replace("&", "§"));
            }
            
            // Set lore
            if (map.containsKey("lore")) {
                @SuppressWarnings("unchecked")
                List<String> loreList = (List<String>) map.get("lore");
                List<String> coloredLore = new ArrayList<>();
                for (String line : loreList) {
                    coloredLore.add(line.replace("&", "§"));
                }
                meta.setLore(coloredLore);
            }
            
            // Set custom model data
            if (map.containsKey("custom-model-data")) {
                int customModelData = (int) map.get("custom-model-data");
                if (customModelData > 0) {
                    meta.setCustomModelData(customModelData);
                }
            }
            
            // Add enchantments
            if (map.containsKey("enchantments")) {
                @SuppressWarnings("unchecked")
                List<String> enchantments = (List<String>) map.get("enchantments");
                for (String enchant : enchantments) {
                    String[] parts = enchant.split(":");
                    if (parts.length == 2) {
                        try {
                            Enchantment enchantment = Enchantment.getByName(parts[0].toUpperCase());
                            int level = Integer.parseInt(parts[1]);
                            if (enchantment != null) {
                                meta.addEnchant(enchantment, level, true);
                            }
                        } catch (NumberFormatException e) {
                            // Invalid enchantment format
                        }
                    }
                }
            }
            
            // Add item flags if needed
            if (map.containsKey("item-flags")) {
                @SuppressWarnings("unchecked")
                List<String> flags = (List<String>) map.get("item-flags");
                for (String flag : flags) {
                    try {
                        ItemFlag itemFlag = ItemFlag.valueOf(flag.toUpperCase());
                        meta.addItemFlags(itemFlag);
                    } catch (IllegalArgumentException e) {
                        // Invalid flag
                    }
                }
            }
            
            item.setItemMeta(meta);
            return item;
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
