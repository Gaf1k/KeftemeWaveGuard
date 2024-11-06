package me.gaf1.keftemewaveguard.Seller;

import me.gaf1.keftemewaveguard.Arena;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.persistence.PersistentDataType;

public class Seller {
    public final static Seller instance = new Seller();

    private Villager villager;

    public void createSeller() {
        Location location = Arena.getCenterArena().clone();
        villager = (Villager) Bukkit.getWorld(Arena.nameWorld).spawnEntity(location.add(0.5, 1, 24.5), EntityType.VILLAGER);
        villager.setAI(false);
        villager.getPersistentDataContainer().set(NamespacedKey.fromString("unique"), PersistentDataType.INTEGER,1);
        villager.setCustomName(ChatColor.translateAlternateColorCodes('&',"&6Носатый торговец"));
        villager.setInvulnerable(true);
        villager.setRotation(180,0);
    }

    public void removeSeller(){
        if (villager!=null) {
            villager.damage(villager.getHealth());
            villager.remove();
        }
    }
}
