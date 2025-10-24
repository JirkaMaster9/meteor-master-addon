package com.example.addon.modules;

import com.example.addon.AddonTemplate;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.MinecraftClient;




public class FlyMinus extends Module {

    private final SettingGroup sgGeneral = settings.getDefaultGroup();


    public FlyMinus() {
        super(AddonTemplate.CATEGORY, "FLY minus", "An fly module that makes you fly (sometime) .");
    }

    private final Setting<Double> flySpeed = sgGeneral.add(new DoubleSetting.Builder().name("flyspeed").description("the speed you fly").defaultValue(0.05d).range(0.0d, 5.0d).build());
    private final Setting<Double> TpUp = sgGeneral.add(new DoubleSetting.Builder().name("TpUp").description("amount of block you get teleported up on activate").defaultValue(0.2).range(0, 3).build());
    private final Setting<Integer> goDownTime = sgGeneral.add(new IntSetting.Builder().name("goDownTime").description("time(ticks) to go down").defaultValue(60).range(1,800).build());
    private final Setting<Boolean> antiKick = sgGeneral.add(new BoolSetting.Builder().name("anti kick").description("time(ticks) to go down").defaultValue(false).build());

    private int nowDownTime;

    private final MinecraftClient client = MinecraftClient.getInstance();

    @Override
    public void onActivate() {

        client.player.getAbilities().setFlySpeed(flySpeed.get().floatValue());
        client.player.getAbilities().flying = true;
        if (client.player.isOnGround()){
            client.player.setPosition(client.player.getX(), client.player.getY()+TpUp.get(), client.player.getZ());
        }
        nowDownTime=0;
    }
    // NAPICU POUZIVA METEOR FUNKCI ON TICK
    @EventHandler
    private void onTick(TickEvent.Post event) {
        if (client.player.isOnGround()){
            this.toggle();

        }
//        System.out.println("nowDownTime: "+nowDownTime);
        if (antiKick.get()) {
            if (nowDownTime == 1) {
                client.player.setPosition(client.player.getX(), client.player.getY() + 0.2, client.player.getZ());
            }
            if (nowDownTime >= goDownTime.get()) {
                client.player.setPosition(client.player.getX(), client.player.getY() - 0.2, client.player.getZ());
                nowDownTime = 0;
            }


            nowDownTime += 1;
        }
//        PlayerMoveC2SPacket.PositionAndOnGround packet = new PlayerMoveC2SPacket.PositionAndOnGround(client.player.getX(), client.player.getY(), client.player.getZ(),false,false);
    }



    @Override
    public void onDeactivate() {

        client.player.getAbilities().flying = false;
    }


}
