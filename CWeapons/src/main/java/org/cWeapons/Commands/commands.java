package org.cWeapons.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.cWeapons.CWeapons;
import org.cWeapons.Weapons.SpiderAxe.SpiderAxe;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class commands implements CommandExecutor, TabCompleter {

    private final JavaPlugin plugin;

    public commands(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("legendaryweapons.admin")) {
                return true;
            }
            plugin.reloadConfig();
            sender.sendMessage("Успешно");
            return true;
        }
        if (args.length == 3 && args[0].equalsIgnoreCase("give")) {
            String weaponName = args[1];
            String targetPlayerName = args[2];
            Player target = Bukkit.getPlayer(targetPlayerName);
            if (target == null) {
                return true;
            }

            if (weaponName.equalsIgnoreCase("spider_axe")) {
                ItemStack item = SpiderAxe.createItem(plugin);
                target.getInventory().addItem(item);
                return true;
            } else if (weaponName.equalsIgnoreCase("mace_updater")) {
                if (plugin instanceof CWeapons) {
                    ItemStack maceItem = ((CWeapons) plugin).getMaceUpdater().createMace();
                    target.getInventory().addItem(maceItem);
                } else {
                }
                return true;
            } else {
                return true;
            }
        }

        sender.sendMessage("/cweapons give <назв> <игрок> | /cweapons reload");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> completions = new ArrayList<>();
            completions.add("give");
            completions.add("reload");
            return completions;
        } else if (args.length == 2 && args[0].equalsIgnoreCase("give")) {
            List<String> weapons = new ArrayList<>();
            weapons.add("spider_axe");
            weapons.add("mace_updater");
            return weapons;
        } else if (args.length == 3 && args[0].equalsIgnoreCase("give")) {
            List<String> playerNames = new ArrayList<>();
            for (Player player : Bukkit.getOnlinePlayers()) {
                playerNames.add(player.getName());
            }
            return playerNames;
        }
        return Collections.emptyList();
    }
}