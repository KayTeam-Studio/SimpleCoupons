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

package org.kayteam.simplecoupons.util.chat;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

public class ChatInputManager implements Listener {

    private static final HashMap<String, ChatInput> inputs = new HashMap<>();

    public void addChatInput(Player player, ChatInput input) {
        inputs.put(player.getName(), input);
    }

    @EventHandler
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        // Getting Player
        Player player = event.getPlayer();
        // If exist Chat Input from player
        if (inputs.containsKey(player.getName())) {
            // Cancel chat event
            event.setCancelled(true);
            // Getting the Chat Input
            ChatInput chatInput = inputs.get(player.getName());
            // If onChatInput method return true
            if (chatInput.onChatInput(player, event.getMessage())) {
                // Removing Chat Input
                inputs.remove(player.getName());
            }
        }
    }

    @EventHandler
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        if (inputs.containsKey(event.getPlayer().getName())) {
            ChatInput chatInput = inputs.get(event.getPlayer().getName());
            chatInput.onPlayerSneak(event.getPlayer());
            inputs.remove(event.getPlayer().getName());
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        inputs.remove(event.getPlayer().getName());
    }

}