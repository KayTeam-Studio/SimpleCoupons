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

package org.kayteam.simplecoupons.commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.kayteam.simplecoupons.SimpleCoupons;
import org.kayteam.simplecoupons.coupon.Action;
import org.kayteam.simplecoupons.coupon.Coupon;
import org.kayteam.simplecoupons.coupon.CouponManager;
import org.kayteam.simplecoupons.inventories.CouponsMenu;
import org.kayteam.simplecoupons.util.Yaml;
import org.kayteam.simplecoupons.util.chat.ChatInput;
import org.kayteam.simplecoupons.util.interact.Interact;
import org.kayteam.simplecoupons.util.inventory.MenuItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Command_Edit {

    private SimpleCoupons plugin;

    public Command_Edit(SimpleCoupons plugin) {
        this.plugin = plugin;
    }

    public void editCoupon(Player player, Coupon coupon){
        if(coupon!=null){
            HashMap<Integer, MenuItem> items = new HashMap<>();
            Yaml config = plugin.getConfigYaml();
            Yaml messages = plugin.getMessagesYaml();
            CouponManager couponManager = plugin.getCouponManager();
            // FILL
            for (int i = 0; i<45; i++){
                items.put(i, new MenuItem() {
                    @Override
                    public ItemStack getItem() {
                        return plugin.getConfigYaml().getItemStack("menu.items.fill");
                    }
                });
            }
            // CLOSE
            items.put(28, new MenuItem() {
                @Override
                public ItemStack getItem() {
                    return plugin.getConfigYaml().getItemStack("menu.items.close");
                }

                @Override
                public void onLeftClick(Player player) {
                    player.closeInventory();
                }
            });
            // CHANGE COUPON ITEM
            items.put(10, new MenuItem() {
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
            // MONEY
            items.put(12, new MenuItem() {
                @Override
                public ItemStack getItem() {
                    try{
                        return Yaml.replace(config.getItemStack("menu.edit.items.money"),
                                new String[][]{{"%coupon_money%", String.valueOf(coupon.getActions().get(Action.MONEY).get(0))}});
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
                    plugin.getChatInputManager().addChatInput(player, new ChatInput(new HashMap<>()) {
                        @Override
                        public boolean onChatInput(Player player, String input) {
                            try{
                                int newMoney = Integer.parseInt(input);
                                coupon.getActions().get(Action.MONEY).clear();
                                List<Object> money = new ArrayList<>();
                                money.add(newMoney);
                                coupon.getActions().get(Action.MONEY).set(0, money);
                                couponManager.saveCoupon(coupon);
                                couponManager.loadCoupon(coupon.getName());
                                Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
                                    @Override
                                    public void run() {
                                        plugin.getServer().dispatchCommand(player, "sc edit "+coupon.getName());
                                    }
                                }, 1);
                                return true;
                            }catch (Exception e){
                                messages.sendMessage(player, "edit.chat",
                                        new String[][]{{"%path%", coupon.getName()+"/money"}, {"%value%", "valid number"}});
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
            // XP
            items.put(14, new MenuItem() {
                @Override
                public ItemStack getItem() {
                    try{
                        return Yaml.replace(config.getItemStack("menu.edit.items.xp"),
                                new String[][]{{"%coupon_xp%", String.valueOf(coupon.getActions().get(Action.XP).get(0))}});
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
                    plugin.getChatInputManager().addChatInput(player, new ChatInput(new HashMap<>()) {
                        @Override
                        public boolean onChatInput(Player player, String input) {
                            try{
                                int newXP = Integer.parseInt(input);
                                coupon.getActions().get(Action.XP).clear();
                                List<Object> xp = new ArrayList<>();
                                xp.add(newXP);
                                coupon.getActions().get(Action.XP).set(0, xp);
                                couponManager.saveCoupon(coupon);
                                couponManager.loadCoupon(coupon.getName());
                                Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
                                    @Override
                                    public void run() {
                                        plugin.getServer().dispatchCommand(player, "sc edit "+coupon.getName());
                                    }
                                }, 1);
                                return true;
                            }catch (Exception e){
                                messages.sendMessage(player, "edit.chat",
                                        new String[][]{{"%path%", coupon.getName()+"/xp"}, {"%value%", "valid number"}});
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
            // ITEMS
            items.put(16, new MenuItem() {
                @Override
                public ItemStack getItem() {
                    return null;
                }
            });
            // COMMANDS
            items.put(30, new MenuItem() {
                @Override
                public ItemStack getItem() {
                    return null;
                }
            });
            // MESSAGES
            items.put(32, new MenuItem() {
                @Override
                public ItemStack getItem() {
                    return null;
                }
            });
            // DELETE COUPON
            items.put(34, new MenuItem() {
                @Override
                public ItemStack getItem() {
                    return config.getItemStack("menu.edit.items.delete");
                }

                @Override
                public void onLeftClick(Player player) {

                }
            });
            player.closeInventory();
            plugin.getMenuInventoryManager().openInventory(player, plugin.getConfigYaml().getString("menu.edit.title").replaceAll("%coupon_name%", coupon.getName()), 45, items);
        }
    }
}
