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

import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.kayteam.simplecoupons.SimpleCoupons;
import org.kayteam.simplecoupons.coupon.Action;
import org.kayteam.simplecoupons.coupon.Coupon;
import org.kayteam.simplecoupons.coupon.CouponManager;
import org.kayteam.simplecoupons.inventories.MessagesMenu;
import org.kayteam.simplecoupons.util.Color;
import org.kayteam.simplecoupons.util.Yaml;
import org.kayteam.simplecoupons.util.chat.ChatInput;
import org.kayteam.simplecoupons.util.interact.Interact;
import org.kayteam.simplecoupons.util.inventory.MenuItem;
import org.kayteam.simplecoupons.util.inventoryitems.InventoryItems;

import java.util.*;

public class Command_Edit {

    private SimpleCoupons plugin;

    public Command_Edit(SimpleCoupons plugin) {
        this.plugin = plugin;
    }

    public void editCoupon(Player player, Coupon coupon){
        if(coupon!=null){
            Yaml config = plugin.getConfigYaml();
            Yaml messages = plugin.getMessagesYaml();
            CouponManager couponManager = plugin.getCouponManager();
            HashMap<Integer, MenuItem> items = new HashMap<>();
            // FILL
            for (int i = 0; i<45; i++){
                items.put(i, new MenuItem() {
                    @Override
                    public ItemStack getItem() {
                        return plugin.getConfigYaml().getItemStack("menu.list.items.fill");
                    }
                });
            }
            // CLOSE
            items.put(28, new MenuItem() {
                @Override
                public ItemStack getItem() {
                    return plugin.getConfigYaml().getItemStack("menu.list.items.close");
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
                                List<Object> money = new ArrayList<>();
                                money.add(newMoney);
                                coupon.getActions().put(Action.MONEY, money);
                                couponManager.saveCoupon(coupon);
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
                                List<Object> xp = new ArrayList<>();
                                xp.add(newXP);
                                coupon.getActions().put(Action.XP, xp);
                                couponManager.saveCoupon(coupon);
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
                    try{
                        return Yaml.replace(plugin.getConfigYaml().getItemStack("menu.edit.items.items"),
                                new String[][]{{"%coupon_items%", String.valueOf(new ArrayList<>(coupon.getActions().get(Action.ITEM)).size())}});
                    }catch (Exception e){
                        return Yaml.replace(plugin.getConfigYaml().getItemStack("menu.edit.items.items"),
                                new String[][]{{"%coupon_items%", "0"}});
                    }
                }

                @Override
                public void onLeftClick(Player player) {
                    player.closeInventory();
                    Inventory inventory = Bukkit.createInventory(null, 54, plugin.getConfigYaml().getString("menu.items.title"));
                    if(coupon.getActions().get(Action.ITEM) != null){
                        for(Object item : coupon.getActions().get(Action.ITEM)){
                            if((ItemStack) item != null){
                                inventory.addItem((ItemStack) item);
                            }
                        }
                    }
                    Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
                        @Override
                        public void run() {
                            player.openInventory(inventory);
                        }
                    }, 1);
                    plugin.getInventoryItemsManager().addInteract(player, new InventoryItems() {
                        @Override
                        public boolean onInventoryClose(Player player, Inventory input) {
                            ItemStack[] content = input.getContents();
                            List<Object> newItems = new ArrayList<>();
                            for(ItemStack item : content){
                                if(item != null){
                                    if(!item.getType().isAir()){
                                        newItems.add(item);
                                    }
                                }
                            }
                            coupon.getActions().put(Action.ITEM, newItems);
                            couponManager.saveCoupon(coupon);
                            Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
                                @Override
                                public void run() {
                                    plugin.getServer().dispatchCommand(player, "sc edit "+coupon.getName());
                                }
                            }, 3);
                            return true;
                        }
                    }, true);
                }
            });
            // COMMANDS
            items.put(30, new MenuItem() {
                @Override
                public ItemStack getItem() {
                    try{
                        return Yaml.replace(config.getItemStack("menu.edit.items.commands"),
                                new String[][]{{"%coupon_commands%", String.valueOf(coupon.getActions().get(Action.COMMAND).size())}});
                    }catch (Exception e){
                        return Yaml.replace(config.getItemStack("menu.edit.items.commands"), new String[][]{{"%coupon_commands%", "0"}});
                    }
                }

                @Override
                public void onLeftClick(Player player) {
                    player.closeInventory();
                    messages.sendMessage(player, "edit.chat",
                            new String[][]{{"%path%", coupon.getName()+"/commands"}, {"%value%", "valid command"}});
                    plugin.getChatInputManager().addChatInput(player, new ChatInput(new HashMap<>()) {
                        @Override
                        public boolean onChatInput(Player player, String input) {
                            List<Object> commands = new ArrayList<>();
                            if(coupon.getActions().get(Action.COMMAND) !=null){
                                commands.addAll(coupon.getActions().get(Action.COMMAND));
                            }
                            commands.add(input);
                            coupon.getActions().put(Action.COMMAND, commands);
                            couponManager.saveCoupon(coupon);
                            Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
                                @Override
                                public void run() {
                                    plugin.getServer().dispatchCommand(player, "sc edit "+coupon.getName());
                                }
                            }, 1);
                            return true;
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

                @Override
                public void onRightClick(Player player) {
                    HashMap<Integer, MenuItem> items = new HashMap<>();
                    if(coupon.getActions().get(Action.COMMAND) != null){
                        int i = 0;
                        for(Object line : coupon.getActions().get(Action.COMMAND)){
                            NBTItem item = new NBTItem(Yaml.replace(config.getItemStack("menu.texts-menu.items.commands"),
                                    new String[][]{{"%command%", (String) line}}));
                            item.setInteger("command-line", i);
                            i++;
                        }
                        player.closeInventory();
                        plugin.getPagesInventoryManager().openInventory(player, new MessagesMenu(plugin, ));
                    plugin.getMenuInventoryManager().openInventory(player, config.getString("menu.texts-menu.title"), 54, items);
                    Inventory inventory = Bukkit.createInventory(null, 54, Color.convert(config.getString("menu.texts-menu.title")
                            .replaceAll("%path%", "commands")));
                        player.openInventory(inventory);
                        plugin.getInventoryItemsManager().addInteract(player, new InventoryItems() {
                            @Override
                            public boolean onInventoryClose(Player player, Inventory input) {
                                List<Integer> newLinesIndex = new ArrayList<>();
                                for(ItemStack item : input.getContents()){
                                    if(item != null){
                                        if(!item.getType().isAir()){
                                            NBTItem nbtItem = new NBTItem(item);
                                            if(nbtItem.getInteger("command-line") != null){
                                                newLinesIndex.add(nbtItem.getInteger("command-line"));
                                            }
                                        }
                                    }
                                }
                                List<Object> newCommands = new ArrayList<>();
                                for(Integer index : newLinesIndex){
                                    List<Object> oldLines = coupon.getActions().get(Action.COMMAND);
                                    if(oldLines.get(index) != null){
                                        newCommands.add(oldLines.get(index));
                                    }
                                }
                                coupon.getActions().put(Action.COMMAND, newCommands);
                                couponManager.saveCoupon(coupon);
                                Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
                                    @Override
                                    public void run() {
                                        plugin.getServer().dispatchCommand(player, "sc edit "+coupon.getName());
                                    }
                                }, 3);
                                return true;
                            }
                        }, false);
                    }
                }
            });
            // MESSAGES
            items.put(32, new MenuItem() {
                @Override
                public ItemStack getItem() {
                    try{
                        return Yaml.replace(config.getItemStack("menu.edit.items.messages"),
                                new String[][]{{"%coupon_messages%", String.valueOf(coupon.getActions().get(Action.MESSAGE).size())}});
                    }catch (Exception e){
                        return Yaml.replace(config.getItemStack("menu.edit.items.messages"), new String[][]{{"%coupon_messages%", "0"}});
                    }
                }

                @Override
                public void onLeftClick(Player player) {
                    player.closeInventory();
                    messages.sendMessage(player, "edit.chat",
                            new String[][]{{"%path%", coupon.getName()+"/messages"}, {"%value%", "custom message"}});
                    plugin.getChatInputManager().addChatInput(player, new ChatInput(new HashMap<>()) {
                        @Override
                        public boolean onChatInput(Player player, String input) {
                            List<Object> messages = new ArrayList<>();
                            if(coupon.getActions().get(Action.MESSAGE) !=null){
                                messages.addAll(coupon.getActions().get(Action.MESSAGE));
                            }
                            messages.add(input);
                            coupon.getActions().put(Action.MESSAGE, messages);
                            couponManager.saveCoupon(coupon);
                            Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
                                @Override
                                public void run() {
                                    plugin.getServer().dispatchCommand(player, "sc edit "+coupon.getName());
                                }
                            }, 1);
                            return true;
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

                @Override
                public void onRightClick(Player player) {
                    Inventory inventory = Bukkit.createInventory(null, 54, Color.convert(config.getString("menu.texts-menu.title")
                            .replaceAll("%path%", "messages")));
                    if(coupon.getActions().get(Action.MESSAGE) != null){
                        int i = 0;
                        for(Object line : coupon.getActions().get(Action.MESSAGE)){
                            NBTItem item = new NBTItem(Yaml.replace(config.getItemStack("menu.texts-menu.items.messages"),
                                    new String[][]{{"%message%", (String) line}}));
                            item.setInteger("message-line", i);
                            inventory.addItem(item.getItem());
                            i++;
                        }
                        player.openInventory(inventory);
                        plugin.getInventoryItemsManager().addInteract(player, new InventoryItems() {
                            @Override
                            public boolean onInventoryClose(Player player, Inventory input) {
                                List<Integer> newLinesIndex = new ArrayList<>();
                                for(ItemStack item : input.getContents()){
                                    if(item != null){
                                        if(!item.getType().isAir()){
                                            NBTItem nbtItem = new NBTItem(item);
                                            if(nbtItem.getInteger("message-line") != null){
                                                newLinesIndex.add(nbtItem.getInteger("message-line"));
                                            }
                                        }
                                    }
                                }
                                List<Object> newMessages = new ArrayList<>();
                                for(Integer index : newLinesIndex){
                                    List<Object> oldLines = coupon.getActions().get(Action.MESSAGE);
                                    if(oldLines.get(index) != null){
                                        newMessages.add(oldLines.get(index));
                                    }
                                }
                                coupon.getActions().put(Action.MESSAGE, newMessages);
                                couponManager.saveCoupon(coupon);
                                Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
                                    @Override
                                    public void run() {
                                        plugin.getServer().dispatchCommand(player, "sc edit "+coupon.getName());
                                    }
                                }, 3);
                                return true;
                            }
                        }, false);
                    }
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
                    Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
                        @Override
                        public void run() {
                            plugin.getServer().dispatchCommand(player, "sc delete "+coupon.getName());
                        }
                    }, 1);
                }
            });
            player.closeInventory();
            plugin.getMenuInventoryManager().openInventory(player, plugin.getConfigYaml().getString("menu.edit.title").replaceAll("%coupon_name%", coupon.getName()), 45, items);
        }
    }
}
