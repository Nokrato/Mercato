package com.nextplugins.nextmarket.hook;

import com.nextplugins.nextmarket.hook.model.impl.CoinsHook;
import com.nextplugins.nextmarket.hook.model.CurrencyHook;
import com.nextplugins.nextmarket.hook.model.CurrencyHookType;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author Yuhtin
 * Github: https://github.com/Yuhtin
 */

@Singleton
public final class CurrencyHookManager {

    @Inject private CoinsHook coinsHook;

    public void init() {
        this.coinsHook.init();
    }

    public CurrencyHook getByType(CurrencyHookType type) {

        switch (type) {

            case COINS: return coinsHook;
            default: return null;

        }

    }


}
