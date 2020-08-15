package com.github.avexiie.checks.combat;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.potion.PotionEffectType;

import com.github.avexiie.VAC;
import com.github.avexiie.Settings;
import com.github.avexiie.checks.Check;
import com.github.avexiie.checks.CheckResult;
import com.github.avexiie.logger.User;
import com.github.avexiie.util.UtilBlock;

public class CriticalCheck extends Check {

    @Override
    public String getName() {
        return "CriticalCheck";
    }

    @Override
    public String getEventCall() {
        return "EntityDamageByEntityEvent";
    }

    @Override
    public CheckResult performCheck(User u, Event ev) {
        EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) ev;
        if (e.getCause() != DamageCause.ENTITY_ATTACK) {
            return new CheckResult("Criticals", true, "not from combat");
        }
        Player p = (Player) e.getDamager();
        if (isCritical(p)) {
            if ((p.getLocation().getY() % 1.0 == 0 || p.getLocation().getY() % 0.5 == 0)
                    && p.getLocation().clone().subtract(0, 1.0, 0).getBlock().getType().isSolid()) {
                e.setCancelled(Settings.CANCEL_ON_OFFENSE);
                VAC.getVAC().EXEMPTHANDLER.addExemptionBlock(u.getPlayer(), 250);
                return new CheckResult("Criticals", false, "player isn't actually falling.");
            }
        }
        return new CheckResult("Criticals", true, "pass");

    }

    private static boolean isCritical(Player player) {
        return player.getFallDistance() > 0.0f && !player.isOnGround() && !player.isInsideVehicle()
                && !player.hasPotionEffect(PotionEffectType.BLINDNESS)
                && !UtilBlock.isHoveringOverWater(player.getLocation(), 25)
                && player.getEyeLocation().getBlock().getType() != Material.LADDER
                && player.getEyeLocation().getBlock().getType() != Material.VINE;
    }

}
