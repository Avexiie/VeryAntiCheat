package com.github.avexiie.checks.movement;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerMoveEvent;

import com.github.avexiie.checks.Check;
import com.github.avexiie.checks.CheckResult;
import com.github.avexiie.logger.User;
import com.github.avexiie.util.JVelocity;
import com.github.avexiie.util.UtilMath;

public class EntitySpeedCheck extends Check {

    @Override
    public String getName() {
        return "EntitySpeedCheck";
    }

    @Override
    public String getEventCall() {
        return "PlayerMoveEvent";
    }

    @Override
    public CheckResult performCheck(User u, Event e) {
        PlayerMoveEvent event = (PlayerMoveEvent) e;
        Player p = u.getPlayer();

        if (!p.isInsideVehicle()) {
            return new CheckResult("EntitySpeedCheck", true, "pass");
        }

        Entity v = p.getVehicle();
        if (v.getType() == EntityType.MINECART) {
            return new CheckResult("EntitySpeedCheck", true, "pass");
        }

        JVelocity jv = new JVelocity(event.getFrom(), event.getTo());
        double limit = 5.75;
        if (jv.offset() > limit) {
            return new CheckResult("Entity Speed", false,
                    "vehicle moved at " + UtilMath.trim(2, jv.offset()) + " speed, max possible is " + limit);
        }

        return new CheckResult("EntitySpeedCheck", true, "pass");
    }

}