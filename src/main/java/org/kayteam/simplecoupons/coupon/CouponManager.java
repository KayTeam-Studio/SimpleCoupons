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

    public void loadCoupon(String couponName){
        Yaml couponYaml = new Yaml(plugin, "coupons", couponName);
        couponYaml.registerFileConfiguration();
        if(couponYaml.getItemStack("item") != null){
            Coupon coupon = new Coupon();
            Map<Action, List<Object>> couponActions = new HashMap<>();
            coupon.setName(couponName);
            coupon.setCouponItem(couponYaml.getItemStack("item"));
            if(couponYaml.contains("rewards")){
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
            plugin.getLogger().info(coupon.getActions().toString());
        }else{
            Logs.sendGetItemLogError(plugin,"coupons/"+couponName);
        }
    }

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

    public void executeCouponActions(Coupon coupon, Player player){
        for(Action action : coupon.getActions().keySet()){
            switch (action){
                case XP:{
                    try{
                        player.giveExp((Integer) coupon.getActions().get(Action.XP).get(0));
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
                            player.getInventory().addItem((ItemStack) item);
                        }catch (Exception e){
                            try{
                                player.getLocation().getWorld().dropItem(player.getLocation(), (ItemStack) item);
                            }catch (Exception ex){
                                Logs.sendGiveItemLogError(plugin, item.toString(), "coupons/"+coupon.getName(),player.getName());
                            }
                        }
                    }
                    break;
                }
                case MESSAGE:{
                    for(Object messageLine : coupon.getActions().get(Action.MESSAGE)){
                        try{
                            Yaml.sendSimpleMessage(player, coupon.getActions().get(Action.MESSAGE));
                        }catch (Exception e){
                            Logs.sendMessageLogError(plugin,"coupons/"+coupon.getName(), player.getName());
                        }
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
