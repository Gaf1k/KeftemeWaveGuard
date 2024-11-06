package me.gaf1.keftemewaveguard.Events;

import me.gaf1.keftemewaveguard.Arena;
import me.gaf1.keftemewaveguard.Util.ChatUtil;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;


public class Events implements Listener {

    @EventHandler
    public void breakBlockInArena(BlockBreakEvent event){
        if (Arena.instance.playerInArena != null) {
            if (Arena.instance.playerInArena.contains(event.getPlayer())){
               event.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void placeBlockInArena(BlockPlaceEvent event){
        if (Arena.instance.playerInArena != null) {
            if (Arena.instance.playerInArena.contains(event.getPlayer())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void spawnEntityAtExplosion(ExplosionPrimeEvent event){
        if (event.getEntity() instanceof Fireball){
            event.setCancelled(true);
            Location location = event.getEntity().getLocation();
            List<EntityType> firstLevel = Arena.instance.getEntityTypesFromConfig("first");
            List<EntityType> secondLevel = Arena.instance.getEntityTypesFromConfig("second");
            List<EntityType> thirdLevel = Arena.instance.getEntityTypesFromConfig("third");
            List<EntityType> fourthLevel = Arena.instance.getEntityTypesFromConfig("fourth");
            if (Arena.instance.NumOfWave <= 5) {
                for (EntityType entityType : firstLevel) {
                    for (int i = 0; i<2;i++) {
                        Entity entity1 = Bukkit.getWorld(Arena.nameWorld).spawnEntity(location, entityType);
                        entity1.setCustomName(ChatColor.translateAlternateColorCodes('&', "&9Монстр"));
                        Arena.instance.entityInArena.add(entity1);
                    }
                }
            }
            else if (Arena.instance.NumOfWave <= 10) {
                for (EntityType entityType : secondLevel) {
                    for (int i = 0; i<2;i++) {
                        Entity entity1 = Bukkit.getWorld(Arena.nameWorld).spawnEntity(location, entityType);
                        entity1.setCustomName(ChatColor.translateAlternateColorCodes('&', "&9Монстр"));
                        Arena.instance.entityInArena.add(entity1);
                    }
                }
            }
            else if (Arena.instance.NumOfWave <= 15) {
                for (EntityType entityType : thirdLevel) {
                    for (int i = 0; i<2;i++) {
                        Entity entity1 = Bukkit.getWorld(Arena.nameWorld).spawnEntity(location, entityType);
                        entity1.setCustomName(ChatColor.translateAlternateColorCodes('&',"&9Монстр"));
                        Arena.instance.entityInArena.add(entity1);
                    }
                }
            }else {
                for (EntityType entityType : fourthLevel) {
                    for (int i = 0; i<2;i++) {
                        Entity entity1 = Bukkit.getWorld(Arena.nameWorld).spawnEntity(location, entityType);
                        entity1.setCustomName(ChatColor.translateAlternateColorCodes('&', "&9Монстр"));
                        Arena.instance.entityInArena.add(entity1);
                    }
                }
            }
            Bukkit.getWorld(event.getEntity().getWorld().getName()).playSound(event.getEntity().getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 10F, 1.0F);
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event){
        if (Arena.instance.entityInArena.contains(event.getEntity())){
            event.getDrops().clear();
            event.getDrops().add(new ItemStack(Material.EMERALD));
            Arena.instance.entityInArena.remove(event.getEntity());
            if (Arena.instance.entityInArena.size() == 0){
                for (Player player : Arena.instance.playerInArena) {
                    ChatUtil.sendMessage(player, "&aВы успешно прошли волну!");
                }
            }
        }

    }




    @EventHandler
    public void onEntityDamageByFire(EntityDamageEvent event) {
        if (event.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK || event.getCause() == EntityDamageEvent.DamageCause.FIRE){
            if (Arena.instance.entityInArena.contains(event.getEntity())) {
                event.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void onExplosion(EntityExplodeEvent event) {
        if (event.getEntity() instanceof Creeper) {
            if (Arena.instance.entityInArena.contains(event.getEntity())){
                Arena.instance.entityInArena.remove(event.getEntity());
                event.blockList().clear();
            }
        }
    }



    @EventHandler
    public void onPlayerDeath(EntityDamageEvent event){
        if (event.getEntity() instanceof Player ){
            Player player = (Player) event.getEntity();
            if (Arena.instance.playerInArena.contains(player)) {
                if (player.getHealth() < event.getDamage()) {
                    event.setCancelled(true);
                    Arena.instance.spectators.add(player);
                    Arena.instance.playerInArena.remove(player);
                    Location location = Arena.instance.getCenterArena().clone();
                    location.setYaw(180);
                    location.setPitch(90);
                    player.teleport(location.add(0, 15, 0));
                    player.setGameMode(GameMode.SPECTATOR);
                    ChatUtil.sendTitle(player,"&cТы умер!","&7Теперь ты наблюдаешь за игрой");
                    if (Arena.instance.playerInArena.size() == 0){
                        Arena.instance.loseGame();
                    }
                }
            }
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event){
        if (Arena.instance.playerInArena.contains(event.getPlayer())){
            Player player = event.getPlayer();
            player.damage(player.getHealth());
        }
    }



}
