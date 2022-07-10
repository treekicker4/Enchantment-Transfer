package com.treekicker4.enchantmenttransfer.core;

import net.minecraftforge.common.ForgeConfigSpec;

public class EnchantmentTransferConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Integer> fixed_value;
    public static final ForgeConfigSpec.ConfigValue<Double> factor_value;
    public static final ForgeConfigSpec.ConfigValue<Integer> limit_value;
    public static final ForgeConfigSpec.ConfigValue<Integer> return_value;

    static {
        BUILDER.push("Config for Enchantment Transfer!");
        fixed_value = BUILDER.comment("Here you can set a fixed value for any enchantment transfer from item to book. Default is 1000 which disables the fixed value.").define("Fixed Experience Cost", 1000);
        factor_value = BUILDER.comment("Here you can set a factor to multiply any enchantment transfer XP cost by. This will be overwritten if a fixed value other than 1000 is defined. Default is 1.0.").define("Experience Cost Factor", 1.0);
        limit_value = BUILDER.comment("Here you can set an enchantment limit to only transfer X amount of enchantments from an item. Default is 1000 which disables the limit.").define("Enchantment Limit", 1000);
        return_value = BUILDER.comment("Should the anvil give you back the item you disenchanted? Change this value to 0 to block the original item from being returned. Default is 1.").define("Should Return", 1);
        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
