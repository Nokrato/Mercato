package com.nextplugins.nextmarket.hook.model;

import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.OfflinePlayer;

public interface CurrencyHook {

    boolean has(OfflinePlayer player, double amount);
    double getBalance(OfflinePlayer player);
    void depositCoins(OfflinePlayer player, double amount);
    EconomyResponse withdrawCoins(OfflinePlayer player, double amount);

}
