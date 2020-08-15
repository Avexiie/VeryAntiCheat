package com.github.avexiie.handlers;

import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRiptideEvent;

import com.github.avexiie.VAC;
import com.github.avexiie.Settings;
import com.github.avexiie.checks.Check;
import com.github.avexiie.checks.CheckResult;
import com.github.avexiie.util.MiniPlugin;

public class MovementHandler extends MiniPlugin {

    public MovementHandler(VAC plugin) {
        super("Movement Handler", plugin);
    }

    @EventHandler(ignoreCancelled = true)
    public void onMove(PlayerMoveEvent event) {
        if (Settings.ENABLED == false || event.getPlayer().getGameMode() == GameMode.CREATIVE
                || event.getPlayer().getGameMode() == GameMode.SPECTATOR) {
            return;
        }
        if (!this.getPlugin().EXEMPTHANDLER.isExempt(event.getPlayer())) {
            for (Check c : this.getPlugin().All_Checks) {
                if (c.getEventCall().equals(event.getEventName())
                        || c.getSecondaryEventCall().equals(event.getEventName())) {
                    CheckResult result = c.performCheck(this.getPlugin().getUser(event.getPlayer()), event);
                    if (!result.passed()) {
                        this.getPlugin().addSuspicion(event.getPlayer(), result.getCheckName());
                    }
                }
            }
        }

    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void riptide(PlayerRiptideEvent event) {
        this.getPlugin().EXEMPTHANDLER.addExemption(event.getPlayer(), 2000, "Riptide");
    }

}
