package com.treekicker4.enchantmenttransfer.events;
import com.treekicker4.enchantmenttransfer.EnchantmentTransfer;
import com.treekicker4.enchantmenttransfer.core.EnchantmentTransferConfig;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.entity.player.AnvilRepairEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(modid = EnchantmentTransfer.MODID)
public class AnvilEvents {
    @SubscribeEvent
    public static void giveEnchantedBook(AnvilUpdateEvent event) {
        int i;
        if (event.getLeft().isEnchanted() && event.getRight().getItem() == Items.BOOK && event.getRight().getCount() == 1) {
            //if (event.getRight().isEnchanted() && event.getLeft().getItem() == Items.BOOK) {
            Map<Enchantment, Integer> raw_enchantments = EnchantmentHelper.getEnchantments(event.getLeft());
            Map<Enchantment, Integer> enchantments = new HashMap<>();
            int count = 0;
            for (Map.Entry<Enchantment, Integer> set : raw_enchantments.entrySet()) {
                if (count >= EnchantmentTransferConfig.limit_value.get()) {
                    break;
                }
                Enchantment enchantment = set.getKey();
                enchantments.put(enchantment, set.getValue());
                count++;
            }
            ItemStack finalBook = new ItemStack(Items.ENCHANTED_BOOK);
            for (Map.Entry<Enchantment, Integer> set : enchantments.entrySet()) {
                if (count >= EnchantmentTransferConfig.limit_value.get()) {
                    break;
                }
                Enchantment enchantment = set.getKey();
                EnchantedBookItem.addEnchantment(finalBook, new EnchantmentInstance(enchantment, set.getValue()));
                count++;
            }
            EnchantmentHelper.setEnchantments(enchantments, finalBook);
            //event.setOutput(finalBook);
            ItemStack itemstack = event.getLeft();
            event.setCost(1);
            i = 0;
            ItemStack itemstack1 = itemstack.copy();
            ItemStack itemstack2 = event.getRight().copy();
            Map<Enchantment, Integer> map = enchantments;
            EnchantmentHelper.setEnchantments(map, itemstack2);
            boolean flag = true;
            if (!itemstack2.isEmpty()) {
                //flag = itemstack2.getItem() == Items.ENCHANTED_BOOK && !EnchantedBookItem.getEnchantments(itemstack2).isEmpty();
                Map<Enchantment, Integer> map1 = enchantments;
                count = 0;
                for (Enchantment enchantment1 : map1.keySet()) {
                    if (enchantment1 != null) {
                        if (count >= EnchantmentTransferConfig.limit_value.get()) {
                            break;
                        }
                        count++;
                        int i2 = map.getOrDefault(enchantment1, 0);
                        int j2 = map1.get(enchantment1);
                        j2 = i2 == j2 ? j2 + 1 : Math.max(j2, i2);
                        boolean flag1 = true;
                        for (Enchantment enchantment : map.keySet()) {
                            if (enchantment != enchantment1 && !enchantment1.isCompatibleWith(enchantment)) {
                                flag1 = false;
                                ++i;
                            }
                        }

                        if (flag1) {
                            if (j2 > enchantment1.getMaxLevel()) {
                                j2 = enchantment1.getMaxLevel();
                            }

                            map.put(enchantment1, j2);
                            int k3 = 0;
                            switch (enchantment1.getRarity()) {
                                case COMMON:
                                    k3 = 1;
                                    break;
                                case UNCOMMON:
                                    k3 = 2;
                                    break;
                                case RARE:
                                    k3 = 4;
                                    break;
                                case VERY_RARE:
                                    k3 = 8;
                            }

                            if (flag) {
                                k3 = Math.max(1, k3 / 2);
                            }

                            i += k3 * j2;
                            if (itemstack.getCount() > 1) {
                                i = 40;
                            }
                        }
                    }
                }
            }
            if (EnchantmentTransferConfig.fixed_value.get() != 1000) {
                if (EnchantmentTransferConfig.fixed_value.get() == 0) {
                    event.setCost(1);
                } else {
                    event.setCost(EnchantmentTransferConfig.fixed_value.get());
                }
            } else {
                double factor_value = EnchantmentTransferConfig.factor_value.get();
                if (factor_value > 0.0) {
                    event.setCost((int) Math.round(i * factor_value));
                } else {
                    event.setCost(1);
                }
            }

            event.setOutput(finalBook);
        }
    }
    @SubscribeEvent
    public static void giveItemBack(AnvilRepairEvent event) {
        if (event.getLeft().isEnchanted() && event.getRight().getItem() == Items.BOOK) {
            if (EnchantmentTransferConfig.return_value.get() != 0) {
                ItemStack disenchanted = event.getLeft().copy();
                EnchantmentHelper.setEnchantments(EnchantmentHelper.getEnchantments(event.getRight()), disenchanted);
                if (EnchantmentTransferConfig.fixed_value.get() == 0) {
                    event.getEntity().giveExperienceLevels(1);
                }
                event.getEntity().getInventory().add(disenchanted);
            }
        }
    }
}
