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

package org.kayteam.simplecoupons.util.inventoryitems;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.kayteam.simplecoupons.util.interact.Interact;

import java.util.HashMap;

public class InventoryItemsManager implements Listener {

    private static final HashMap<String, InventoryItems> inputs = new HashMap<>();
    private static final HashMap<String, Boolean> drag = new HashMap<>();

    public void addInteract(Player player, InventoryItems input, boolean enableMove) {
        inputs.put(player.getName(), input);
        drag.put(player.getName(), enableMove);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event){
        // Getting Player
        Player player = (Player) event.getPlayer();
        // If exist Inventory Input from player
        if (inputs.containsKey(player.getName())) {
            // Getting the Inventory Input
            InventoryItems inventoryInput = inputs.get(player.getName());
            // If onInventoryClose method return true
            if (inventoryInput.onInventoryClose(player, event.getInventory())) {
                // Removing Inventory Input
                inputs.remove(player.getName());
                drag.remove(player.getName());
            }
        }
    }

    @EventHandler
    public void onItemMove(InventoryInteractEvent event){
        Player player = (Player) event.getWhoClicked();
        if(drag.containsKey(player.getName())){
            if(!drag.get(player.getName())){
                Bukkit.getLogger().info(event.getHandlers().toString());
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        inputs.remove(event.getPlayer().getName());
        drag.remove(event.getPlayer().getName());
    }
}
