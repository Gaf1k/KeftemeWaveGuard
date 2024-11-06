package me.gaf1.keftemewaveguard.Seller;

import me.gaf1.keftemewaveguard.Arena;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class SellerUtil {

    public static ItemStack createItemForSell(Material material, int price){

        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.getPersistentDataContainer().set(NamespacedKey.fromString("price"), PersistentDataType.INTEGER, price);

        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemMeta.addItemFlags(ItemFlag.HIDE_DYE);
        itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);

        List<String> list = new ArrayList<>();
        list.add(" ");
        list.add(ChatColor.translateAlternateColorCodes('&',"&fЦена: &a" + price + " &fизумрудов"));



        itemMeta.setLore(list);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
    public static ItemStack createItemForSell(Material material,int amount, int price){

        ItemStack itemStack = new ItemStack(material,amount);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.getPersistentDataContainer().set(NamespacedKey.fromString("price"), PersistentDataType.INTEGER, price);

        List<String> list = new ArrayList<>();
        list.add(" ");
        list.add(ChatColor.translateAlternateColorCodes('&',"&fЦена: &a" + price + " &fизумрудов"));

        itemMeta.setLore(list);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
    public static ItemStack createDecorItem(Material material){
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(" ");
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

}
