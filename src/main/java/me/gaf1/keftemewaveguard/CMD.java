package me.gaf1.keftemewaveguard;

import me.gaf1.keftemewaveguard.Seller.Seller;
import me.gaf1.keftemewaveguard.Seller.SellerMenu;
import me.gaf1.keftemewaveguard.Util.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CMD implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args[0].equalsIgnoreCase("reload")){
            Plugin.getInstance().reloadConfig();
            if (!(sender instanceof Player)){
                System.out.println("Конфиг перезагружен!");
            }else {
                Player player = (Player) sender;
                ChatUtil.sendMessage(player, "&aКонфиг перезагружен!");
            }
        }
        if (!(sender instanceof Player)){
            System.out.println("Ты консоль тебе нельзя!");
            return true;
        }
        Player player = (Player) sender;

        if (args.length == 0){
            ChatUtil.sendMessage(player,"&cНедостаточно аргументов");
            return true;
        }

        if (args[0].equalsIgnoreCase("create")) {
            if (args.length < 4){
                ChatUtil.sendMessage(player,"&cТы ввел координаты неправильно!");
                return true;
            }
            int x,y,z;
            try {
                x = Integer.parseInt(args[1]);
                y = Integer.parseInt(args[2]);
                z = Integer.parseInt(args[3]);
            }catch (NumberFormatException e){
                ChatUtil.sendMessage(player,"&cТы ввел координаты неправильно!");
                return true;
            }
            Arena.createArena(x,y,z);
            ChatUtil.sendMessage(player,"&aАрена успешно установлена");
            return true;
        }else if(args[0].equalsIgnoreCase("remove")){
            Arena.instance.removeArena();
        }
        else if (args[0].equalsIgnoreCase("join")){
            Arena.instance.playerJoinArena(player);
        }else if (args[0].equalsIgnoreCase("leave")){
            Arena.instance.playerLeaveFromArena(player);
        }else if (args[0].equalsIgnoreCase("killMobs")){
            Arena.instance.killEntityInArena();
        } else if (args[0].equalsIgnoreCase("killSeller")){
            Seller.instance.removeSeller();
        }

        return false;
        }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        if (args.length == 1){
            if (sender.isOp()) {
                return List.of("create","join","leave","reload","killMobs","remove","killSeller");
            }else{
                return List.of("join","leave");
            }
        }
        if (args[0].equalsIgnoreCase("create")){
            if (args.length == 2){
                return List.of("x");
            }
            if (args.length == 3){
                return List.of("y");
            }
            if (args.length == 4){
                return List.of("z");
            }
        }

        return null;
    }
}
