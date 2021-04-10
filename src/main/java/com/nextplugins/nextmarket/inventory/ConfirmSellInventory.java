package com.nextplugins.nextmarket.inventory;

import com.henryfabio.minecraft.inventoryapi.editor.InventoryEditor;
import com.henryfabio.minecraft.inventoryapi.inventory.impl.simple.SimpleInventory;
import com.henryfabio.minecraft.inventoryapi.item.InventoryItem;
import com.henryfabio.minecraft.inventoryapi.viewer.Viewer;
import com.henryfabio.minecraft.inventoryapi.viewer.property.ViewerPropertyMap;
import com.nextplugins.nextmarket.api.event.ProductCreateEvent;
import com.nextplugins.nextmarket.api.model.product.Product;
import com.nextplugins.nextmarket.hook.model.CurrencyHookType;
import com.nextplugins.nextmarket.util.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

/**
 * @author Yuhtin
 * Github: https://github.com/Yuhtin
 */
public final class ConfirmSellInventory extends SimpleInventory {

    public ConfirmSellInventory() {
        super("nextmarket.confirmationSell", "Confirme a venda do item", 3 * 9);
    }

    public void openConfirmation(Player player, Product product, ItemStack itemStack) {
        openInventory(player, viewer -> {

            ViewerPropertyMap propertyMap = viewer.getPropertyMap();
            propertyMap.set("product", product);
            propertyMap.set("itemStack", itemStack);

        });
    }

    @Override
    protected void configureInventory(Viewer viewer, InventoryEditor editor) {

        ItemStack itemStack = viewer.getPropertyMap().get("itemStack");

        editor.setItem(10, InventoryItem.of(itemStack));
        editor.setItem(11, cancelItem());
        editor.setItem(14, coinsItem());
        editor.setItem(15, tokensItem());
        editor.setItem(16, crystalsItem());

    }

    public InventoryItem cancelItem() {

        ItemStack item = new ItemBuilder(Material.GOLD_INGOT)
                .name("&cCancelar")
                .setLore("&7Clique para cancelar a venda do item")
                .changeItemMeta(meta -> meta.addItemFlags(ItemFlag.values()))
                .wrap();

        return InventoryItem.of(item).defaultCallback(event -> event.getPlayer().closeInventory());

    }

    public InventoryItem coinsItem() {

        ItemStack item = new ItemBuilder(Material.GOLD_INGOT)
                .name("&2Vender por coins")
                .setLore("&7Clique para colocar este item à venda por &acoins")
                .changeItemMeta(meta -> meta.addItemFlags(ItemFlag.values()))
                .wrap();

        return InventoryItem.of(item).defaultCallback(event -> accept(event.getViewer(), CurrencyHookType.COINS));

    }

    public InventoryItem tokensItem() {

        ItemStack item = new ItemBuilder(Material.GOLD_INGOT)
                .name("&6Vender por tokens")
                .setLore("&7Clique para colocar este item à venda por &etokens")
                .changeItemMeta(meta -> meta.addItemFlags(ItemFlag.values()))
                .wrap();

        return InventoryItem.of(item).defaultCallback(event -> accept(event.getViewer(), CurrencyHookType.TOKENS));

    }

    public InventoryItem crystalsItem() {

        ItemStack item = new ItemBuilder(Material.PRISMARINE_CRYSTALS)
                .name("&3Vender por cristais")
                .setLore("&7Clique para colocar este item à venda por &bcristais")
                .changeItemMeta(meta -> meta.addItemFlags(ItemFlag.values()))
                .wrap();

        return InventoryItem.of(item).defaultCallback(event -> accept(event.getViewer(), CurrencyHookType.CRISTALS));

    }

    private void accept(Viewer viewer, CurrencyHookType type) {

        Product product = viewer.getPropertyMap().get("product");
        product.setCurrencyHookType(type);

        ProductCreateEvent createEvent = new ProductCreateEvent(viewer.getPlayer(), product);
        Bukkit.getPluginManager().callEvent(createEvent);

    }

}
