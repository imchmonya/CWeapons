package org.cWeapons.Weapons.SpiderAxe;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SpiderAxeListener implements Listener {

    private final JavaPlugin plugin;
    private final Map<UUID, Long> cooldowns = new HashMap<>();

    public SpiderAxeListener(JavaPlugin plugin) {
        this.plugin = plugin;
        startCooldownActionBarTask();
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (item == null || !SpiderAxe.isSpiderAxe(item, plugin)) {
            return;
        }

        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (player.isSneaking()) {
                event.setCancelled(true);
                UUID uuid = player.getUniqueId();
                long currentTime = System.currentTimeMillis();
                long cooldownEnd = cooldowns.getOrDefault(uuid, 0L);

                if (currentTime < cooldownEnd) {
                    player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                    return;
                }
                Location loc = player.getLocation();
                int radius1 = plugin.getConfig().getInt("spider_axe.radius", 10);
                int radius = radius1;
                int count = 0;

                for (int x = -radius; x <= radius; x++) {
                    for (int y = -radius; y <= radius; y++) {
                        for (int z = -radius; z <= radius; z++) {
                            Block block = loc.clone().add(x, y, z).getBlock();
                            if (block.getType() == Material.COBWEB) {
                                block.setType(Material.AIR);
                                count++;
                            }
                        }
                    }
                }


                Location actionLoc = player.getLocation();
                for (Player p : actionLoc.getWorld().getPlayers()) {
                    if (p.getLocation().distance(actionLoc) <= 10) {
                        p.playSound(actionLoc, Sound.ENTITY_SLIME_ATTACK, 1.0f, 1.0f);
                    }
                }
                int cooldownSeconds = plugin.getConfig().getInt("spider_axe.cooldown", 30);
                cooldowns.put(uuid, currentTime + (cooldownSeconds * 1000L));
            }
        }
    }

    private void startCooldownActionBarTask() {
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            long currentTime = System.currentTimeMillis();
            for (Player player : Bukkit.getOnlinePlayers()) {
                ItemStack item = player.getInventory().getItemInMainHand();

                if (SpiderAxe.isSpiderAxe(item, plugin)) {
                    UUID uuid = player.getUniqueId();
                    long cooldownEnd = cooldowns.getOrDefault(uuid, 0L);

                    if (currentTime < cooldownEnd) {
                        long remainingSeconds = Math.max(0, (cooldownEnd - currentTime) / 1000);
                        player.sendActionBar("§cЗадержка: " + remainingSeconds + "с");
                    }
                }
            }
        }, 0L, 20L);
    }
}