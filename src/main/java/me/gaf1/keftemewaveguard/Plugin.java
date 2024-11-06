package me.gaf1.keftemewaveguard;

import me.gaf1.keftemewaveguard.Events.Events;
import me.gaf1.keftemewaveguard.Seller.Seller;
import me.gaf1.keftemewaveguard.Seller.SellerEvent;
import me.gaf1.keftemewaveguard.Seller.SellerMenu;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.function.BiConsumer;

public final class Plugin extends JavaPlugin {

    public static Plugin instance;

    public static Plugin getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {

        instance = this;
        saveDefaultConfig();

        Arena.loadArena();

        Bukkit.getPluginManager().registerEvents(new SellerEvent(),this);

        Bukkit.getPluginManager().registerEvents(new Events(),this);
        getCommand("arena").setExecutor(new CMD());
        getCommand("arena").setTabCompleter(new CMD());

    }

    @Override
    public void onDisable() {
        Seller.instance.removeSeller();
        Arena.instance.endGame();
        Arena.saveArena();
    }
}
