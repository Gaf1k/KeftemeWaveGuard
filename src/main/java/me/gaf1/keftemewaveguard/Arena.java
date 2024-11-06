package me.gaf1.keftemewaveguard;

import me.gaf1.keftemewaveguard.Seller.Seller;
import me.gaf1.keftemewaveguard.Util.ChatUtil;
import org.bukkit.*;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static javax.sql.rowset.spi.SyncFactory.getLogger;

public class Arena {

    public final static Arena instance = new Arena();
    private int maxPlayers = Plugin.getInstance().getConfig().getInt("maxPlayers");
    public static int NumOfWave = 1;
    private boolean areneIsActive = false;
    public List<Entity> entityInArena = new ArrayList<>();
    public static String nameWorld = Plugin.getInstance().getConfig().getString("World");
    private static Location centerArena;
    public List<Player> playerInArena = new ArrayList<>();
    public List<Player> spectators = new ArrayList<>();
    private BukkitRunnable countdownTimer = null;
    private BukkitRunnable gameTimer;


    public void playerJoinArena(Player player){

        if (centerArena == null){
            ChatUtil.sendMessage(player, "&cАдмин еще не создал арены :(");
            return;
        }
        if (playerInArena.contains(player)){
            ChatUtil.sendMessage(player,"&cТы уже на арене бро");
            return;
        }
        if (areneIsActive){
            ChatUtil.sendMessage(player, "&cАрена уже активна, дождись её конца :(");
            return;
        }



        if (playerInArena != null) {
            if (maxPlayers > playerInArena.size()) {
                playerInArena.add(player);
                ChatUtil.sendTitle(player, "&aТы попал на арену!", "&f");
                for (Player player1: playerInArena) {
                    ChatUtil.sendMessage(player1, "&fЧтобы начать игру осталось &A" + playerInArena.size() + "&F/&a" + maxPlayers + " &fигроков");
                }
                playerSetReady(player);
                if (maxPlayers == playerInArena.size()){
                    for (Player player1: playerInArena) {
                        ChatUtil.sendMessage(player1,"&fИгра начнется через &a20&f секунд!");
                    }
                    startGame();
                }
            } else {
                ChatUtil.sendMessage(player, "&cК сожалению на арене нету для тебя места :(");
            }
        }
    }
    public void playerLeaveFromArena(Player player){
        if (playerInArena.contains(player)){
            playerInArena.remove(player);
            ChatUtil.sendMessage(player,"&aТы вышел с арены!");
        }else {
            ChatUtil.sendMessage(player,"&cТебя же нету на арене");
        }
        if (playerInArena.size() == 0){
            loseGame();
        }
    }

    public void startGame() {

        areneIsActive = true;



        gameTimer = new BukkitRunnable() {
            @Override
            public void run() {
                if (entityInArena.size() == 0 && countdownTimer == null) {
                    countdownTimer = new BukkitRunnable() {
                        int ctr = 10;

                        @Override
                        public void run() {
                            for (Player player : playerInArena) {
                                ChatUtil.sendTitle(player, "&fВолна появится через &a" + ctr + "&f сек.", "&fВолна &a" + NumOfWave);
                            }
                            ctr -= 1;
                            if (ctr <= 0) {
                                for (Player player: playerInArena) {
                                    ChatUtil.sendMessage(player, "&fВолна &a" + NumOfWave + " &fпоявилась!");
                                }
                                SpawnWave();
                                cancel();
                                countdownTimer = null;
                            }
                        }
                    };
                    countdownTimer.runTaskTimer(Plugin.getInstance(), 300L, 20L);
                }
            }
        };
        gameTimer.runTaskTimer(Plugin.getInstance(), 0L, 100L);


    }


    public void loseGame(){

        for (Player player: spectators){
            int num = NumOfWave;

            ChatUtil.sendTitle(player,"&cВы проиграли :(", "&fВолна&a " + (num-1));
            ChatUtil.sendMessage(player,"&cВы проиграли на " + (num-1) + " волне :(");

            player.setGameMode(GameMode.CREATIVE);
            player.teleport(Bukkit.getWorld(nameWorld).getSpawnLocation());
            player.getInventory().clear();

            for (Entity entity :entityInArena){
                entity.remove();
            }
            entityInArena.clear();
        }

        gameTimer.cancel();
        NumOfWave = 1;
        areneIsActive = false;
    }
    public void playerSetReady(Player player){
        Location location = centerArena.clone();
        location.add(0.5,1,21.5);
        location.setYaw(180);
        location.setPitch(0);
        player.setGameMode(GameMode.SURVIVAL);
        player.setHealth(20);
        player.setSaturation(20);
        player.getInventory().clear();
        player.getInventory().addItem(new ItemStack(Material.WOODEN_SWORD));
        player.getInventory().addItem(new ItemStack(Material.COOKED_BEEF,16));
        player.teleport(location);
    }

