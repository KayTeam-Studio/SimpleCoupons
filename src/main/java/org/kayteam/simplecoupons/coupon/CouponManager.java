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
import org.kayteam.storageapi.storage.YML;
import org.kayteam.storageapi.storage.Yaml;

import java.util.*;

public class CouponManager {
    private final SimpleCoupons plugin;

    Map<String, Coupon> coupons;

    public CouponManager(SimpleCoupons plugin) {
        this.coupons = new HashMap<>();
        this.plugin = plugin;
    }

    public Map<String, Coupon> getCoupons() {
        return this.coupons;
    }

    public void loadCoupons() {
        for (Yaml yaml : Yaml.getYamlFiles(this.plugin.getDataFolder() + "/coupons"))
            loadCoupon(yaml.getName().replaceAll(".yml", ""));
    }

    public void loadCoupon(String couponName) {
        YML couponYaml = new YML(this.plugin, "coupons", couponName);
        couponYaml.register();
        System.out.println(couponName);
        if (couponYaml.getItemStack("item") != null) {
            Coupon coupon = new Coupon(couponName);
            coupon.setCouponItem(couponYaml.getItemStack("item"));
            if (couponYaml.contains("rewards")) {
                if (couponYaml.contains("rewards.xp"))
                    if (couponYaml.isInt("rewards.xp")) {
                        try {
                            coupon.setXp(couponYaml.getInt("rewards.xp"));
                        } catch (Exception e) {
                            Logs.sendLoadLogError(this.plugin, "rewards/xp", "coupons/" + couponName);
                        }
                    } else {
                        Logs.sendLoadLogError(this.plugin, "rewards/xp", "coupons/" + couponName);
                    }
                if (couponYaml.contains("rewards.money"))
                    if (couponYaml.isInt("rewards.money")) {
                        try {
                            coupon.setMoney(couponYaml.getInt("rewards.money"));
                        } catch (Exception e) {
                            Logs.sendLoadLogError(this.plugin, "rewards/money", "coupons/" + couponName);
                        }
                    } else {
                        Logs.sendLoadLogError(this.plugin, "rewards/money", "coupons/" + couponName);
                    }
                if (couponYaml.contains("rewards.items"))
                    for (String itemName : Objects.requireNonNull(couponYaml.getFileConfiguration().getConfigurationSection("rewards.items")).getKeys(false)) {
                        if (couponYaml.getItemStack("rewards.items." + itemName) != null) {
                            try {
                                coupon.getItems().add(couponYaml.getItemStack("rewards.items." + itemName));
                            } catch (Exception e) {
                                Logs.sendLoadLogError(this.plugin, "rewards/items/" + itemName, "coupons/" + couponName);
                            }
                            continue;
                        }
                        Logs.sendLoadLogError(this.plugin, "rewards/items/" + itemName, "coupons/" + couponName);
                    }
                if (couponYaml.contains("rewards.messages"))
                    if (couponYaml.isStringList("rewards.messages")) {
                        try {
                            coupon.setMessages(couponYaml.getStringList("rewards.messages"));
                        } catch (Exception e) {
                            Logs.sendLoadLogError(this.plugin, "rewards/messages", "coupons/" + couponName);
                        }
                    } else if (couponYaml.isString("rewards.messages")) {
                        try {
                            List<String> messages = new ArrayList<>();
                            messages.add(couponYaml.getString("rewards.messages"));
                            coupon.setMessages(messages);
                        } catch (Exception e) {
                            Logs.sendLoadLogError(this.plugin, "rewards/messages", "coupons/" + couponName);
                        }
                    } else {
                        Logs.sendLoadLogError(this.plugin, "rewards/messages", "coupons/" + couponName);
                    }
                if (couponYaml.contains("rewards.commands"))
                    if (couponYaml.isStringList("rewards.commands")) {
                        try {
                            coupon.setCommands(couponYaml.getStringList("rewards.commands"));
                        } catch (Exception e) {
                            Logs.sendLoadLogError(this.plugin, "rewards/commands", "coupons/" + couponName);
                        }
                    } else if (couponYaml.isString("rewards.commands")) {
                        try {
                            List<String> commands = new ArrayList<>();
                            commands.add(couponYaml.getString("rewards.commands"));
                            coupon.setCommands(commands);
                        } catch (Exception e) {
                            Logs.sendLoadLogError(this.plugin, "rewards/commands", "coupons/" + couponName);
                        }
                    } else {
                        Logs.sendLoadLogError(this.plugin, "rewards/commands", "coupons/" + couponName);
                    }
                if (couponYaml.contains("settings.uses"))
                    if (couponYaml.isInt("settings.uses")) {
                        try {
                            coupon.setUses(couponYaml.getInt("settings.uses"));
                        } catch (Exception e) {
                            Logs.sendLoadLogError(this.plugin, "settings/uses", "coupons/" + couponName);
                        }
                    } else {
                        Logs.sendLoadLogError(this.plugin, "settings/uses", "coupons/" + couponName);
                    }
                if (couponYaml.contains("settings.permission"))
                    if (couponYaml.isString("settings.permission")) {
                        try {
                            if (!couponYaml.getString("settings.permission").equalsIgnoreCase("none"))
                                coupon.setPermission(couponYaml.getString("settings.permission"));
                        } catch (Exception e) {
                            Logs.sendLoadLogError(this.plugin, "settings/permission", "coupons/" + couponName);
                        }
                    } else {
                        Logs.sendLoadLogError(this.plugin, "settings/permission", "coupons/" + couponName);
                    }
            }
            getCoupons().put(couponName, coupon);
            Logs.sendCorrectCouponLoadLog(this.plugin, couponName);
        } else {
            Logs.sendGetItemLogError(this.plugin, "coupons/" + couponName);
        }
    }

