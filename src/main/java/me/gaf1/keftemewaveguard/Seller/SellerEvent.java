package me.gaf1.keftemewaveguard.Seller;

import me.gaf1.keftemewaveguard.Arena;
import me.gaf1.keftemewaveguard.Util.ChatUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class SellerEvent implements Listener {


    @EventHandler
public void onClick(InventoryClickEvent event) {
    if (event.getInventory() == SellerMenu.instance.inventory) {
        event.setCancelled(true);
        if (event.getCurrentItem() != null) {
            Player player = (Player) event.getWhoClicked();
            if (event.getCurrentItem().getItemMeta().getPersistentDataContainer().has(NamespacedKey.fromString("price"), PersistentDataType.INTEGER)) {
                int price = event.getCurrentItem().getItemMeta().getPersistentDataContainer().get(NamespacedKey.fromString("price"), PersistentDataType.INTEGER);
                if (event.getWhoClicked().getInventory().contains(Material.EMERALD, price)) {
                    event.getWhoClicked().getInventory().removeItem(new ItemStack[]{new ItemStack(Material.EMERALD, price)});

                    ItemStack itemStack = event.getCurrentItem().clone();
                    itemStack.setItemMeta(null);
                    event.getWhoClicked().getInventory().addItem(itemStack);
                } else {
                    ChatUtil.sendMessage((Player) event.getWhoClicked(), "&cУ тебя не достаточно изумрудов");
                }

            } else if (event.getCurrentItem().getItemMeta().getPersistentDataContainer().has(NamespacedKey.fromString("tovar"), PersistentDataType.INTEGER)) {
                if (event.getWhoClicked().getInventory().contains(Material.EMERALD, 10)) {
                    event.getWhoClicked().getInventory().removeItem(new ItemStack(Material.EMERALD, 10));
                    SellerMenu.instance.fillMenu();
                } else {
                    ChatUtil.sendMessage(player, "&cУ тебя недостаточно изумрудов");
                }
            }
        }
    }
}




        @EventHandler
        public void onClickSeller (PlayerInteractAtEntityEvent event){
            if (event.getRightClicked().getPersistentDataContainer().has(NamespacedKey.fromString("unique"), PersistentDataType.INTEGER)) {
                event.getPlayer().openInventory(SellerMenu.instance.inventory);
            }
        }


}

