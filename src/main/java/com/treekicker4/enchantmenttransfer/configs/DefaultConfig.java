package com.treekicker4.enchantmenttransfer.configs;

public interface DefaultConfig {
    String get(String var1);

    static String empty(String namespace) {
        return "";
    }
}