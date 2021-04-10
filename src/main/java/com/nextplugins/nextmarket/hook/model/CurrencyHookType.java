package com.nextplugins.nextmarket.hook.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum CurrencyHookType {

    COINS("coins"),
    TOKENS("tokens"),
    CRISTALS("cristais");

    @Getter private final String name;

}
