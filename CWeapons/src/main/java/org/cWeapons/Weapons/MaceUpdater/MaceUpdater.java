package org.cWeapons.Weapons.MaceUpdater;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.cWeapons.CWeapons;

public class MaceUpdater {

    private final CWeapons plugin;
    private final NamespacedKey levelKey;

    public MaceUpdater(CWeapons plugin) {
        this.plugin = plugin;
        this.levelKey = new NamespacedKey(plugin, "mace_level");
    }

    public static boolean isMace(ItemStack item, JavaPlugin plugin) {
        if (item == null || item.getType() == Material.AIR || !item.hasItemMeta()) {
            return false;
        }
        NamespacedKey key = new NamespacedKey(plugin, "mace_level");
        return item.getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.INTEGER);
    }

    public ItemStack createMace() {
        ItemStack mace = new ItemStack(Material.MACE);
        ItemMeta meta = mace.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.GOLD + "Булава" + ChatColor.GRAY + " [1 LvL]");
            meta.addEnchant(Enchantment.WIND_BURST, 1, true);
            meta.addEnchant(Enchantment.DENSITY, 3, true);
            meta.setUnbreakable(true);
            meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ENCHANTS);

            meta.getPersistentDataContainer().set(levelKey, PersistentDataType.INTEGER, 1);

            mace.setItemMeta(meta);
        }
        return mace;
    }

    public int getLevel(ItemStack item) {
        if (item != null && item.hasItemMeta()) {
            if (item.getItemMeta().getPersistentDataContainer().has(levelKey, PersistentDataType.INTEGER)) {
                return item.getItemMeta().getPersistentDataContainer().get(levelKey, PersistentDataType.INTEGER);
            }
        }
        return 1;
    }

    public int getRequiredPoints(int level) {
        if (level == 2) return 150;
        if (level == 3) return 250;
        return 99999;
    }

    public void upgradeMace(ItemStack item, int level) {
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            for (Enchantment ench : meta.getEnchants().keySet()) {
                meta.removeEnchant(ench);
            }
            if (level == 2) {
                meta.addEnchant(Enchantment.WIND_BURST, 1, true);
                meta.addEnchant(Enchantment.DENSITY, 4, true);
                meta.setDisplayName(ChatColor.GOLD + "Булава" + ChatColor.GRAY + " [2 LvL]");
            } else if (level == 3) {
                meta.addEnchant(Enchantment.WIND_BURST, 1, true);
                meta.addEnchant(Enchantment.DENSITY, 5, true);
                meta.setDisplayName(ChatColor.GOLD + "Булава" + ChatColor.GRAY + " [3 LvL]");
            }

            meta.getPersistentDataContainer().set(levelKey, PersistentDataType.INTEGER, level);
            item.setItemMeta(meta);
        }
    }
}