package com.treekicker4.enchantmenttransfer.configs;

import com.mojang.datafixers.util.Pair;

public class EnchantmentTransferConfigs {
    public static SimpleConfig CONFIG;
    private static ModConfigProvider configs;
    public static int fixedCost;
    public static double costFactor;
    public static int limit;
    public static int returnItem;

    public EnchantmentTransferConfigs() {
    }

    public static void registerConfigs() {
        configs = new ModConfigProvider();
        createConfigs();
        CONFIG = SimpleConfig.of("enchantment-transfer-config").provider(configs).request();
        assignConfigs();
    }

    private static void createConfigs() {
        configs.addKeyValuePair(new Pair("cost", 1000), "Here you can set a fixed value for any enchantment transfer from item to book. Leaving it default disables the fixed value.");
        configs.addKeyValuePair(new Pair("factor", 1.0), "Here you can set a factor to multiply any enchantment transfer XP cost by. Default is 1.0.");
        configs.addKeyValuePair(new Pair("limit", 0), "Here you can set an enchantment limit to only transfer X amount of enchantments from an item. Default is 0 which disables the limit.");
        configs.addKeyValuePair(new Pair("return", 1), "Should the anvil give you back the item you disenchanted? Change this value to 0 to block the original item from being returned. Default is 1.");
    }

    private static void assignConfigs() {
        fixedCost = CONFIG.getOrDefault("cost", 1000);
        costFactor = CONFIG.getOrDefault("factor", 1.0);
        limit = CONFIG.getOrDefault("limit", 0);
        returnItem = CONFIG.getOrDefault("return", 1);
    }
}
