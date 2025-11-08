package com.example.addon.mixin;

import com.example.addon.modules.AutoFishMinus;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.entity.projectile.FishingBobberEntity;

@Mixin(FishingBobberEntity.class)
public abstract class FisshingBobberEntityMixin {

    @Shadow private boolean caughtFish;

    @Inject(method = "onTrackedDataSet", at = @At("TAIL"))
    public void onTrackedDataSet(TrackedData<?> data , CallbackInfo ci) {
        Modules.get().isActive(AutoFishMinus.class);
        MinecraftClient client = MinecraftClient.getInstance();
        if (caughtFish && Modules.get().isActive(AutoFishMinus.class)) {

            client.interactionManager.interactItem(client.player, Hand.MAIN_HAND);
            Modules.get().get(AutoFishMinus.class).setRecastTimer();


        }
    }
}
