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

package org.kayteam.simplecoupons;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.kayteam.inputapi.InputManager;
import org.kayteam.inventoryapi.InventoryManager;
import org.kayteam.simplecoupons.commands.Command_SimpleCoupons;
import org.kayteam.simplecoupons.coupon.CouponManager;
import org.kayteam.simplecoupons.inventories.CouponsMenu;
import org.kayteam.simplecoupons.listeners.CouponUse;
import org.kayteam.simplecoupons.listeners.PlayerInteract;
import org.kayteam.simplecoupons.util.CommandManager;
import org.kayteam.simplecoupons.util.Metrics;
import org.kayteam.simplecoupons.util.UpdateChecker;
import org.kayteam.storageapi.storage.YML;
import org.kayteam.storageapi.utils.BrandSender;

public class SimpleCoupons extends JavaPlugin {
    private final CouponManager couponManager = new CouponManager(this);

    private final CommandManager commandManager = new CommandManager(this);

    private static Economy econ = null;

    private final YML config = new YML(this, "config");

    private final YML messages = new YML(this, "messages");

    private UpdateChecker updateChecker;

    public void onEnable() {
        BrandSender.onEnable(this);
        registerFiles();
        setupEconomy();
        registerCommands();
        this.couponManager.loadCoupons();
        this.couponsMenu = new CouponsMenu(this, 0);
        registerListeners();
        inventoryManager.registerManager();
        inputManager.registerManager();
        enablebStats();
        this.updateChecker = new UpdateChecker(this, 95021);
        if (this.updateChecker.getUpdateCheckResult().equals(UpdateChecker.UpdateCheckResult.OUT_DATED))
            this.updateChecker.sendOutDatedMessage(getServer().getConsoleSender());
    }

    public UpdateChecker getUpdateChecker() {
        return this.updateChecker;
    }

    public InventoryManager inventoryManager = new InventoryManager(this);

    public InventoryManager getInventoryManager() {
        return this.inventoryManager;
    }

    public InputManager inputManager = new InputManager(this);

    private CouponsMenu couponsMenu;

    public InputManager getInputManager() {
        return this.inputManager;
    }

    public CouponsMenu getCouponsMenu() {
        return this.couponsMenu;
    }

    private void registerFiles() {
        try {
            if (YML.getYMLFiles(getDataFolder() + "/coupons").isEmpty()) {
                YML example = new YML(this, "coupons", "Example");
                example.register();
            }
        } catch (Exception e) {
            YML example = new YML(this, "coupons", "Example");
            example.register();
        }
        this.config.register();
        this.messages.register();
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new PlayerInteract(this), this);
        getServer().getPluginManager().registerEvents(new CouponUse(this), this);
    }

    private void registerCommands() {
        getCommand("simplecoupons").setExecutor(new Command_SimpleCoupons(this));
    }

    private void enablebStats() {
        int pluginId = 12232;
        Metrics metrics = new Metrics(this, pluginId);
        metrics.addCustomChart(new Metrics.SingleLineChart("coupons", () -> {
            int coupons = 0;
            coupons = getCouponManager().getCoupons().keySet().size();
            return Integer.valueOf(coupons);
        }));
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null)
            return false;
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null)
            return false;
        econ = rsp.getProvider();
        return (econ != null);
    }

    public CouponManager getCouponManager() {
        return this.couponManager;
    }

    public CommandManager getCommandManager() {
        return this.commandManager;
    }

    public static Economy getEconomy() {
        return econ;
    }

    public YML getMessagesYaml() {
        return this.messages;
    }

    public YML getConfigYaml() {
        return this.config;
    }

    public void onDisable() {
        BrandSender.onDisable(this);
    }
}
