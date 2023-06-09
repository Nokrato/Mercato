package com.nextplugins.nextmarket.listener;

import com.google.inject.Inject;
import com.nextplugins.nextmarket.api.event.ProductBuyEvent;
import com.nextplugins.nextmarket.api.model.product.Product;
import com.nextplugins.nextmarket.configuration.value.MessageValue;
import com.nextplugins.nextmarket.hook.CurrencyHookManager;
import com.nextplugins.nextmarket.storage.ProductStorage;
import com.nextplugins.nextmarket.util.NumberUtils;
import lombok.val;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.PlayerInventory;

public final class ProductBuyListener implements Listener {

    @Inject private CurrencyHookManager currencyHookManager;
    @Inject private ProductStorage productStorage;

    @EventHandler
    private void onProductBuy(ProductBuyEvent event) {
        Player player = event.getPlayer();

        Product product = event.getProduct();
        if (!product.isAvailable() || product.isExpired()) {

            event.setCancelled(true);
            player.sendMessage(MessageValue.get(MessageValue::unavailableProduct));
            return;

        }

        PlayerInventory inventory = player.getInventory();
        if (player.getInventory().firstEmpty() == -1) {

            event.setCancelled(true);
            player.getPlayer().sendMessage(MessageValue.get(MessageValue::fullInventoryMessage));
            return;

        }

        val economy = currencyHookManager.getByType(product.getCurrencyHookType());

        EconomyResponse economyResponse = economy.withdrawCoins(player, product.getPrice());
        if (!economyResponse.transactionSuccess()) {

            event.setCancelled(true);
            player.sendMessage(MessageValue.get(MessageValue::insufficientMoneyMessage)
                    .replace("%difference%", String.valueOf(product.getPrice() - economyResponse.balance))
                    .replace("%economyType%", product.getCurrencyHookType().getName())
            );
            return;

        }

        product.setAvailable(false);
        productStorage.deleteOne(product);

        OfflinePlayer seller = product.getSeller();
        economy.depositCoins(seller, product.getPrice());
        inventory.addItem(product.getItemStack());

        player.closeInventory();

        player.sendMessage(MessageValue.get(MessageValue::boughtAnItemMessage));

        if (seller.isOnline()) {
            seller.getPlayer().sendMessage(MessageValue.get(MessageValue::soldAItemMessage)
                    .replace("%amount%", NumberUtils.formatNumber(product.getPrice()))
                    .replace("%economyType%", product.getCurrencyHookType().getName())
            );
        }
    }

}
