package com.nextplugins.nextmarket.hook.model.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.nextplugins.nextmarket.hook.model.CurrencyHook;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.logging.Logger;

@Singleton
public final class CoinsHook implements CurrencyHook {

    @Inject @Named("main") private Logger logger;
    private Economy economy;

    public void init() {
        RegisteredServiceProvider<Economy> registration = Bukkit.getServicesManager().getRegistration(Economy.class);
        if (registration == null) {
            logger.severe("Não foi encontrado nenhum plugin de economia no servidor!");
        } else {
            economy = registration.getProvider();
        }
    }

    public double getBalance(OfflinePlayer player) {
        return economy.getBalance(player);
    }

    public void depositCoins(OfflinePlayer player, double amount) {
        economy.depositPlayer(player, amount);
    }

    public EconomyResponse withdrawCoins(OfflinePlayer player, double amount) {
        if (has(player, amount)) return economy.withdrawPlayer(player, amount);
        else return new EconomyResponse(amount, getBalance(player), EconomyResponse.ResponseType.FAILURE, "");
    }

    public boolean has(OfflinePlayer player, double amount) {
        // avoid unimplemented economy methods
        return economy.has(player, amount) || getBalance(player) >= amount;
    }

}
