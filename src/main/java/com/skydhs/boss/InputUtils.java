package com.skydhs.boss;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;
import net.minecraft.server.v1_8_R3.PacketPlayOutAnimation;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;

/**
 * Never used test only.
 */
public class InputUtils {
//    PacketPlayInUseEntity
//    PacketPlayOutEntity.PacketPlayOutRelEntityMove

    public static void inject(Player player) {

        ChannelDuplexHandler channelDuplexHandler = new ChannelDuplexHandler() {
            @Override
            public void channelRead(ChannelHandlerContext channelHandlerContext,Object packet) throws Exception {
                if (StringUtils.containsIgnoreCase(packet.toString(), "ArmorStand")
                        || StringUtils.containsIgnoreCase(packet.toString(), "Damage")
                        || StringUtils.containsIgnoreCase(packet.toString(), "entity")) {

                    if (packet instanceof PacketPlayInUseEntity) {
                        PacketPlayInUseEntity p = (PacketPlayInUseEntity) packet;
                        Bukkit.broadcastMessage("A= " + p.a().name());

                        try {
                            Field field = PacketPlayInUseEntity.class.getDeclaredField("a");
                            field.setAccessible(true);

                            int a = field.getInt(p);
                            Bukkit.broadcastMessage("B= " + a);
                            animation(1, a);

//                            field.set(this, Integer.MAX_VALUE);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "+Packet READ: " + packet.toString());
                    }
                }
                super.channelRead(channelHandlerContext, packet);
            }

            @Override
            public void write(ChannelHandlerContext channelHandlerContext, Object packet, ChannelPromise promise) throws Exception {
                if (StringUtils.containsIgnoreCase(packet.toString(), "ArmorStand")
                        || StringUtils.containsIgnoreCase(packet.toString(), "entity")
                        || StringUtils.containsIgnoreCase(packet.toString(), "Damage")) {
                    Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "-Packet WRITE: " + packet.toString());
                }
                super.write(channelHandlerContext, packet, promise);
            }
        };

        ChannelPipeline pipeline = ((CraftPlayer) player).getHandle().playerConnection.networkManager.channel.pipeline();
        pipeline.addBefore("packet_handler", player.getName(), channelDuplexHandler);
    }

    public static Entity get(CraftWorld world, int id) {
        Bukkit.broadcastMessage("YOURS: " + id);
        for (Entity e : world.getHandle().k) {
            if (e.getId() == id) {
                return e;
            } else {
                Bukkit.broadcastMessage("ID: " + e.getId());
            }
        }
        Bukkit.broadcastMessage("wut");
        return world.getHandle().a(id);
    }

    public static void animation(int animation, int id) {
        PacketPlayOutAnimation packet = new PacketPlayOutAnimation();
        setValue(packet, "a", id);
        setValue(packet, "b", (byte)animation);
        sendPacket(packet);
    }

    public static void setValue(Object obj,String name,Object value) {
        try {
            Field field = obj.getClass().getDeclaredField(name);
            field.setAccessible(true);
            field.set(obj, value);
        } catch(Exception e) {}
    }

    private static void sendPacket(Packet packet) {
        Bukkit.getOnlinePlayers().forEach(cur -> ((CraftPlayer)cur).getHandle().playerConnection.sendPacket(packet));
    }
}