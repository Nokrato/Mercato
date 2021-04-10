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
public final class TokensHook implements CurrencyHook {

    @Override
    public boolean has(OfflinePlayer player, double amount) {
        MinePlayer minePlayer = PlayerController.getByPlayer(player.getName());
        return minePlayer.getTokens() >= amount;
    }

    @Override
    public double getBalance(OfflinePlayer player) {
        MinePlayer minePlayer = PlayerController.getByPlayer(player.getName());
        return minePlayer.getTokens();
    }

    @Override
    public void depositCoins(OfflinePlayer player, double amount) {
        MinePlayer minePlayer = PlayerController.getByPlayer(player.getName());
        minePlayer.addTokens(amount);
    }

    @Override
    public EconomyResponse withdrawCoins(OfflinePlayer player, double amount) {

        MinePlayer minePlayer = PlayerController.getByPlayer(player.getName());
        if (minePlayer.getTokens() < amount) {
            return new EconomyResponse(amount, minePlayer.getTokens(), EconomyResponse.ResponseType.FAILURE, "");
        }

        minePlayer.setTokens(minePlayer.getTokens() - amount);
        return new EconomyResponse(amount, minePlayer.getTokens(), EconomyResponse.ResponseType.SUCCESS, "");

    }

}
