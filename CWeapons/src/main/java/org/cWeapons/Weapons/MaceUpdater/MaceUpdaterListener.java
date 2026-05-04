package org.cWeapons.Weapons.MaceUpdater;

import org.black_ixx.playerpoints.libs.rosegarden.utils.HexUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.cWeapons.CWeapons;
import java.util.Arrays;

public class MaceUpdaterListener implements Listener {

    private final CWeapons plugin;

    public MaceUpdaterListener(CWeapons plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (player.isSneaking()) {
                ItemStack item = player.getInventory().getItemInMainHand();
                if (item != null && item.getType() == Material.MACE) {
                    event.setCancelled(true);
                    openMenu(player, item);
                }
            }
        }
    }

    private void openMenu(Player player, ItemStack item) {
        Inventory inventory = Bukkit.createInventory(null, 27, ChatColor.DARK_BLUE + "Прокачка Булавы");
        MaceUpdater maceUpdater = plugin.getMaceUpdater();

        ItemStack glass = new ItemStack(Material.MACE);
        ItemMeta meta = glass.getItemMeta();
        if (meta != null) {
            int level = maceUpdater.getLevel(item);
            int nextLevel = level + 1;

            if (nextLevel <= 3) {
                int requiredPoints = maceUpdater.getRequiredPoints(nextLevel);
                meta.setDisplayName(ChatColor.WHITE + "Хотите ли вы прокачать " + ChatColor.BLUE + "Булаву?");
                meta.setLore(Arrays.asList(
                        ChatColor.WHITE + "",
                        ChatColor.WHITE + "Следующий LvL: " + ChatColor.BLUE + nextLevel,
                        ChatColor.WHITE + "Нужно монет: " + ChatColor.BLUE + requiredPoints
                ));
            } else {
                meta.setDisplayName(ChatColor.WHITE + "Булава максимального уровня!");
                meta.setLore(Arrays.asList(
                ));
            }
            glass.setItemMeta(meta);
        }

        inventory.setItem(13, glass);
        player.openInventory(inventory);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals("Прокачка Булавы")) {
            event.setCancelled(true);
            if (event.getCurrentItem() != null && event.getCurrentItem().getType() == Material.LIME_STAINED_GLASS_PANE) {
                Player player = (Player) event.getWhoClicked();
                ItemStack mace = player.getInventory().getItemInMainHand();

                if (mace != null && mace.getType() == Material.MACE) {
                    MaceUpdater maceUpdater = plugin.getMaceUpdater();
                    int currentLevel = maceUpdater.getLevel(mace);
                    int nextLevel = currentLevel + 1;

                    if (nextLevel > 3) {
                        player.closeInventory();
                        return;
                    }

                    int requiredPoints = maceUpdater.getRequiredPoints(nextLevel);
                    int playerPoints = plugin.getPlayerPointsAPI().look(player.getUniqueId());

                    if (playerPoints >= requiredPoints) {
                        plugin.getPlayerPointsAPI().take(player.getUniqueId(), requiredPoints);
                        maceUpdater.upgradeMace(mace, nextLevel);

                        player.getInventory().setItemInMainHand(mace);
                        player.closeInventory();
                    } else {
                        player.closeInventory();
                    }
                } else {
                    player.closeInventory();
                }

            }
        }

    }
}