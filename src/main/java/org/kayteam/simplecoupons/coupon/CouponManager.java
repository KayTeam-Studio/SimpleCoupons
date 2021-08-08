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
import java.util.*;

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
            Coupon coupon = new Coupon(couponName);
            coupon.setCouponItem(couponYaml.getItemStack("item"));
            if(couponYaml.contains("rewards")){
                // LOAD XP
                if(couponYaml.contains("rewards.xp")){
                    if(couponYaml.isInt("rewards.xp")){
                        try{
                            coupon.setXp(couponYaml.getInt("rewards.xp"));
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
                            coupon.setMoney(couponYaml.getInt("rewards.money"));
                        }catch (Exception e){
                            Logs.sendLoadLogError(plugin,"rewards/money", "coupons/"+couponName);
                        }
                    }else{
                        Logs.sendLoadLogError(plugin,"rewards/money", "coupons/"+couponName);
                    }
                }

                // LOAD ITEMS
                if(couponYaml.contains("rewards.items")){
                    for(String itemName : Objects.requireNonNull(couponYaml.getFileConfiguration().getConfigurationSection("rewards.items")).getKeys(false)){
                        if(couponYaml.getItemStack("rewards.items."+itemName) != null){
                            try{
                                coupon.getItems().add(couponYaml.getItemStack("rewards.items."+itemName));
                            }catch (Exception e){
                                Logs.sendLoadLogError(plugin,"rewards/items/"+itemName, "coupons/"+couponName);
                            }
                        }else{
                            Logs.sendLoadLogError(plugin,"rewards/items/"+itemName, "coupons/"+couponName);
                        }
                    }
                }

                // LOAD MESSAGES
                if(couponYaml.contains("rewards.messages")){
                    if(couponYaml.isList("rewards.messages")){
                        try{
                            coupon.setMessages(couponYaml.getStringList("rewards.messages"));
                        }catch (Exception e){
                            Logs.sendLoadLogError(plugin,"rewards/messages", "coupons/"+couponName);
                        }
                    }else if(couponYaml.isString("rewards.messages")){
                        try{
                            List<String> messages = new ArrayList<>();
                            messages.add(couponYaml.getString("rewards.messages"));
                            coupon.setMessages(messages);
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
                            coupon.setCommands(couponYaml.getStringList("rewards.commands"));
                        }catch (Exception e){
                            Logs.sendLoadLogError(plugin,"rewards/commands", "coupons/"+couponName);
                        }
                    }else if(couponYaml.isString("rewards.commands")){
                        try{
                            List<String> commands = new ArrayList<>();
                            commands.add(couponYaml.getString("rewards.commands"));
                            coupon.setCommands(commands);
                        }catch (Exception e){
                            Logs.sendLoadLogError(plugin,"rewards/commands", "coupons/"+couponName);
                        }
                    }else{
                        Logs.sendLoadLogError(plugin,"rewards/commands", "coupons/"+couponName);
                    }
                }

                // LOAD COUPON MAX USES
                if(couponYaml.contains("settings.uses")){
                    if(couponYaml.isInt("settings.uses")){
                        try{
                            coupon.setUses(couponYaml.getInt("settings.uses"));
                        }catch (Exception e){
                            Logs.sendLoadLogError(plugin,"settings/uses", "coupons/"+couponName);
                        }
                    }else{
                        Logs.sendLoadLogError(plugin,"settings/uses", "coupons/"+couponName);
                    }
                }

                // LOAD COUPON PERMISSION
                if(couponYaml.contains("settings.permission")){
                    if(couponYaml.isString("settings.permission")){
                        try{
                            if(!couponYaml.getString("settings.permission").equalsIgnoreCase("none")){
                                coupon.setPermission(couponYaml.getString("settings.permission"));
                            }
                        }catch (Exception e){
                            Logs.sendLoadLogError(plugin,"settings/permission", "coupons/"+couponName);
                        }
                    }else{
                        Logs.sendLoadLogError(plugin,"settings/permission", "coupons/"+couponName);
                    }
                }
            }
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
        Coupon coupon = getCoupons().get(couponName);
        if(coupon.getCouponItem() != null){
            ItemStack item = coupon.getCouponItem();
            NBTItem nbti = new NBTItem(item);
            nbti.setString("coupon-name", couponName);
            nbti.setInteger("coupon-uses", coupon.getUses());
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
            couponYaml.set("settings", null);
            couponYaml.setItemStack("item", coupon.getCouponItem());

            // SAVE MONEY
            couponYaml.set("rewards.money", coupon.getMoney());

            // SAVE XP
            couponYaml.set("rewards.xp", coupon.getXp());

            // SAVE ITEMS
            int i = 0;
            for(ItemStack item : coupon.getItems()){
                if(item != null){
                    couponYaml.setItemStack("rewards.items."+i, item);
                    i++;
                }
            }

            // SAVE COMMANDS
            couponYaml.set("rewards.commands", coupon.getCommands());

            // SAVE MESSAGES
            couponYaml.set("rewards.messages", coupon.getMessages());

            // SAVE MAX USES
            couponYaml.set("settings.uses", coupon.getUses());

            // SAVE PERMISSION
            couponYaml.set("settings.permission", coupon.getPermission());

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
    public void executeCouponActions(Coupon coupon, Player player) {

        // GIVE XP
        try {
            player.giveExpLevels(coupon.getXp());
        } catch (Exception e) {
            Logs.sendGiveXPLogError(plugin, "coupons/" + coupon.getName(), player.getName());
        }

        // GIVE MONEY
        try {
            if(coupon.getMoney() > 0){
                SimpleCoupons.getEconomy().depositPlayer(player, coupon.getMoney());
            }else{
                SimpleCoupons.getEconomy().withdrawPlayer(player, Math.abs(coupon.getMoney()));
            }
        } catch (Exception e) {
            Logs.sendGiveMoneyLogError(plugin, "coupons/" + coupon.getName(), player.getName());
        }

        // GIVE ITEMS
        for (ItemStack item : coupon.getItems()) {
            try {
                if (player.getInventory().firstEmpty() != -1) {
                    player.getInventory().addItem(item);
                } else {
                    Objects.requireNonNull(player.getLocation().getWorld()).dropItem(player.getLocation(), item);
                }
            } catch (Exception ex) {
                Logs.sendGiveItemLogError(plugin, item.toString(), "coupons/" + coupon.getName(), player.getName());
            }
        }

        // SEND MESSAGES
        try {
            Yaml.sendSimpleMessage(player, coupon.getMessages(), new String[][]{{"%player%", player.getName()}});
        } catch (Exception e) {
            Logs.sendMessageLogError(plugin, "coupons/" + coupon.getName(), player.getName());
        }

        // EXECUTE COMMANDS
        for (String commandLine : coupon.getCommands()) {
            try {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), (commandLine).replaceAll("%player%", player.getName()));
            } catch (Exception e) {
                Logs.sendExecuteLogError(plugin, commandLine, "coupons/" + coupon.getName());
            }
        }
    }
}
