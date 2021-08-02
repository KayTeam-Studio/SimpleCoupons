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

package org.kayteam.simplecoupons.util.interact;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import java.util.HashMap;

public class InteractManager implements Listener {

    private static final HashMap<String, Interact> inputs = new HashMap<>();

    public void addInteract(Player player, Interact input) {
        inputs.put(player.getName(), input);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event){
        // Getting Player
        Player player = event.getPlayer();
        // If exist Interact Input from player
        if (inputs.containsKey(player.getName())) {
            // Cancel interact event
            event.setCancelled(true);
            // Getting the Interact Input
            Interact interactInput = inputs.get(player.getName());
            // If onInteractInput method return true
            if (interactInput.onInteract(player, event.getItem())) {
                // Removing Interact Input
                inputs.remove(player.getName());
            }
        }
    }

    @EventHandler
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        if (inputs.containsKey(event.getPlayer().getName())) {
            Interact interactInput = inputs.get(event.getPlayer().getName());
            interactInput.onPlayerSneak(event.getPlayer());
            inputs.remove(event.getPlayer().getName());
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        inputs.remove(event.getPlayer().getName());
    }
}
