package com.example.manepearsscythe;

import com.example.manepearsscythe.item.ScytheItem;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.item.ItemGroups;

public class ManePearsScytheMod implements ModInitializer {
    public static final String MOD_ID = "manepearsscythe";
    public static ScytheItem SCYTHE_ITEM;
    public static final int[] ENCHANTMENT_LEVELS = {10, 1, 2, 3, 5, 5, 2147483647}; 
    public static final String[] ENCHANTMENT_IDS = {
        "sharpness", "knockback", "wind_burst", "sweeping_edge",
        "breach", "density", "unbreaking"
    };

    @Override
    public void onInitialize() {
        SCYTHE_ITEM = new ScytheItem(new net.minecraft.item.Item.Settings());
        Registry.register(Registries.ITEM, Identifier.of(MOD_ID, "scythe"), SCYTHE_ITEM);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> {
            entries.add(SCYTHE_ITEM);
        });

        ServerEntityEvents.ENTITY_LOAD.register(this::onEntityLoad);
    }

    private void onEntityLoad(Entity entity, ServerWorld world) {
        if (!(entity instanceof PlayerEntity player)) return;
        ItemStack stack = player.getEquippedStack(EquipmentSlot.MAINHAND);
        if (stack.isOf(SCYTHE_ITEM)) {
            ScytheItem.applyBaseEnchantmentsIfMissing(stack, world);
        }
    }
}