    public void endGame(){

        if (areneIsActive) {
            gameTimer.cancel();
        }

        NumOfWave = 1;
        areneIsActive = false;

        for (Entity entity :entityInArena){
            entity.remove();
        }
        entityInArena.clear();
    }
    public void killEntityInArena(){
        for (Entity entity :entityInArena){
            entity.remove();
        }
        entityInArena.clear();
    }

    public static void saveArena() {
        FileConfiguration config = Plugin.getInstance().getConfig();
        if (centerArena != null) {
            config.set("ArenaLocation.x", centerArena.getX());
            config.set("ArenaLocation.y", centerArena.getY());
            config.set("ArenaLocation.z", centerArena.getZ());
            Plugin.getInstance().saveConfig();
        }
    }

    // Загрузка Location из конфига
    public static void loadArena() {
        FileConfiguration config = Plugin.getInstance().getConfig();
        if (!config.contains("ArenaLocation.x") ||
                !config.contains("ArenaLocation.y") ||
                !config.contains("ArenaLocation.z")) {
            return;
        }

        double x = config.getDouble("ArenaLocation.x");
        double y = config.getDouble("ArenaLocation.y");
        double z = config.getDouble("ArenaLocation.z");
        createArena(x,y,z);
    }


    public static void createArena(double centX, double centY, double centZ) {

        centerArena = new Location(Bukkit.getWorld(Arena.nameWorld),centX,centY,centZ);

        int radius = 25;
        int height = 10;
        int centerX = (int) centX;
        int centerY = (int) centY;
        int centerZ = (int) centZ;

        for (int y = 0; y < height; y++) {
            for (int x = -radius; x <= radius; x++) {
                for (int z = -radius; z <= radius; z++) {
                    if (Math.sqrt(x * x + z * z) >= radius - 0.5 && Math.sqrt(x * x + z * z) <= radius + 0.5) {
                        Block block = Bukkit.getWorld(nameWorld).getBlockAt(centerX + x, centerY + y, centerZ + z);
                        block.setType(Material.STONE);
                    }
                    if (y == 0 && Math.sqrt(x * x + z * z) <= radius) {
                        Block floorBlock = Bukkit.getWorld(nameWorld).getBlockAt(centerX + x, centerY, centerZ + z);
                        floorBlock.setType(Material.STONE);
                    }
                }
            }
        }
        for (int y = 0; y < height; y++) {
            for (int x = -radius + 1; x < radius; x++) {
                for (int z = -radius + 1; z < radius; z++) {
                    if (Math.sqrt(x * x + z * z) < radius - 0.5) {
                        Block block = Bukkit.getWorld(nameWorld).getBlockAt(centerX + x, centerY + y, centerZ + z);
                        block.setType(Material.AIR);
                    }
                }
            }
        }
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                if (Math.sqrt(x * x + z * z) <= radius) {
                    Block floorBlock = Bukkit.getWorld(nameWorld).getBlockAt(centerX + x, centerY, centerZ + z);
                    floorBlock.setType(Material.STONE);
                }
            }
        }
        Location location1 = centerArena.clone();
        Location location2 = centerArena.clone();
        Location location3 = centerArena.clone();
        Location location4 = centerArena.clone();
        Block spawnBlock = Bukkit.getWorld(nameWorld).getBlockAt(location4.add(0,0,21));
        spawnBlock.setType(Material.GREEN_CONCRETE);
        Block fireballBlock1 = Bukkit.getWorld(nameWorld).getBlockAt(location1.add(0,0,-18));
        Block fireballBlock2 = Bukkit.getWorld(nameWorld).getBlockAt(location2.add(18,0,0));
        Block fireballBlock3 = Bukkit.getWorld(nameWorld).getBlockAt(location3.add(-18,0,0));
        fireballBlock1.setType(Material.RED_CONCRETE);
        fireballBlock2.setType(Material.RED_CONCRETE);
        fireballBlock3.setType(Material.RED_CONCRETE);

        Seller.instance.createSeller();

        saveArena();
    }
    public void  removeArena(){

        int radius = 25;
        int height = 25;
        int centerX = (int) centerArena.getX();
        int centerY = (int) centerArena.getY();
        int centerZ = (int) centerArena.getZ();

        for (int y = 0; y < height; y++) {
            for (int x = -radius; x <= radius; x++) {
                for (int z = -radius; z <= radius; z++) {
                    if (Math.sqrt(x * x + z * z) >= radius - 0.5 && Math.sqrt(x * x + z * z) <= radius + 0.5) {
                        Block block = Bukkit.getWorld(nameWorld).getBlockAt(centerX + x, centerY + y, centerZ + z);
                        block.setType(Material.AIR);
                    }
                    if (y == 0 && Math.sqrt(x * x + z * z) <= radius) {
                        Block floorBlock = Bukkit.getWorld(nameWorld).getBlockAt(centerX + x, centerY, centerZ + z);
                        floorBlock.setType(Material.AIR);
                    }
                }
            }
        }
        for (int y = 0; y < height; y++) {
            for (int x = -radius + 1; x < radius; x++) {
                for (int z = -radius + 1; z < radius; z++) {
                    if (Math.sqrt(x * x + z * z) < radius - 0.5) {
                        Block block = Bukkit.getWorld(nameWorld).getBlockAt(centerX + x, centerY + y, centerZ + z);
                        block.setType(Material.AIR);
                    }
                }
            }
        }
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                if (Math.sqrt(x * x + z * z) <= radius) {
                    Block floorBlock = Bukkit.getWorld(nameWorld).getBlockAt(centerX + x, centerY, centerZ + z);
                    floorBlock.setType(Material.AIR);
                }
            }
        }
        Location location1 = centerArena.clone();
        Location location2 = centerArena.clone();
        Location location3 = centerArena.clone();
        Location location4 = centerArena.clone();
        Block spawnBlock = Bukkit.getWorld(nameWorld).getBlockAt(location4.add(0,0,21));
        spawnBlock.setType(Material.AIR);
        Block fireballBlock1 = Bukkit.getWorld(nameWorld).getBlockAt(location1.add(0,0,-18));
        Block fireballBlock2 = Bukkit.getWorld(nameWorld).getBlockAt(location2.add(18,0,0));
        Block fireballBlock3 = Bukkit.getWorld(nameWorld).getBlockAt(location3.add(-18,0,0));
        fireballBlock1.setType(Material.AIR);
        fireballBlock2.setType(Material.AIR);
        fireballBlock3.setType(Material.AIR);

        FileConfiguration config = Plugin.getInstance().getConfig();
        config.set("ArenaLocation", null);
        Plugin.getInstance().saveConfig();

        Seller.instance.removeSeller();

        centerArena = null;
    }

    public void SpawnWave() {
        Location firebalLocation1 = centerArena.clone();
        Location firebalLocation2 = centerArena.clone();
        Location firebalLocation3 = centerArena.clone();
        Vector fbvector = new Vector(0, -1, 0);
        Fireball fireball1 = (Fireball) Bukkit.getWorld(nameWorld).spawnEntity(firebalLocation1.add(-18, 5, 0), EntityType.FIREBALL);
        Fireball fireball2 = (Fireball) Bukkit.getWorld(nameWorld).spawnEntity(firebalLocation2.add(18, 5, 0), EntityType.FIREBALL);
        Fireball fireball3 = (Fireball) Bukkit.getWorld(nameWorld).spawnEntity(firebalLocation3.add(0, 5, -18), EntityType.FIREBALL);
        fireball1.setDirection(fbvector);
        fireball2.setDirection(fbvector);
        fireball3.setDirection(fbvector);
        NumOfWave +=1;
    }

    public void setCenterArena(Location centerArena) {
        this.centerArena = centerArena;
    }

    public static Location getCenterArena() {
        return centerArena;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }
    public List<EntityType> getEntityTypesFromConfig(String level) {
        List<EntityType> listName = new ArrayList<>();
        FileConfiguration config = Plugin.instance.getConfig();
        List<String> entityTypeNames = config.getStringList("Level." + level);

        for (String name : entityTypeNames) {
            try {
                EntityType entityType = EntityType.valueOf(name.toUpperCase());
                listName.add(entityType);
            } catch (IllegalArgumentException e) {
            }
        }

        return listName;
    }


}
