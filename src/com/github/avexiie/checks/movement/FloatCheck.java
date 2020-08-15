package com.github.avexiie.checks.movement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffectType;

import com.github.avexiie.VAC;
import com.github.avexiie.checks.Check;
import com.github.avexiie.checks.CheckResult;
import com.github.avexiie.logger.User;
import com.github.avexiie.util.UtilBlock;

public class FloatCheck extends Check {

    private static Map<Player, Integer> calls = new HashMap<Player, Integer>();

    @Override
    public String getName() {
        return "FloatFlyCheck";
    }

    @Override
    public String getEventCall() {
        return "PlayerMoveEvent";
    }

    @Override
    public CheckResult performCheck(User u, Event e) {
        PlayerMoveEvent event = (PlayerMoveEvent) e;

        if (!calls.containsKey(u.getPlayer())) {
            calls.put(u.getPlayer(), 0);
        }
        int cc = calls.get(u.getPlayer());
        Player p = u.getPlayer();
        if (p.isFlying() || p.isGliding() || p.getGameMode() == GameMode.CREATIVE
                || p.getGameMode() == GameMode.SPECTATOR || p.isInsideVehicle() || p.isSwimming()
                || p.getLocation().getBlock().isLiquid()
                || p.getLocation().getBlock().getType().toString().contains("SEAGRASS")
                || p.getLocation().getBlock().getType().toString().contains("KELP")
                || p.hasPotionEffect(PotionEffectType.LEVITATION)) {
            return new CheckResult("Flight", true, "pass");
        }
        ArrayList<Block> around = UtilBlock.getSurroundingIgnoreAir(u.getBlock(), true);
        Boolean onlyliquid = true;
        for (Block b : around) {
            if (!b.isLiquid()) {
                onlyliquid = false;
            }
        }
        if (onlyliquid) {
            return new CheckResult("Flight", true, "pass");
        }
        Double mpx = event.getFrom().getY() - event.getTo().getY();
        if (event.getTo().getY() == event.getFrom().getY() && !u.getBlockBelow().isLiquid() && !p.isSwimming()
                && around.size() == 0) {
            cc++;
        } else if (mpx <= 0.007 && !p.hasPotionEffect(PotionEffectType.SLOW_FALLING) && !u.getBlockBelow().isLiquid()
                && !p.getLocation().getBlock().getRelative(BlockFace.DOWN).isLiquid() && !p.isSwimming()
                && around.size() == 0) {
            cc++;
        } else if (VAC.getVAC().getUser(p).isFalling() && mpx <= 0.07) {
            cc++;
        } else {
            if (cc > 0) {
                cc--;
            }
        }
        if (cc > 7) {
            calls.put(u.getPlayer(), 2);

            return new CheckResult("Flight", false, "floating");
        } else {
            calls.put(u.getPlayer(), cc);
        }
        return new CheckResult("Flight", true, "pass");
    }

}