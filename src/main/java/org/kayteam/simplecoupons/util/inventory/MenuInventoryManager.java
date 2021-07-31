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

package org.kayteam.simplecoupons.util.inventory;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;

public class MenuInventoryManager implements Listener {

    private final static HashMap<String, HashMap<Integer, MenuItem>> actions = new HashMap<>();

    public void openInventory(Player player, String title, int slots, HashMap<Integer, MenuItem> actions) {
        MenuInventoryManager.actions.put(player.getName(), actions);
        Inventory inventory = Bukkit.createInventory(null, slots, ChatColor.translateAlternateColorCodes('&', title));
        for (int slot:actions.keySet()) {
            MenuItem action = actions.get(slot);
            inventory.setItem(slot, action.getItem());
        }
        // Open Inventory
        player.openInventory(inventory);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (MenuInventoryManager.actions.containsKey(player.getName())) {
            event.setCancelled(true);
            int slot = event.getRawSlot();
            HashMap<Integer, MenuItem> actions = MenuInventoryManager.actions.get(player.getName());
            if (actions.containsKey(slot)) {
                MenuItem action = actions.get(slot);
                switch (event.getClick()) {
                    case LEFT:
                        action.onLeftClick(player);
                        break;
                    case RIGHT:
                        action.onRightClick(player);
                        break;
                    case MIDDLE:
                        action.onMiddleClick(player);
                        break;
                    case SHIFT_LEFT:
                        action.onShiftLeftClick(player);
                        break;
                    case SHIFT_RIGHT:
                        action.onShiftRightClick(player);
                        break;
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        if (MenuInventoryManager.actions.containsKey(player.getName())) {
            MenuInventoryManager.actions.remove(player.getName());
        }
    }
}
