package me.gaf1.keftemewaveguard.Util;

import me.gaf1.keftemewaveguard.Plugin;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ChatUtil {

    public static void sendMessage(Player player,String msg){
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
    }
    public static void sendTitle(Player player,String msg,String subMsg,int fadeIn,int stay,int fadeOut){
        player.sendTitle(ChatColor.translateAlternateColorCodes('&',msg),ChatColor.translateAlternateColorCodes('&',subMsg), fadeIn,stay,fadeOut);
    }

    public static void sendTitle(Player player,String msg,String subMsg){
        player.sendTitle(ChatColor.translateAlternateColorCodes('&',msg),ChatColor.translateAlternateColorCodes('&',subMsg), 10,20,10);
    }


    public static void broadcastTitle(String msg,String subMsg,int fadeIn,int stay,int fadeOut){
        for (Player player: Plugin.getInstance().getServer().getOnlinePlayers()){
            player.sendTitle(ChatColor.translateAlternateColorCodes('&',msg),ChatColor.translateAlternateColorCodes('&',subMsg), fadeIn,stay,fadeOut);
        }
    }
    public static void broadcastTitle(String msg,String subMsg){
        for (Player player: Plugin.getInstance().getServer().getOnlinePlayers()){
            player.sendTitle(ChatColor.translateAlternateColorCodes('&',msg),ChatColor.translateAlternateColorCodes('&',subMsg), 10,20,10);
        }
    }
    public static void broadcastMessage(String msg){
        for (Player player: Plugin.getInstance().getServer().getOnlinePlayers()){
            sendMessage(player,msg);
        }
    }


}
