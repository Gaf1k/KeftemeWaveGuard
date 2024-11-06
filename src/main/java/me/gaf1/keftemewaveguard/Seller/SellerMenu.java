package me.gaf1.keftemewaveguard.Seller;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class SellerMenu {

    public static final SellerMenu instance = new SellerMenu();

    Inventory inventory = Bukkit.createInventory(null, 36, ChatColor.translateAlternateColorCodes('&',"&0Магазин"));

    List<ItemStack> itemForSell = new ArrayList<>();

    SellerMenu(){
        fillMenu();
    }

    public void fillMenu(){
        itemForSell = Arrays.asList(
                SellerUtil.createItemForSell(Material.LEATHER_BOOTS,5),
                SellerUtil.createItemForSell(Material.LEATHER_LEGGINGS, 5),
                SellerUtil.createItemForSell(Material.LEATHER_CHESTPLATE,5 ),
                SellerUtil.createItemForSell(Material.LEATHER_HELMET,5),
                SellerUtil.createItemForSell(Material.IRON_BOOTS, 15),
                SellerUtil.createItemForSell(Material.IRON_LEGGINGS, 15),
                SellerUtil.createItemForSell(Material.IRON_CHESTPLATE, 15),
                SellerUtil.createItemForSell(Material.IRON_HELMET,15),
                SellerUtil.createItemForSell(Material.IRON_SWORD,15),
                SellerUtil.createItemForSell(Material.CHAINMAIL_BOOTS,10),
                SellerUtil.createItemForSell(Material.CHAINMAIL_LEGGINGS,10),
                SellerUtil.createItemForSell(Material.CHAINMAIL_CHESTPLATE,10),
                SellerUtil.createItemForSell(Material.CHAINMAIL_HELMET,10),
                SellerUtil.createItemForSell(Material.STONE_SWORD,10),
                SellerUtil.createItemForSell(Material.GOLDEN_BOOTS,7),
                SellerUtil.createItemForSell(Material.GOLDEN_LEGGINGS,7),
                SellerUtil.createItemForSell(Material.GOLDEN_CHESTPLATE,7),
                SellerUtil.createItemForSell(Material.GOLDEN_HELMET,7),
                SellerUtil.createItemForSell(Material.GOLDEN_SWORD,7),
                SellerUtil.createItemForSell(Material.COOKED_BEEF,4,2),
                SellerUtil.createItemForSell(Material.COOKED_CHICKEN,4,2),
                SellerUtil.createItemForSell(Material.COOKED_RABBIT,4,2),
                SellerUtil.createItemForSell(Material.GOLDEN_APPLE,2,6),
                SellerUtil.createItemForSell(Material.TOTEM_OF_UNDYING,64),
                SellerUtil.createItemForSell(Material.ENDER_PEARL,4),
                SellerUtil.createItemForSell(Material.ENCHANTED_GOLDEN_APPLE,32)
        );
        Collections.shuffle(itemForSell);
        int itemIndex = 0;

        for (int i = 0; i < this.inventory.getSize(); ++i){
            if (i >= 10 && i <= 16){
                if (itemIndex < itemForSell.size()) {
                    inventory.setItem(i, itemForSell.get(itemIndex));
                    itemIndex++;
                }
            }
            else if(i == 31){
                ItemStack itemStack = new ItemStack(Material.GHAST_TEAR);
                ItemMeta itemMeta = itemStack.getItemMeta();
                itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&fНажми чтобы поменять товары"));
                itemMeta.getPersistentDataContainer().set(NamespacedKey.fromString("tovar"), PersistentDataType.INTEGER,1);
                List<String> list = new ArrayList<>();
                list.add(" ");
                list.add(ChatColor.translateAlternateColorCodes('&', "&fЦена данной процедуры &a10 &fизумрудов"));
                itemMeta.setLore(list);
                itemStack.setItemMeta(itemMeta);
                inventory.setItem(i,itemStack);
            }
            else {
                inventory.setItem(i,SellerUtil.createDecorItem(Material.GRAY_STAINED_GLASS_PANE));
            }
        }
    }

}

