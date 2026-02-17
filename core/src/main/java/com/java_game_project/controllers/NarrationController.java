package com.java_game_project.controllers;

import com.badlogic.gdx.graphics.Color;
import com.java_game_project.models.FloatingText;
import com.java_game_project.models.GameWorld;
import com.java_game_project.utils.NarrationConstants;

public class NarrationController {
    private final GameWorld world;

    private boolean startThoughtTriggered = false;
    private boolean exitThoughtTriggered = false;
    private boolean lowHealthTriggered = false;

    public NarrationController(GameWorld world) {
        this.world = world;
    }

    public void update() {
        if (!startThoughtTriggered && world.getTime() > 1.0f) {
            world.addFloatingText(new FloatingText(NarrationConstants.GAME_START_THOUGHT,
                    world.getPlayer().getPosition().x - 100, world.getPlayer().getPosition().y + 100, Color.CYAN,
                    FloatingText.Type.THOUGHT));
            startThoughtTriggered = true;
        }

        if (!exitThoughtTriggered && world.getExitZone() != null) {
            float dist = world.getPlayer().getPosition().dst(world.getExitZone().x, world.getExitZone().y);
            if (dist < 400) {
                world.addFloatingText(new FloatingText(NarrationConstants.EXIT_NEAR_THOUGHT,
                        world.getPlayer().getPosition().x - 100, world.getPlayer().getPosition().y + 100, Color.CYAN,
                        FloatingText.Type.THOUGHT));
                exitThoughtTriggered = true;
            }
        }

        if (!lowHealthTriggered && world.getPlayer().getHealth() < 30) {
            world.addFloatingText(new FloatingText(NarrationConstants.LOW_HEALTH_THOUGHT,
                    world.getPlayer().getPosition().x, world.getPlayer().getPosition().y + 100, Color.ORANGE,
                    FloatingText.Type.THOUGHT));
            lowHealthTriggered = true;
        } else if (lowHealthTriggered && world.getPlayer().getHealth() > 50) {
            lowHealthTriggered = false;
        }
    }
}
