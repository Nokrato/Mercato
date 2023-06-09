package com.nextplugins.nextmarket.command;

import com.google.inject.Inject;
import com.henryfabio.minecraft.inventoryapi.viewer.property.ViewerPropertyMap;
import com.nextplugins.nextmarket.api.model.category.Category;
import com.nextplugins.nextmarket.api.model.product.Product;
import com.nextplugins.nextmarket.configuration.value.MessageValue;
import com.nextplugins.nextmarket.inventory.PersonalMarketInventory;
import com.nextplugins.nextmarket.inventory.SellingMarketInventory;
import com.nextplugins.nextmarket.manager.CategoryManager;
import com.nextplugins.nextmarket.manager.ProductManager;
import com.nextplugins.nextmarket.registry.InventoryRegistry;
import com.nextplugins.nextmarket.storage.ProductStorage;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.annotation.Optional;
import me.saiintbrisson.minecraft.command.command.Context;
import me.saiintbrisson.minecraft.command.target.CommandTarget;
import org.bukkit.entity.Player;

public final class MarketCommand {

    @Inject private CategoryManager categoryManager;

    @Inject private ProductManager productManager;
    @Inject private ProductStorage productStorage;

    @Inject private InventoryRegistry inventoryRegistry;

    @Command(
            name = "market",
            aliases = {"mercado"},
            permission = "nextmarket.use",
            target = CommandTarget.PLAYER,
            async = true
    )
    public void marketCommand(Context<Player> context) {
        context.sendMessage(MessageValue.get(MessageValue::commandMessage).toArray(new String[]{}));
    }

    @Command(
            name = "market.show",
            aliases = {"ver"},
            async = true
    )
    public void showMarketCommand(Context<Player> context, @Optional String id) {
        if (id == null) {
            inventoryRegistry.getMarketInventory().openInventory(context.getSender());
        } else {
            Category category = categoryManager.findCategoryById(id).orElse(null);
            if (category == null) {
                String message = MessageValue.get(MessageValue::categoryNotExists);
                context.sendMessage(message);
            } else {
                inventoryRegistry.getCategoryInventory().openInventory(context.getSender(), viewer -> {
                    ViewerPropertyMap propertyMap = viewer.getPropertyMap();
                    propertyMap.set("category", category);
                    propertyMap.set("products", productStorage.findProductsByCategory(category));
                });
            }
        }
    }

    @Command(
            name = "market.personal",
            aliases = {"pessoal"},
            async = true
    )
    public void personalMarketCommand(Context<Player> context) {
        PersonalMarketInventory personalMarketInventory = inventoryRegistry.getPersonalMarketInventory();
        personalMarketInventory.openInventory(context.getSender());
    }

    @Command(
            name = "market.sell",
            aliases = {"vender"},
            async = true
    )
    public void sellMarketCommand(Context<Player> context, @Optional String priceText, @Optional String destination) {
       if (priceText == null) {
            context.sendMessage(MessageValue.get(MessageValue::correctUsageSellMessage));

            return;
       }

       final double price = Double.parseDouble(priceText);

        Product product = productManager.createProduct(context.getSender(), destination, price);
        if (product == null) return;

        inventoryRegistry.getConfirmSellInventory().openConfirmation(context.getSender(), product, product.getItemStack());
    }

    @Command(
            name = "market.selling",
            aliases = {"anunciados", "vendidos"},
            async = true
    )
    public void sellingMarketCommand(Context<Player> context) {
        SellingMarketInventory sellingMarketInventory = inventoryRegistry.getSellingMarketInventory();
        sellingMarketInventory.openInventory(context.getSender());
    }

}
