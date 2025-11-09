package com.example.addon.modules;

import com.example.addon.AddonTemplate;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;


public class FlyMinus extends Module {

    private final SettingGroup sgGeneral = settings.getDefaultGroup();


    public FlyMinus() {
        super(AddonTemplate.CATEGORY, "FLY minus", "An fly module that makes you fly (sometime) .");
    }

    private final Setting<Double> flySpeed = sgGeneral.add(new DoubleSetting.Builder().name("flyspeed").description("the speed you fly").defaultValue(0.05d).range(0.0d, 5.0d).build());
    private final Setting<Double> TpUp = sgGeneral.add(new DoubleSetting.Builder().name("TpUp").description("amount of block you get teleported up on activate").defaultValue(0.2).range(0, 3).build());
    private final Setting<Integer> goDownTime = sgGeneral.add(new IntSetting.Builder().name("goDownTime").description("time(ticks) to go down").defaultValue(60).range(1,800).build());
    private final Setting<Boolean> antiKick = sgGeneral.add(new BoolSetting.Builder().name("anti kick").description("time(ticks) to go down").defaultValue(false).build());
    private final Setting<Boolean> creativeflight = sgGeneral.add(new BoolSetting.Builder().name("creativeflight").description("switches the flight type").defaultValue(false).build());
    private final Setting<Boolean> vectorflight = sgGeneral.add(new BoolSetting.Builder().name("vectorflight").description("switches the flight type").defaultValue(true).build());

    int toggle =0;
    int MAX_SPEED = 3;
    double FALL_SPEED = -0.04;
    double acceleration = 0.1;


    private int nowDownTime;

    private final MinecraftClient client = MinecraftClient.getInstance();

    @Override
    public void onActivate() {

        if (creativeflight.get()) {
            client.player.getAbilities().setFlySpeed(flySpeed.get().floatValue());
            client.player.getAbilities().flying = true;
            if (client.player.isOnGround()){
                client.player.setPosition(client.player.getX(), client.player.getY()+TpUp.get(), client.player.getZ());
            }
            nowDownTime=0;
        }
    }
    // NAPICU POUZIVA METEOR FUNKCI ON TICK
    @EventHandler
    private void onTick(TickEvent.Post event) {
        if (creativeflight.get()) {
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


        if (client.player!=null && this.isActive() && vectorflight.get()) {
            boolean jumpPressed = client.options.jumpKey.isPressed();
            boolean forwardPressed = client.options.forwardKey.isPressed();
            boolean leftPressed = client.options.leftKey.isPressed();
            boolean rightPressed = client.options.rightKey.isPressed();
            boolean backPressed = client.options.backKey.isPressed();

            Entity object = client.player;
            if (client.player.hasVehicle()) {
                object = client.player.getVehicle();
            }

            Vec3d velocity = object.getVelocity();
            Vec3d newVelocity = new Vec3d(velocity.x,-FALL_SPEED,velocity.z);
            if (jumpPressed) {
                if (forwardPressed) {
                    newVelocity = client.player.getRotationVector().multiply(acceleration);
                }
                if (leftPressed && !client.player.hasVehicle()) {
                    newVelocity = client.player.getRotationVector().multiply(acceleration).rotateY(3.1415927F/2);
                    newVelocity = new Vec3d(newVelocity.x,0,velocity.z);
                }
                if (rightPressed && !client.player.hasVehicle()) {
                    newVelocity = client.player.getRotationVector().multiply(acceleration).rotateY(-3.1415927F/2);
                    newVelocity = new Vec3d(newVelocity.x,0,velocity.z);
                }
                if (backPressed) {
                    newVelocity = client.player.getRotationVector().negate().multiply(acceleration);
                }


                newVelocity = new Vec3d(newVelocity.x, (toggle==0 && newVelocity.y > FALL_SPEED) ? FALL_SPEED : newVelocity.y,newVelocity.z);
                object.setVelocity(newVelocity);

                if (forwardPressed || leftPressed || rightPressed || backPressed) {
                    if (acceleration<MAX_SPEED) {
                        acceleration += 0.1;
                    }
                }else if (acceleration>0.2) {
                    acceleration -= 0.2;
                }

            }

            if (toggle ==  0 || newVelocity.y <= -0.04){
                toggle = 40;
            }
            toggle --;

        }





    }



    @Override
    public void onDeactivate() {

        if (creativeflight.get()) {
            client.player.getAbilities().flying = false;
        }
    }


}
