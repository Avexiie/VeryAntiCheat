package com.github.avexiie.handlers;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.spigotmc.event.entity.EntityDismountEvent;

import com.github.avexiie.VAC;
import com.github.avexiie.Settings;
import com.github.avexiie.checks.Check;
import com.github.avexiie.checks.CheckResult;
import com.github.avexiie.util.MiniPlugin;

public class DamageHandler extends MiniPlugin {

    public DamageHandler(VAC plugin) {
        super("Damage Handler", plugin);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player)
            VAC.getVAC().EXEMPTHANDLER.addExemption((Player) event.getEntity(), 845, "damaged");
    }

    @EventHandler
    public void onEject(EntityDismountEvent event) {
        if (event.getEntity() instanceof Player) {
            Player p = (Player) event.getEntity();
            VAC.getVAC().EXEMPTHANDLER.addExemption(p, 500, "vehicle exempt");
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onDamage(EntityDamageByEntityEvent event) {
        if (Settings.ENABLED == false) {
            return;
        }
        if (event.getDamager() instanceof Player) {
            Player p = (Player) event.getDamager();
            for (Check c : this.getPlugin().All_Checks) {
                if (c.getEventCall().equals(event.getEventName())
                        || c.getSecondaryEventCall().equals(event.getEventName())) {
                    CheckResult result = c.performCheck(this.getPlugin().getUser(p), event);
                    if (!result.passed()) {
                        this.getPlugin().addSuspicion(p, result.getCheckName());
                    }
                }
            }
        }
    }
}