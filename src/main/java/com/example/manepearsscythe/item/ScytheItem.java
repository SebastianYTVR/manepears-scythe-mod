package com.example.manepearsscythe.item;

import com.example.manepearsscythe.ManePearsScytheMod;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import net.minecraft.util.Identifier;

public class ScytheItem extends Item {
    public ScytheItem(Settings settings) {
        super(settings);
    }

    public static void applyBaseEnchantmentsIfMissing(ItemStack stack, World world) {
        if (world.isClient) return;
        
        ItemEnchantmentsComponent current = stack.getOrDefault(
            DataComponentTypes.ENCHANTMENTS, ItemEnchantmentsComponent.DEFAULT
        );
        if (!current.getEnchantments().iterator().hasNext()) {
            applyBaseEnchantments(stack, world);
        }
    }

    public static void applyBaseEnchantments(ItemStack stack, World world) {
        if (world.isClient) return;
        
        var enchantmentRegistry = world.getRegistryManager().get(RegistryKeys.ENCHANTMENT);
        var builder = new ItemEnchantmentsComponent.Builder(
            stack.getOrDefault(DataComponentTypes.ENCHANTMENTS, ItemEnchantmentsComponent.DEFAULT)
        );

        for (int i = 0; i < ManePearsScytheMod.ENCHANTMENT_IDS.length; i++) {
            String id = ManePearsScytheMod.ENCHANTMENT_IDS[i];
            RegistryEntry<Enchantment> entry = enchantmentRegistry.getEntry(Identifier.ofVanilla(id)).orElse(null);
            if (entry != null) {
                builder.set(entry, ManePearsScytheMod.ENCHANTMENT_LEVELS[i]);
            }
        }

        stack.set(DataComponentTypes.ENCHANTMENTS, builder.build());
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if (selected && !world.isClient) {
            ItemEnchantmentsComponent current = stack.getOrDefault(
                DataComponentTypes.ENCHANTMENTS, ItemEnchantmentsComponent.DEFAULT
            );
            if (!current.getEnchantments().iterator().hasNext()) {
                applyBaseEnchantments(stack, world);
            }
        }
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!attacker.getWorld().isClient && target instanceof PlayerEntity targetPlayer) {
            ItemStack offhand = targetPlayer.getEquippedStack(EquipmentSlot.OFFHAND);
            if (!offhand.isEmpty() && offhand.isOf(net.minecraft.item.Items.SHIELD) && targetPlayer.isBlocking()) {
                targetPlayer.disableShield(true);
                targetPlayer.getItemCooldownManager().set(offhand.getItem(), 100);
                targetPlayer.getWorld().playSound(
                    null, targetPlayer.getBlockPos(), SoundEvents.ITEM_SHIELD_BLOCK,
                    targetPlayer.getSoundCategory(), 1.0F, 0.8F
                );
            }
            
            offhand = targetPlayer.getMainHandStack();
            if (!offhand.isEmpty() && offhand.isOf(net.minecraft.item.Items.SHIELD) && targetPlayer.isBlocking()) {
                targetPlayer.disableShield(true);
                targetPlayer.getItemCooldownManager().set(offhand.getItem(), 100);
            }
        }
        return super.postHit(stack, target, attacker);
    }
}
