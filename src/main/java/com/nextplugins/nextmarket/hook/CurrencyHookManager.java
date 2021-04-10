package com.nextplugins.nextmarket.hook;

import com.nextplugins.nextmarket.hook.model.impl.CoinsHook;
import com.nextplugins.nextmarket.hook.model.CurrencyHook;
import com.nextplugins.nextmarket.hook.model.CurrencyHookType;
import com.nextplugins.nextmarket.hook.model.impl.CrystalHook;
import com.nextplugins.nextmarket.hook.model.impl.TokensHook;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author Yuhtin
 * Github: https://github.com/Yuhtin
 */

@Singleton
public final class CurrencyHookManager {

    @Inject private CoinsHook coinsHook;
    @Inject private TokensHook tokensHook;
    @Inject private CrystalHook crystalHook;

    public void init() {
        this.coinsHook.init();
    }

    public CurrencyHook getByType(CurrencyHookType type) {

        switch (type) {

            case COINS: return coinsHook;
            case TOKENS: return tokensHook;
            default: return crystalHook;

        }

    }


}
