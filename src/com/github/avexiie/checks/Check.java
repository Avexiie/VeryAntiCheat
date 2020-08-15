package com.github.avexiie.checks;

import org.bukkit.event.Event;

import com.github.avexiie.logger.User;

public abstract class Check {

    public abstract String getName();

    public abstract String getEventCall();

    public abstract CheckResult performCheck(User u, Event e);

    public CheckResult performCheck(User u) {
        return performCheck(u, null);
    }

    public String getSecondaryEventCall() {
        return "";
    }
}