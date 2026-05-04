package org.cWeapons.Weapons.SpiderAxe;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

public class SpiderAxe {

    public static ItemStack createItem(JavaPlugin plugin) {
        ItemStack item = new ItemStack(Material.NETHERITE_AXE);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.GOLD + "Паучий топор");

            NamespacedKey key = new NamespacedKey(plugin, "legendary_weapon");
            meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, "spider_axe");

            item.setItemMeta(meta);
        }
        return item;
    }

    public static boolean isSpiderAxe(ItemStack item, JavaPlugin plugin) {
        if (item == null || item.getType() == Material.AIR || !item.hasItemMeta()) {
            return false;
        }
        NamespacedKey key = new NamespacedKey(plugin, "legendary_weapon");
        return item.getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.STRING);
    }
}