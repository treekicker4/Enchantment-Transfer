package com.treekicker4.enchantmenttransfer;

import com.treekicker4.enchantmenttransfer.configs.EnchantmentTransferConfigs;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EnchantmentTransfer implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("enchantment-transfer");
	public static final String MOD_ID = "enchantment-transfer";

	public EnchantmentTransfer() {
	}

	public void onInitialize() {
		EnchantmentTransferConfigs.registerConfigs();
	}
}
