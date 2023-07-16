package com.treekicker4.enchantmenttransfer.mixin;

import com.treekicker4.enchantmenttransfer.configs.EnchantmentTransferConfigs;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.nbt.NbtList;
import net.minecraft.screen.Property;
import net.minecraft.screen.AnvilScreenHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({AnvilScreenHandler.class})
public abstract class AnvilLogicModifier {
	@Shadow
	@Final
	private Property levelCost;
	private boolean isEnchantmentTransfer = false;
	private ItemStack cleanedItem;

	public AnvilLogicModifier() {
		this.cleanedItem = new ItemStack(Items.AIR);
	}

	@ModifyVariable(
			at = @At("STORE"),
			method = {"updateResult()V"},
			ordinal = 1
	)
	private ItemStack changeIS(ItemStack itemStack2) {
		System.out.println("AAGA");
		Object[] input = ((AnvilScreenHandler)(Object)this).getStacks().toArray();
		ItemStack itemStack3 = (ItemStack)input[0];
		ItemStack itemStack = (ItemStack)input[1];
		boolean var10000;
		if (itemStack3.isOf(Items.ENCHANTED_BOOK) && !EnchantedBookItem.getEnchantmentNbt(itemStack3).isEmpty()) {
			var10000 = true;
		} else {
			var10000 = false;
		}

		boolean newLogic = !itemStack3.getEnchantments().isEmpty() && itemStack.isOf(Items.BOOK) && itemStack.getCount() == 1;
		if (!newLogic) {
			this.isEnchantmentTransfer = false;
			return itemStack2;
		} else {
			this.isEnchantmentTransfer = true;
			Map<Enchantment, Integer> enchantments = EnchantmentHelper.get(itemStack3);
			ItemStack result = Items.ENCHANTED_BOOK.getDefaultStack();
			if (EnchantmentTransferConfigs.limit == 0) {
				EnchantmentHelper.set(enchantments, result);
			} else {
				int count = 0;
				NbtList nbtList = new NbtList();
				Iterator var11 = enchantments.entrySet().iterator();

				while(var11.hasNext()) {
					Map.Entry<Enchantment, Integer> entry = (Map.Entry)var11.next();
					if (count >= EnchantmentTransferConfigs.limit) {
						break;
					}

					Enchantment enchantment = (Enchantment)entry.getKey();
					if (enchantment != null) {
						int i = (Integer)entry.getValue();
						nbtList.add(EnchantmentHelper.createNbt(EnchantmentHelper.getEnchantmentId(enchantment), i));
						if (result.isOf(Items.ENCHANTED_BOOK)) {
							EnchantedBookItem.addEnchantment(result, new EnchantmentLevelEntry(enchantment, i));
							++count;
						}
					}
				}

				if (nbtList.isEmpty()) {
					result.removeSubNbt("Enchantments");
				} else if (!result.isOf(Items.ENCHANTED_BOOK)) {
					result.setSubNbt("Enchantments", nbtList);
				}
			}

			this.cleanedItem = itemStack3.copy();
			this.levelCost.set(Math.max((int)((double)this.levelCost.get() * EnchantmentTransferConfigs.costFactor), 1));
			if (EnchantmentTransferConfigs.fixedCost != 1000) {
				this.levelCost.set(EnchantmentTransferConfigs.fixedCost);
			}

			return result;
		}
	}

	@ModifyVariable(
			at = @At("STORE"),
			method = {"updateResult()V"},
			ordinal = 0
	)
	private boolean changeBL(boolean bl) {
		Object[] input = ((AnvilScreenHandler)(Object)this).getStacks().toArray();
		ItemStack itemStack = (ItemStack)input[0];
		ItemStack itemStack3 = (ItemStack)input[1];
		boolean defaultLogic = itemStack3.isOf(Items.ENCHANTED_BOOK) && !EnchantedBookItem.getEnchantmentNbt(itemStack3).isEmpty();
		boolean newLogic = !itemStack.getEnchantments().isEmpty() && itemStack3.isOf(Items.BOOK);
		return defaultLogic || newLogic;
	}

	@Inject(
			at = {@At("TAIL")},
			method = {"onTakeOutput"}
	)
	private void setVanillaItem(PlayerEntity player, ItemStack stack, CallbackInfo ci) {
		Object[] input = ((AnvilScreenHandler)(Object)this).getStacks().toArray();
		if (this.isEnchantmentTransfer) {
			EnchantmentHelper.set(Map.of(), this.cleanedItem);
			if (EnchantmentTransferConfigs.returnItem == 1) {
				player.giveItemStack(this.cleanedItem);
			}
		}

	}
}
