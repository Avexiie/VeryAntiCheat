package com.github.avexiie.handlers;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import com.github.avexiie.VAC;
import com.github.avexiie.Settings;
import com.github.avexiie.checks.Check;
import com.github.avexiie.checks.CheckResult;
import com.github.avexiie.util.MiniPlugin;

public class BlockHandler extends MiniPlugin {

    public BlockHandler(VAC plugin) {
        super("Block Handler", plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlace(BlockPlaceEvent event) {
        if (Settings.ENABLED == false) {
            return;
        }
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

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBreak(BlockBreakEvent event) {
        if (Settings.ENABLED == false) {
            return;
        }
        for (Check c : this.getPlugin().All_Checks) {
            if (c.getEventCall().equals(event.getEventName())
                    || c.getSecondaryEventCall().equals(event.getEventName())) {
                try {
                    CheckResult result = c.performCheck(this.getPlugin().getUser(event.getPlayer()), event);
                    if (!result.passed()) {
                        this.getPlugin().addSuspicion(event.getPlayer(), result.getCheckName());
                    }
                } catch (Exception e) {

                }
            }
        }
    }
}