    public void giveCoupon(String couponName, Player player) {
        Coupon coupon = getCoupons().get(couponName);
        if (coupon.getCouponItem() != null) {
            ItemStack item = coupon.getCouponItem();
            NBTItem nbti = new NBTItem(item);
            nbti.setString("coupon-name", couponName);
            nbti.setInteger("coupon-uses", Integer.valueOf(coupon.getUses()));
            player.getInventory().addItem(nbti.getItem());
        } else {
            Logs.sendGetItemLogError(this.plugin, "coupons/" + couponName);
        }
    }

    public void saveCoupon(Coupon coupon) {
        if (coupon != null) {
            YML couponYaml = new YML(this.plugin, "coupons", coupon.getName());
            couponYaml.register();
            couponYaml.set("item", null);
            couponYaml.set("rewards", null);
            couponYaml.set("settings", null);
            couponYaml.setItemStack("item", coupon.getCouponItem());
            couponYaml.set("rewards.money", Integer.valueOf(coupon.getMoney()));
            couponYaml.set("rewards.xp", Integer.valueOf(coupon.getXp()));
            int i = 0;
            for (ItemStack item : coupon.getItems()) {
                if (item != null) {
                    couponYaml.setItemStack("rewards.items." + i, item);
                    i++;
                }
            }
            couponYaml.set("rewards.commands", coupon.getCommands());
            couponYaml.set("rewards.messages", coupon.getMessages());
            couponYaml.set("settings.uses", Integer.valueOf(coupon.getUses()));
            couponYaml.set("settings.permission", coupon.getPermission());
            couponYaml.save();
        }
    }

    public boolean deleteCoupon(Coupon coupon) {
        YML couponYaml = new YML(this.plugin, "coupons", coupon.getName());
        if (couponYaml.delete()) {
            unloadCoupon(coupon);
            return true;
        }
        return false;
    }

    public void unloadCoupon(Coupon coupon) {
        getCoupons().remove(coupon.getName(), coupon);
    }

    public void executeCouponActions(Coupon coupon, Player player) {
        try {
            player.giveExpLevels(coupon.getXp());
        } catch (Exception e) {
            Logs.sendGiveXPLogError(this.plugin, "coupons/" + coupon.getName(), player.getName());
        }
        try {
            if (coupon.getMoney() > 0) {
                SimpleCoupons.getEconomy().depositPlayer(player, coupon.getMoney());
            } else {
                SimpleCoupons.getEconomy().withdrawPlayer(player, Math.abs(coupon.getMoney()));
            }
        } catch (Exception e) {
            Logs.sendGiveMoneyLogError(this.plugin, "coupons/" + coupon.getName(), player.getName());
        }
        for (ItemStack item : coupon.getItems()) {
            try {
                if (player.getInventory().firstEmpty() != -1) {
                    player.getInventory().addItem(item);
                    continue;
                }
                Objects.requireNonNull(player.getLocation().getWorld()).dropItem(player.getLocation(), item);
            } catch (Exception ex) {
                Logs.sendGiveItemLogError(this.plugin, item.toString(), "coupons/" + coupon.getName(), player.getName());
            }
        }
        try {
            Yaml.sendSimpleMessage(player, coupon.getMessages(), new String[][]{{"%player%", player.getName()}});
        } catch (Exception e) {
            Logs.sendMessageLogError(this.plugin, "coupons/" + coupon.getName(), player.getName());
        }
        for (String commandLine : coupon.getCommands()) {
            try {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commandLine.replaceAll("%player%", player.getName()));
            } catch (Exception e) {
                Logs.sendExecuteLogError(this.plugin, commandLine, "coupons/" + coupon.getName());
            }
        }
    }
}
