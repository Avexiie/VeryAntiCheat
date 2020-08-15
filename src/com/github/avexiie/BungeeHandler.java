package com.github.avexiie;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class BungeeHandler implements PluginMessageListener {

    public static void load(VAC plugin) {
        Bukkit.getMessenger().registerOutgoingPluginChannel(plugin, "VAC:VACAlert".toLowerCase());
        Bukkit.getMessenger().registerOutgoingPluginChannel(plugin, "VAC:VACPunish".toLowerCase());
        Bukkit.getMessenger().registerIncomingPluginChannel(plugin, "VAC:VACAlert".toLowerCase(), new BungeeHandler());
    }

    private static void sbmsg(String msg, String ch) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        try {
            out.writeUTF(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bukkit.getServer().sendPluginMessage(VAC.getVAC(), ch.toLowerCase(), b.toByteArray());
    }

    public static void handleCrossAlert(String msg) {
        sbmsg(msg, "vac:vacalert");
    }

    public static boolean handleBungeePunish(String msg) {
        if (msg.toLowerCase().startsWith("bungeecord:")) {
            sbmsg(msg.replaceAll("bungeecord:", ""), "vac:vacpunish");
            return true;
        }
        return false;
    }

    @Override
    public synchronized void onPluginMessageReceived(String channel, Player player, byte[] message) {
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(message));
        if (channel.equals("vac:vacalert")) {
            try {
                String msg = in.readUTF();
                VAC.getVAC().broadcast(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}