package com.github.avexiie.command;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.github.avexiie.VAC;
import com.github.avexiie.Lag;
import com.github.avexiie.Settings;
import com.github.avexiie.fwk.Command;
import com.github.avexiie.fwk.CommandArgs;
import com.github.avexiie.handlers.CStatsHandler;
import com.github.avexiie.util.UtilMath;
import com.github.avexiie.util.UtilTime;

public class VACCmd {

    @Command(name = "vac")
    public void onCmd(CommandArgs a) {
        Player p = a.getPlayer();
        String[] args = a.getArgs();
        VAC bac = VAC.getVAC();
        if (args.length == 0) {
            bac.sendMessage(p, "§rRunning VAC version " + Settings.VARIABLE_COLOR
                            + bac.getDescription().getVersion().replaceAll("\\[", "").replaceAll("\\]", "") + "§f by "
                            + Settings.VARIABLE_COLOR
                            + bac.getDescription().getAuthors().get(0).replaceAll("\\[", "").replaceAll("\\]", "") + "§f.",
                    false);
            if (p.hasPermission("vac.admin")) {
                bac.sendMessage(p, "§fUse " + Settings.VARIABLE_COLOR + "/VAC help§f for a list of available commands.",
                        false);
            }
            return;
        }
        if (args[0].equalsIgnoreCase("tac") && p.hasPermission("vac.tac")) {
            if (Settings.ENABLED == true) {
                Settings.ENABLED = false;
                bac.sendMessage(p,
                        "§cVeryAntiCheat functionality has been disabled! §cChanging this disables it for the session only, restarting the server will re-enable VAC!",
                        false);
            } else {
                Settings.ENABLED = true;
                bac.sendMessage(p,
                        "§aVeryAntiCheat functionality has been enabled! §cChanging this disables it for the session only, restarting the server will re-enable VAC!",
                        false);
            }
            return;
        }
        if (args[0].equalsIgnoreCase("help")) {
            boolean hasinfo = false;
            boolean hasnotify = false;
            boolean hasexempt = false;
            boolean hasvr = false;
            boolean hasrlcfg = false;
            boolean hastac = false;
            if (p.hasPermission("vac.tac")) {
                hastac = true;
            }
            if (p.hasPermission("vac.reload")) {
                hasrlcfg = true;
            }
            if (p.hasPermission("vac.info")) {
                hasinfo = true;
            }
            if (p.hasPermission("vac.verbose")) {
                hasnotify = true;
            }
            if (p.hasPermission("vac.exempt")) {
                hasexempt = true;
            }
            if (hasnotify || hasexempt || hasinfo || hasvr || hasrlcfg)
                bac.sendMessage(p, "List of Available Commands: ", false);
            if (hasinfo)
                bac.sendMessage(p, Settings.VARIABLE_COLOR + "/VAC info §7-§r Shows System / Server Info", false);
            if (hasnotify)
                bac.sendMessage(p, Settings.VARIABLE_COLOR + "/VAC verbose §7-§r Toggles Verbose on and off.", false);
            if (hasexempt)
                bac.sendMessage(p, Settings.VARIABLE_COLOR
                        + "/VAC exempt <player> <time> §7-§r Exempts a player for a specified time.", false);
            if (hasvr)
                bac.sendMessage(p,
                        Settings.VARIABLE_COLOR + "/VAC viewreport <id> §7-§r Views a report on a player kick.", false);
            if (hasrlcfg)
                bac.sendMessage(p, Settings.VARIABLE_COLOR + "/VAC reload §7-§r Reloads the configuration file.",
                        false);
            if (hastac)
                bac.sendMessage(p, Settings.VARIABLE_COLOR + "/VAC tac - §cThis will disable/enable the anticheat.",
                        false);

            return;
        }
        if (args[0].equalsIgnoreCase("statskey") && p.hasPermission("vac.statskey")) {
            bac.sendMessage(p, "Statistics Key: " + CStatsHandler.ID, false);
            return;
        }
        if (args[0].equalsIgnoreCase("info") && p.hasPermission("vac.info")) {
            double tps = UtilMath.trim(2, Lag.getTPS());
            String tps_real = "§c" + tps;
            if (tps >= 19) {
                tps_real = Settings.VARIABLE_COLOR + "" + tps;
            } else if (tps >= 18) {
                tps_real = "§e" + tps;
            }
            bac.sendMessage(p, "Current TPS: " + tps_real, false);
            bac.sendMessage(p, "Maximum Memory: " + Settings.VARIABLE_COLOR
                    + (Runtime.getRuntime().maxMemory() / 1024 / 1024) + "MB", false);
            bac.sendMessage(p, "Free Memory: " + Settings.VARIABLE_COLOR
                    + (Runtime.getRuntime().freeMemory() / 1024 / 1024) + "MB", false);
            bac.sendMessage(p, "Available Cores: §6" + Runtime.getRuntime().availableProcessors(), false);
            bac.sendMessage(p, "Operating System: §6" + System.getProperty("os.name") + " ("
                    + System.getProperty("os.version") + ")", false);
            bac.sendMessage(p, "System Architecture: §6" + System.getProperty("os.arch"), false);
            String bukkitVersion = org.bukkit.Bukkit.getVersion();
            bukkitVersion = bukkitVersion.substring(bukkitVersion.indexOf("MC: ") + 4, bukkitVersion.length() - 1);
            bac.sendMessage(p, "Server Version: §e" + bukkitVersion, false);
            bac.sendMessage(p, "Java Runtime Version: §e" + System.getProperty("java.runtime.version"), false);
            return;
        }
        if (args[0].equalsIgnoreCase("verbose") && p.hasPermission("bac.verbose")) {
            if (bac.nonotify.contains(p)) {
                bac.nonotify.remove(p);
                bac.sendMessage(p, "Verbose is now turned " + Settings.VARIABLE_COLOR + "on§r.", false);
            } else {
                bac.nonotify.add(p);
                bac.sendMessage(p, "Verbose is now turned §4off§r.", false);
            }
            return;
        }
        if (args[0].equalsIgnoreCase("exempt") && p.hasPermission("vac.exempt")) {
            if (args.length != 3) {
                bac.sendMessage(p, "Usage: " + Settings.VARIABLE_COLOR + "/VAC exempt <player> <time>", false);
                bac.sendMessage(p, "§7Time Example: " + Settings.VARIABLE_COLOR + "1h30m", false);
                return;
            }
            Player t = Bukkit.getPlayer(args[1]);
            if (t == null || !t.isOnline()) {
                bac.sendMessage(p, "That player is not online!", false);
                return;
            }
            long expire = UtilTime.parseDateDiff(args[2], true);
            if (expire == 0) {
                bac.sendMessage(p, "That's not a valid time! Example: " + Settings.VARIABLE_COLOR + "1d5h3m", false);
                return;
            }
            VAC.getVAC().EXEMPTHANDLER.addExemption(p, (int) (expire - System.currentTimeMillis()),
                    "command exempt req");
            bac.broadcast(null, Settings.VARIABLE_COLOR + "" + t.getDisplayName() + " §rwas added to the exempt list.",
                    "This will expire in " + Settings.VARIABLE_COLOR
                            + UtilTime.MakeStr((expire - System.currentTimeMillis()), 2) + "§r.");
            return;
        }
        if (args[0].equalsIgnoreCase("reload") && p.hasPermission("vac.reload")) {
            Settings.loadConfig();
            bac.sendMessage(p, "Configuration reloaded.", false);
            return;
        }
        bac.sendMessage(p,
                "Running VAC version " + Settings.VARIABLE_COLOR
                        + bac.getDescription().getVersion().replaceAll("\\[", "").replaceAll("\\]", "") + "§f by "
                        + Settings.VARIABLE_COLOR
                        + bac.getDescription().getAuthors().get(0).replaceAll("\\[", "").replaceAll("\\]", "") + "§f.",
                false);
        if (p.hasPermission("vac.admin")) {
            bac.sendMessage(p, "Use " + Settings.VARIABLE_COLOR + "/VAC help§r for a list of available commands.",
                    false);
        }
    }
}
