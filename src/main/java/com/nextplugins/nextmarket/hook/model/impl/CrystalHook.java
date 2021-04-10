package com.nextplugins.nextmarket.hook.model.impl;

import com.nextplugins.nextmarket.hook.model.CurrencyHook;
import com.yuhtin.minecraft.heroesmines.service.player.MinePlayer;
import com.yuhtin.minecraft.heroesmines.service.player.controller.PlayerController;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.OfflinePlayer;

import javax.inject.Singleton;

/**
 * @author Yuhtin
 * Github: https://github.com/Yuhtin
 */

@Singleton
public final class CrystalHook implements CurrencyHook {

    @Override
    public boolean has(OfflinePlayer player, double amount) {
        MinePlayer minePlayer = PlayerController.getByPlayer(player.getName());
        return minePlayer.getCrystals() >= amount;
    }

    @Override
    public double getBalance(OfflinePlayer player) {
        MinePlayer minePlayer = PlayerController.getByPlayer(player.getName());
        return minePlayer.getCrystals();
    }

    @Override
    public void depositCoins(OfflinePlayer player, double amount) {
        MinePlayer minePlayer = PlayerController.getByPlayer(player.getName());
        minePlayer.addCrystals(amount);
    }

    @Override
    public EconomyResponse withdrawCoins(OfflinePlayer player, double amount) {

        MinePlayer minePlayer = PlayerController.getByPlayer(player.getName());
        if (minePlayer.getCrystals() < amount) {
            return new EconomyResponse(amount, minePlayer.getCrystals(), EconomyResponse.ResponseType.FAILURE, "");
        }

        minePlayer.setCrystals(minePlayer.getCrystals() - amount);
        return new EconomyResponse(amount, minePlayer.getCrystals(), EconomyResponse.ResponseType.SUCCESS, "");

    }

}
