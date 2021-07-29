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
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.kayteam.simplecoupons.commands.Command_SimpleCoupons;
import org.kayteam.simplecoupons.coupon.CouponManager;
import org.kayteam.simplecoupons.listeners.PlayerInteract;
import org.kayteam.simplecoupons.util.KayTeam;
import org.kayteam.simplecoupons.util.Metrics;
import org.kayteam.simplecoupons.util.UpdateChecker;
import org.kayteam.simplecoupons.util.Yaml;

import java.io.File;

public class SimpleCoupons extends JavaPlugin {

    private CouponManager couponManager = new CouponManager(this);
    private static Economy econ = null;
    private Yaml Example = new Yaml(this, "coupons", "Example");
    private Yaml config = new Yaml(this, "config");

    @Override
    public void onEnable() {
        KayTeam.sendBrandMessage("enabled", this);
        enablebStats();
        setupEconomy();
        registerCommands();
        registerListeners();
        registerFiles();
        loadCoupons();
        checkUpdates();
    }

    private UpdateChecker updateChecker;
    public UpdateChecker getUpdateChecker() {
        return updateChecker;
    }

    private void checkUpdates() {
    /*
        updateChecker = new UpdateChecker(this, 94734);
        if (updateChecker.getUpdateCheckResult().equals(UpdateChecker.UpdateCheckResult.OUT_DATED)) {
            updateChecker.sendOutDatedMessage(getServer().getConsoleSender());
        }
    */
    }

    private void loadCoupons() {
        for (File file : Yaml.getFolderFiles(getDataFolder()+"/coupons")) {
            couponManager.loadCoupon((file.getName()).replaceAll(".yml", ""));
        }
    }

    private void registerFiles() {
        Example.registerFileConfiguration();
        config.registerFileConfiguration();
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new PlayerInteract(this), this);
    }

    private void registerCommands() {
        getCommand("simplecoupons").setExecutor(new Command_SimpleCoupons(this));
    }

    private void enablebStats() {
        int pluginId = 12232;
        Metrics metrics = new Metrics(this, pluginId);
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public CouponManager getCouponManager(){
        return couponManager;
    }

    public static Economy getEconomy() {
        return econ;
    }

    @Override
    public void onDisable() {
        KayTeam.sendBrandMessage("disabled", this);
    }
}
