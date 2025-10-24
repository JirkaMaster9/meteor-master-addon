package com.example.addon.modules;

import com.example.addon.AddonTemplate;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import meteordevelopment.meteorclient.systems.modules.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;


public class BundleDupe extends Module  {
    public static int apDelay = 30;
    public static int apbDelay = 20;
    public static int packetcount = 20;
    public static boolean soundsenabled = false;
    private static Thread t;


    public BundleDupe() {
        super(AddonTemplate.CATEGORY, "Bundle Dupe", "An dupe module that dupes.");
    }



    @Override
    public void onActivate() {
        System.out.println("FUNGUJU");
        MinecraftClient client = MinecraftClient.getInstance();

        if (t != null) {
            client.player.sendMessage(Text.literal("Already started"), false);
            return;
        }

        if (!checkConditions()) return;

        ClientPlayerEntity player = client.player;
        client.player.sendMessage(Text.literal("Starting dupe"), false);

        t = Thread.ofVirtual().start(() -> {
            // Prepare modified stacks map
            Int2ObjectMap<ItemStack> state = new Int2ObjectArrayMap<>();

            client.player.sendMessage(Text.literal("t1 go"), false);

            while (!Thread.interrupted()) {
                // Pickup bundle
                client.getNetworkHandler().sendPacket(
                    new ClickSlotC2SPacket(
                        player.currentScreenHandler.syncId,
                        player.currentScreenHandler.getRevision(),
                        0, // slot index
                        0, // button (0 = left click)
                        SlotActionType.PICKUP,
                        player.getInventory().getMainHandStack(),
                        state
                    )
                );

                // Send extra packets
                for (int i = 0; i < packetcount; i++) {
                    client.getNetworkHandler().sendPacket(new UpdateSelectedSlotC2SPacket(36));
                }

                try {
                    Thread.sleep(apDelay);
                } catch (InterruptedException e) {
                    break;
                }

                // Pickup bundle again
                client.getNetworkHandler().sendPacket(
                    new ClickSlotC2SPacket(
                        player.currentScreenHandler.syncId,
                        player.currentScreenHandler.getRevision(),
                        0,
                        0,
                        SlotActionType.PICKUP,
                        player.getInventory().getMainHandStack(),
                        state
                    )
                );

                try {
                    Thread.sleep(apbDelay);
                } catch (InterruptedException e) {
                    break;
                }

                // Drop first item if bundle changed
                ItemStack firstStack = player.getInventory().getMainHandStack();
                if (!firstStack.isOf(Items.BUNDLE) && !firstStack.isEmpty()) {
                    player.dropItem(firstStack, true);

                    // Pickup after drop
                    client.getNetworkHandler().sendPacket(
                        new ClickSlotC2SPacket(
                            player.currentScreenHandler.syncId,
                            player.currentScreenHandler.getRevision(),
                            0,
                            0,
                            SlotActionType.PICKUP,
                            player.getInventory().getMainHandStack(),
                            state
                        )
                    );

                    try {
                        Thread.sleep(50L);
                    } catch (InterruptedException e) {
                        break;
                    }

                    // Drop outside inventory
                    client.getNetworkHandler().sendPacket(
                        new ClickSlotC2SPacket(
                            player.currentScreenHandler.syncId,
                            player.currentScreenHandler.getRevision(),
                            0,
                            -999, // drop outside inventory
                            SlotActionType.PICKUP,
                            player.getInventory().getMainHandStack(),
                            state
                        )
                    );

                    try {
                        Thread.sleep(50L);
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            }

            client.player.sendMessage(Text.literal("t1 end"), false);
        });
    }

    @Override
    public void onDeactivate() {
        if (t == null) {
            System.out.println("Already stopped");
            return;
        }

        t.interrupt();
        try {
            t.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        t = null;
        System.out.println("Dupe thread stopped.");
    }

    public static boolean checkConditions() {
        MinecraftClient client = MinecraftClient.getInstance();
        ItemStack stack = client.player.getInventory().getMainHandStack();

        if (!stack.isOf(Items.BUNDLE)) {
            client.player.sendMessage(Text.literal("No bundle in first slot"), false);
            return false;
        }

        if (stack.isEmpty()) {
            client.player.sendMessage(Text.literal("Bundle empty"), false);
            return false;
        }

        if (!(client.player.currentScreenHandler instanceof PlayerScreenHandler)) {
            client.player.sendMessage(Text.literal("Not default screen handler, close all screens for real"), false);
            return false;
        }

        return true;
    }

}
