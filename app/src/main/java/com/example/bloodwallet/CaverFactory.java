package com.example.bloodwallet;

import com.klaytn.caver.Caver;

/**
 * Simple factory class for Caver object.
 */
public class CaverFactory {
    private static Caver caver = Caver.build(Constants.EN_URL);

    public static Caver get() {
        return caver;
    }
}

