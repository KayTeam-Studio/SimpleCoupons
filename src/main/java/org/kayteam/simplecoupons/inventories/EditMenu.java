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

package org.kayteam.simplecoupons.inventories;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.kayteam.simplecoupons.SimpleCoupons;
import org.kayteam.simplecoupons.commands.Command_Delete;
import org.kayteam.simplecoupons.coupon.Coupon;
import org.kayteam.simplecoupons.coupon.CouponManager;
import org.kayteam.simplecoupons.inputs.*;
import org.kayteam.simplecoupons.util.Color;
import org.kayteam.simplecoupons.util.Yaml;
import org.kayteam.simplecoupons.util.interact.Interact;
import org.kayteam.simplecoupons.util.inventory.Item;
import org.kayteam.simplecoupons.util.inventory.MenuInventory;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EditMenu extends MenuInventory {

    private SimpleCoupons plugin;
    private Coupon coupon;

    public EditMenu(SimpleCoupons plugin, Coupon coupon) {
        this.plugin = plugin;
        this.coupon = coupon;
        CouponManager couponManager = plugin.getCouponManager();
        Yaml messages = plugin.getMessagesYaml();
        Yaml config = plugin.getConfigYaml();

        // FILL ITEM
        for (int i = 0; i<45; i++){
            addMenuAction(i, new Item() {
                @Override
                public ItemStack getItem() {
                    return config.getItemStack("menu.list.items.fill");
                }
            });
        }

        // CLOSE ITEM
        addMenuAction(28, new Item() {
            @Override
            public ItemStack getItem() {
                return config.getItemStack("menu.list.items.close");
            }

            @Override
            public void onLeftClick(Player player) {
                player.closeInventory();
            }
        });

        // CHANGE COUPON ITEM
        addMenuAction(10, new Item() {
            @Override
            public ItemStack getItem() {
                return coupon.getCouponItem();
            }

            @Override
            public void onLeftClick(Player player) {
                player.closeInventory();
                messages.sendMessage(player, "edit.item",
                        new String[][]{{"%path%", coupon.getName()+"/item"}, {"%value%", "valid item"}});
                plugin.getInteractManager().addInteract(player, new Interact() {
                    @Override
                    public boolean onInteract(Player player, ItemStack input) {
                        if(!input.getType().equals(Material.AIR)){
                            coupon.setCouponItem(input);
                            couponManager.saveCoupon(coupon);
                            Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
                                @Override
                                public void run() {
                                    plugin.getServer().dispatchCommand(player, "sc edit "+coupon.getName());
                                }
                            }, 1);
                            return true;
                        }else{
                            messages.sendMessage(player, "edit.item",
                                    new String[][]{{"%path%", coupon.getName()+"/item"}, {"%value%", "valid item"}});
                            return false;
                        }
                    }

                    @Override
                    public void onPlayerSneak(Player player) {
                        Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
                            @Override
                            public void run() {
                                plugin.getServer().dispatchCommand(player, "sc edit "+coupon.getName());
                            }
                        }, 1);
                    }
                });
            }
        });

        // CHANGE MONEY ITEM
        addMenuAction(12, new Item() {
            @Override
            public ItemStack getItem() {
                try{
                    return Yaml.replace(config.getItemStack("menu.edit.items.money"),
                            new String[][]{{"%coupon_money%", String.valueOf(coupon.getMoney())}});
                }catch (Exception e){
                    return Yaml.replace(config.getItemStack("menu.edit.items.money"),
                            new String[][]{{"%coupon_money%", "0"}});
                }
            }

            @Override
            public void onLeftClick(Player player) {
                player.closeInventory();
                messages.sendMessage(player, "edit.chat",
                        new String[][]{{"%path%", coupon.getName()+"/money"}, {"%value%", "valid number"}});
                new MoneyInput(plugin, coupon).addMoneyInput(player);
            }
        });

        // CHANGE XP ITEM
        addMenuAction(14, new Item() {
            @Override
            public ItemStack getItem() {
                try{
                    return Yaml.replace(config.getItemStack("menu.edit.items.xp"),
                            new String[][]{{"%coupon_xp%", String.valueOf(coupon.getXp())}});
                }catch (Exception e){
                    return Yaml.replace(config.getItemStack("menu.edit.items.xp"),
                            new String[][]{{"%coupon_xp%", "0"}});
                }
            }

            @Override
            public void onLeftClick(Player player) {
                player.closeInventory();
                messages.sendMessage(player, "edit.chat",
                        new String[][]{{"%path%", coupon.getName()+"/xp"}, {"%value%", "valid number"}});
                new XPInput(plugin, coupon).addXPInput(player);
            }
        });

        // CHANGE ITEMS REWARD
        addMenuAction(16, new Item() {
            @Override
            public ItemStack getItem() {
                try{
                    return Yaml.replace(plugin.getConfigYaml().getItemStack("menu.edit.items.items"),
                            new String[][]{{"%coupon_items%", String.valueOf(coupon.getItems().size())}});
                }catch (Exception e){
                    return Yaml.replace(plugin.getConfigYaml().getItemStack("menu.edit.items.items"),
                            new String[][]{{"%coupon_items%", "0"}});
                }
            }

            @Override
            public void onLeftClick(Player player) {
                new ItemsImput(plugin, coupon).addItemsInput(player);
            }
        });

        // CHANGE COMMANDS
        addMenuAction(30, new Item() {
            @Override
            public ItemStack getItem() {
                try{
                    return Yaml.replace(config.getItemStack("menu.edit.items.commands"),
                            new String[][]{{"%coupon_commands%", String.valueOf(coupon.getCommands().size())}});
                }catch (Exception e){
                    return Yaml.replace(config.getItemStack("menu.edit.items.commands"), new String[][]{{"%coupon_commands%", "0"}});
                }
            }

            @Override
            public void onLeftClick(Player player) {
                player.closeInventory();
                messages.sendMessage(player, "edit.chat",
                        new String[][]{{"%path%", coupon.getName()+"/commands"}, {"%value%", "valid command"}});
                new CommandsInput(plugin, coupon).addCommandInput(player);
            }

            @Override
            public void onRightClick(Player player) {
                if(coupon.getCommands().size() > 0){
                    player.closeInventory();
                    List<Object> index = new ArrayList<>();
                    for(int i = 0; i < coupon.getCommands().size(); i++){
                        index.add(i);
                    }
                    plugin.getMenuInventoryManager().openInventory(player, new CommandsEditMenu(plugin, index, coupon));
                }
            }
        });

        // CHANGE MESSAGES
        addMenuAction(32, new Item() {
            @Override
            public ItemStack getItem() {
                try{
                    return Yaml.replace(config.getItemStack("menu.edit.items.messages"),
                            new String[][]{{"%coupon_messages%", String.valueOf(coupon.getMessages().size())}});
                }catch (Exception e){
                    return Yaml.replace(config.getItemStack("menu.edit.items.messages"), new String[][]{{"%coupon_messages%", "0"}});
                }
            }

            @Override
            public void onLeftClick(Player player) {
                player.closeInventory();
                messages.sendMessage(player, "edit.chat",
                        new String[][]{{"%path%", coupon.getName()+"/messages"}, {"%value%", "custom message"}});
                new MessagesInput(plugin, coupon).addMessageInput(player);
            }

            @Override
            public void onRightClick(Player player) {
                if(coupon.getMessages().size() > 0){
                    player.closeInventory();
                    List<Object> index = new ArrayList<>();
                    for(int i = 0; i < coupon.getMessages().size(); i++){
                        index.add(i);
                    }
                    plugin.getMenuInventoryManager().openInventory(player, new MessagesEditMenu(plugin, index, coupon));
                }
            }
        });

        // DELETE COUPON ITEM
        addMenuAction(34, new Item() {
            @Override
            public ItemStack getItem() {
                return config.getItemStack("menu.edit.items.delete");
            }

            @Override
            public void onLeftClick(Player player) {
                player.closeInventory();
                new Command_Delete(plugin).openDeleteMenu(player, coupon);
            }
        });
    }

    @Override
    public String getTitle() {
        return plugin.getConfigYaml().getString("menu.edit.title");
    }

    @Override
    public int getRows() {
        return 5;
    }
}
