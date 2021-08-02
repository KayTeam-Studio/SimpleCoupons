/*
 *   Copyright (C) 2021 segu23
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package org.kayteam.simplecoupons.coupon;

import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.kayteam.simplecoupons.SimpleCoupons;
import org.kayteam.simplecoupons.util.Logs;
import org.kayteam.simplecoupons.util.Yaml;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CouponManager {

    private final SimpleCoupons plugin;

    public CouponManager(SimpleCoupons plugin) {
        this.plugin = plugin;
    }

    Map<String, Coupon> coupons = new HashMap<>();
    public Map<String, Coupon> getCoupons() {
        return coupons;
    }


    public void loadCoupons() {
        for (File file : Yaml.getFolderFiles(plugin.getDataFolder()+"/coupons")) {
            loadCoupon((file.getName()).replaceAll(".yml", ""));
        }
    }

    /**
     *
     * @param couponName Name of the coupon what do you want to load
     */
    public void loadCoupon(String couponName){
        Yaml couponYaml = new Yaml(plugin, "coupons", couponName);
        couponYaml.registerFileConfiguration();
        couponYaml.reloadFileConfiguration();
        if(couponYaml.getItemStack("item") != null){
            Coupon coupon = new Coupon();
            Map<Action, List<Object>> couponActions = new HashMap<>();
            coupon.setName(couponName);
            coupon.setCouponItem(couponYaml.getItemStack("item"));
            if(couponYaml.contains("rewards")){

                // LOAD XP
                if(couponYaml.contains("rewards.xp")){
                    if(couponYaml.isInt("rewards.xp")){
                        try{
                            List<Object> xp = new ArrayList<>();
                            xp.add(couponYaml.getInt("rewards.xp"));
                            couponActions.put(Action.XP, xp);
                        }catch (Exception e){
                            Logs.sendLoadLogError(plugin,"rewards/xp", "coupons/"+couponName);
                        }
                    }else{
                        Logs.sendLoadLogError(plugin,"rewards/xp", "coupons/"+couponName);
                    }
                }

                // LOAD MONEY
                if(couponYaml.contains("rewards.money")){
                    if(couponYaml.isInt("rewards.money")){
                        try{
                            List<Object> money = new ArrayList<>();
                            money.add(couponYaml.getInt("rewards.money"));
                            couponActions.put(Action.MONEY, money);
                        }catch (Exception e){
                            Logs.sendLoadLogError(plugin,"rewards/money", "coupons/"+couponName);
                        }
                    }else{
                        Logs.sendLoadLogError(plugin,"rewards/money", "coupons/"+couponName);
                    }
                }

                // LOAD ITEMS
                if(couponYaml.contains("rewards.items")){
                    List<Object> items = new ArrayList<>();
                    for(String itemName : couponYaml.getFileConfiguration().getConfigurationSection("rewards.items").getKeys(false)){
                        if(couponYaml.getItemStack("rewards.items."+itemName) != null){
                            try{
                                items.add(couponYaml.getItemStack("rewards.items."+itemName));
                            }catch (Exception e){
                                Logs.sendLoadLogError(plugin,"rewards/items/"+itemName, "coupons/"+couponName);
                            }
                        }else{
                            Logs.sendLoadLogError(plugin,"rewards/items/"+itemName, "coupons/"+couponName);
                        }
                    }
                    couponActions.put(Action.ITEM, items);
                }

                // LOAD MESSAGES
                if(couponYaml.contains("rewards.messages")){
                    if(couponYaml.isList("rewards.messages")){
                        try{
                            couponActions.put(Action.MESSAGE, new ArrayList<>(couponYaml.getStringList("rewards.messages")));
                        }catch (Exception e){
                            Logs.sendLoadLogError(plugin,"rewards/messages", "coupons/"+couponName);
                        }
                    }else if(couponYaml.isString("rewards.messages")){
                        try{
                            List<Object> messages = new ArrayList<>();
                            messages.add(couponYaml.getString("rewards.messages"));
                            couponActions.put(Action.MESSAGE, messages);
                        }catch (Exception e){
                            Logs.sendLoadLogError(plugin,"rewards/messages", "coupons/"+couponName);
                        }
                    }else{
                        Logs.sendLoadLogError(plugin,"rewards/messages", "coupons/"+couponName);
                    }
                }

                // LOAD COMMANDS
                if(couponYaml.contains("rewards.commands")){
                    if(couponYaml.isList("rewards.commands")){
                        try{
                            couponActions.put(Action.COMMAND, new ArrayList<>(couponYaml.getStringList("rewards.commands")));
                        }catch (Exception e){
                            Logs.sendLoadLogError(plugin,"rewards/commands", "coupons/"+couponName);
                        }
                    }else if(couponYaml.isString("rewards.commands")){
                        try{
                            List<Object> commands = new ArrayList<>();
                            commands.add(couponYaml.getString("rewards.commands"));
                            couponActions.put(Action.COMMAND, commands);
                        }catch (Exception e){
                            Logs.sendLoadLogError(plugin,"rewards/commands", "coupons/"+couponName);
                        }
                    }else{
                        Logs.sendLoadLogError(plugin,"rewards/commands", "coupons/"+couponName);
                    }
                }
            }
            coupon.setActions(couponActions);
            getCoupons().put(couponName, coupon);
            Logs.sendCorrectCouponLoadLog(plugin, couponName);
        }else{
            Logs.sendGetItemLogError(plugin,"coupons/"+couponName);
        }
    }

    /**
     *
     * @param couponName Name of the coupon what do you want to give
     * @param player Player target who receive the coupon
     */
    public void giveCoupon(String couponName, Player player){
        if(getCoupons().get(couponName).getCouponItem() != null){
            ItemStack item = getCoupons().get(couponName).getCouponItem();
            NBTItem nbti = new NBTItem(item);
            nbti.setString("coupon-name", couponName);
            player.getInventory().addItem(nbti.getItem());
        }else{
            Logs.sendGetItemLogError(plugin,"coupons/"+couponName);
        }
    }

    /**
     *
     * @param coupon Coupon object what you want to save into coupon YML file
     */
    public void saveCoupon(Coupon coupon){
        if(coupon != null){
            Yaml couponYaml = new Yaml(plugin, "coupons", coupon.getName());
            couponYaml.registerFileConfiguration();
            couponYaml.set("item", null);
            couponYaml.set("rewards", null);
            couponYaml.setItemStack("item", coupon.getCouponItem());

            // SAVE MONEY
            if(coupon.getActions().get(Action.MONEY) != null){
                List<Object> list = new ArrayList<>(coupon.getActions().get(Action.MONEY));
                couponYaml.set("rewards.money", (Integer) list.get(0));
            }

            // SAVE XP
            if(coupon.getActions().get(Action.XP) != null){
                List<Object> list = new ArrayList<>(coupon.getActions().get(Action.XP));
                couponYaml.set("rewards.xp", (Integer) list.get(0));
            }

            // SAVE ITEMS
            if(coupon.getActions().get(Action.ITEM) != null){
                int i = 0;
                for(Object item : coupon.getActions().get(Action.ITEM)){
                    if((ItemStack) item != null){
                        couponYaml.setItemStack("rewards.items."+i, (ItemStack) item);
                        i++;
                    }
                }
            }

            // SAVE COMMANDS
            if(coupon.getActions().get(Action.COMMAND) != null){
                couponYaml.set("rewards.commands", new ArrayList<>(coupon.getActions().get(Action.COMMAND)));
            }

            // SAVE MESSAGES
            if(coupon.getActions().get(Action.MESSAGE) != null){
                couponYaml.set("rewards.messages", new ArrayList<>(coupon.getActions().get(Action.MESSAGE)));
            }

            couponYaml.saveFileConfiguration();
        }
    }

    /**
     *
     * @param coupon Coupon object what do you want to delete
     * @return If the coupon could be deleted
     */
    public boolean deleteCoupon(Coupon coupon){
        Yaml couponYaml = new Yaml(plugin, "coupons", coupon.getName());
        if(couponYaml.existFileConfiguration()){
            couponYaml.deleteFileConfiguration();
            unloadCoupon(coupon);
            return true;
        }else{
            return false;
        }
    }

    /**
     *
     * @param coupon Coupon object what do you want to unload from RAM
     */
    public void unloadCoupon(Coupon coupon){
        getCoupons().remove(coupon.getName(), coupon);
    }

    /**
     *
     * @param coupon Coupon object what do you want to execute actions
     * @param player Target player on which actions will be executed
     */
    public void executeCouponActions(Coupon coupon, Player player){
        for(Action action : coupon.getActions().keySet()){
            switch (action){
                case XP:{
                    try{
                        player.giveExpLevels((Integer) coupon.getActions().get(Action.XP).get(0));
                    }catch (Exception e){
                        Logs.sendGiveXPLogError(plugin,"coupons/"+coupon.getName(), player.getName());
                    }
                    break;
                }
                case MONEY:{
                    try{
                        SimpleCoupons.getEconomy().depositPlayer(player, (Integer) coupon.getActions().get(Action.MONEY).get(0));
                    }catch (Exception e){
                        Logs.sendGiveMoneyLogError(plugin,"coupons/"+coupon.getName(), player.getName());
                    }
                    break;
                }
                case ITEM:{
                    for(Object item : coupon.getActions().get(Action.ITEM)){
                        try{
                            if(player.getInventory().firstEmpty() != -1){
                                player.getInventory().addItem((ItemStack) item);
                            }else{
                                player.getLocation().getWorld().dropItem(player.getLocation(), (ItemStack) item);
                            }
                        }catch (Exception ex){
                            Logs.sendGiveItemLogError(plugin, item.toString(), "coupons/"+coupon.getName(),player.getName());
                        }
                    }
                    break;
                }
                case MESSAGE:{
                    try{
                        Yaml.sendSimpleMessage(player, coupon.getActions().get(Action.MESSAGE), new String[][]{{"%player%", player.getName()}});
                    }catch (Exception e){
                        Logs.sendMessageLogError(plugin,"coupons/"+coupon.getName(), player.getName());
                    }
                    break;
                }
                case COMMAND:{
                    for(Object commandLine : coupon.getActions().get(Action.COMMAND)){
                        try{
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), (commandLine.toString()).replaceAll("%player%", player.getName()));
                        }catch (Exception e){
                            Logs.sendExecuteLogError(plugin, commandLine.toString(), "coupons/"+coupon.getName());
                        }
                    }
                    break;
                }
            }
        }
    }
}
