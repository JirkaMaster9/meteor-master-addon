package com.example.addon.modules;
import com.example.addon.AddonTemplate;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Hand;

public class AutoFishMinus extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    public AutoFishMinus() {
        super(AddonTemplate.CATEGORY, "Auto fish Minus", "should catch fish");
    }
    private final Setting<Integer> recastTimerMax = sgGeneral.add(new IntSetting.Builder().name("Time to trow the rod back ").description("time(ticks) to use the rod").defaultValue(20).build());
    private int recastTimer;



    MinecraftClient client = MinecraftClient.getInstance();

    @EventHandler
    private void onTick(TickEvent.Post event) {
        if (recastTimer > 0) {
            recastTimer--;
        }
        if (recastTimer == 0 && Modules.get().isActive(AutoFishMinus.class)) {
            client.interactionManager.interactItem(client.player, Hand.MAIN_HAND);
            recastTimer=-1;
        }
    }
    public void setRecastTimer() {
        recastTimer = recastTimerMax.get();
    }

}
