package org.cWeapons;

import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.cWeapons.Weapons.MaceUpdater.MaceUpdater;
import org.cWeapons.Weapons.SpiderAxe.SpiderAxe;
public class glowning implements Listener {
    private final JavaPlugin plugin;
    public glowning(JavaPlugin plugin) {
        this.plugin = plugin;
    }
    @EventHandler
    public void onItemSpawn(ItemSpawnEvent event) {
        Item itemEntity = event.getEntity();
        ItemStack itemStack = itemEntity.getItemStack();
        if (SpiderAxe.isSpiderAxe(itemStack, plugin)) {
            itemEntity.setGlowing(true);
        }
        if (MaceUpdater.isMace(itemStack, plugin)) {
            itemEntity.setGlowing(true);
        }
    }

}