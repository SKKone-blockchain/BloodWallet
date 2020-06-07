package com.example.bloodwallet;

import java.math.BigDecimal;

public class Utils{
    public static BigDecimal hexToBigDecimal(String hex) {
        if (hex.startsWith("0x")) {
            hex = hex.substring(2);
        }
        return BigDecimal.valueOf(Long.parseLong(hex, 16));
    }
}