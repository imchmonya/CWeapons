package org.cWeapons;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.black_ixx.playerpoints.PlayerPoints;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.cWeapons.Commands.commands;
import org.cWeapons.Weapons.MaceUpdater.MaceUpdater;
import org.cWeapons.Weapons.MaceUpdater.MaceUpdaterListener;
import org.cWeapons.Weapons.SpiderAxe.SpiderAxeListener;
public final class CWeapons extends JavaPlugin {

    private static CWeapons instance;
    private PlayerPointsAPI playerPointsAPI;
    private MaceUpdater maceUpdater;

    @Override
    public void onEnable() {
        instance = this;
        if (Bukkit.getPluginManager().getPlugin("PlayerPoints") != null) {
            playerPointsAPI = PlayerPoints.getInstance().getAPI();
        } else {
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        maceUpdater = new MaceUpdater(this);
        saveDefaultConfig();
        getCommand("cweapons").setExecutor(new commands(this));
        getCommand("cweapons").setTabCompleter(new commands(this));
        getServer().getPluginManager().registerEvents(new MaceUpdaterListener(this), this);
        getServer().getPluginManager().registerEvents(new SpiderAxeListener(this), this);
        getServer().getPluginManager().registerEvents(new glowning(this), this);

        getLogger().info("CWeapons вкл!");
    }

    public static CWeapons getInstance() {
        return instance;
    }

    public PlayerPointsAPI getPlayerPointsAPI() {
        return playerPointsAPI;
    }

    public MaceUpdater getMaceUpdater() {
        return maceUpdater;
    }
}