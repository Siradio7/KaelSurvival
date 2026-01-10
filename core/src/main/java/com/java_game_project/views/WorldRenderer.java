package com.java_game_project.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.java_game_project.models.GameWorld;
import com.java_game_project.models.Ork;
import com.java_game_project.models.Player;
import com.java_game_project.utils.AnimationUtils;
import com.java_game_project.utils.EntityState;
import com.java_game_project.utils.MapManager;

public class WorldRenderer {
    private final GameWorld world;
    private final Texture treeTex, caveTex, exitTex;
    private final Animation<TextureRegion> playerWalkAnim;
    private final Animation<TextureRegion> orkWalkAnim;
    private final TextureRegion playerIdle, orkIdle;

    public WorldRenderer(GameWorld world) {
        this.world = world;

        this.treeTex = new Texture(Gdx.files.internal("maps/TilesTree.png"));
        this.caveTex = new Texture(Gdx.files.internal("maps/mystic_cave.png"));
        this.exitTex = new Texture(Gdx.files.internal("images/portail.png"));

        this.playerWalkAnim = AnimationUtils.loadAnimation("images/player_walk.png", 250, 250, 0.1f);
        this.orkWalkAnim = AnimationUtils.loadAnimation("images/ork_attack.png", 250, 250, 0.1f);

        this.playerIdle = new TextureRegion(new Texture(Gdx.files.internal("images/player.png")));
        this.orkIdle = new TextureRegion(new Texture(Gdx.files.internal("maps/ork.png")));
    }

    public void render(SpriteBatch batch, OrthographicCamera camera) {
        MapManager.getInstance().render(camera);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        Player p = world.getPlayer();
        if (p != null) {
            if (p.isInvincible() && p.getStateTime() % 0.2f < 0.1f) {

            } else {
                TextureRegion currentFrame = (p.getState() == EntityState.WALKING)
                        ? playerWalkAnim.getKeyFrame(p.getStateTime(), true)
                        : playerIdle;

                batch.draw(currentFrame, p.getPosition().x, p.getPosition().y, p.getBounds().width / 2,
                        p.getBounds().height / 2, p.getBounds().width, p.getBounds().height, 1, 1, p.getRotation());
            }
        }

        for (Rectangle r : world.getTrees()) {
            batch.draw(treeTex, r.x, r.y, r.width, r.height);
        }

        for (Ork o : world.getOrks()) {
            TextureRegion currentFrame = (o.getState() == EntityState.WALKING)
                    ? orkWalkAnim.getKeyFrame(o.getStateTime(), true)
                    : orkIdle;

            batch.draw(currentFrame, o.getPosition().x, o.getPosition().y, o.getBounds().width / 2,
                    o.getBounds().height / 2, o.getBounds().width, o.getBounds().height, 1, 1, o.getRotation());
        }

        if (world.getTarget() != null) {
            batch.draw(caveTex, world.getTarget().x, world.getTarget().y, world.getTarget().width,
                    world.getTarget().height);
        }

        if (world.getExitZone() != null) {
            float scale = 2.0f; // Increase visual size without changing hitbox
            float width = world.getExitZone().width * scale;
            float height = world.getExitZone().height * scale;
            float x = world.getExitZone().x - (width - world.getExitZone().width) / 2;
            float y = world.getExitZone().y - (height - world.getExitZone().height) / 2;
            batch.draw(exitTex, x, y, width, height);
        }

        batch.end();
    }

    public void dispose() {
        treeTex.dispose();
        caveTex.dispose();
        exitTex.dispose();
        playerIdle.getTexture().dispose();
        orkIdle.getTexture().dispose();
    }
}
