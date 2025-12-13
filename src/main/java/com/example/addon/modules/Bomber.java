package com.example.addon.modules;

import com.example.addon.AddonTemplate;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.FindItemResult;
import meteordevelopment.meteorclient.utils.world.BlockUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class Bomber extends Module {


    private final MinecraftClient client = MinecraftClient.getInstance();
    private final SettingGroup sgGeneral = settings.getDefaultGroup();
    Vec3d pos;

    public Bomber(){
        super(AddonTemplate.CATEGORY, "B-2 Spirit", "An Bomber module that drops bombs.");

    }
    private final Setting<Integer> delay = sgGeneral.add(new IntSetting.Builder()
        .name("drop-TnT-delay")
        .description("Delay between each TnT drop cycle.")
        .defaultValue(1).max(1000)
        .build()
    );
    private final Setting<Boolean> swinghand = sgGeneral.add(new BoolSetting.Builder().name("swig hand").defaultValue(false).build());
    private final Setting<Boolean> rotate = sgGeneral.add(new BoolSetting.Builder().name("rotate").defaultValue(false).build());

    private int tickcountdown = 0;
    BlockPos  blockpos = new BlockPos(0,0,0);
    @EventHandler
    private void onTick(TickEvent.Post event) {
        if (tickcountdown==0){
            client.interactionManager.interactBlock(
                client.player,
                Hand.MAIN_HAND,
                new BlockHitResult(
                    Vec3d.ofCenter(blockpos), // Hit the center of the block
                    Direction.UP,             // Face to click (UP is usually fine)
                    blockpos,
                    false                     // Not inside the block
                )
            );
        }


        if (tickcountdown>=delay.get()){
//            client.interactionManager.block
            pos = client.player.getPos();
            blockpos = new BlockPos((int) pos.x, (int) (pos.y+4), (int) pos.z);
            BlockUtils.place(blockpos,new FindItemResult(0,1),rotate.get(),1,swinghand.get());
            tickcountdown = 0;
//BlockPos blockPos, FindItemResult findItemResult, boolean rotate, int rotationPriority, boolean swingHand, boolean checkEntities, boolean swapBack


        }else {
        tickcountdown++;
        }
    }
}
