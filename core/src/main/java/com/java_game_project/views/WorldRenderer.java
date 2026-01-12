package com.java_game_project.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.java_game_project.models.*;
import com.java_game_project.utils.AnimationUtils;
import com.java_game_project.utils.EntityState;
import com.java_game_project.utils.MapManager;

public class WorldRenderer {
    private final GameWorld world;
    private final Texture treeTex, caveTex, exitTex, projectileTex;
    private final Animation<TextureRegion> playerWalkAnim, playerAttackAnim;
    private final Animation<TextureRegion> orkWalkAnim;
    private final TextureRegion playerIdle, orkIdle;
    private final TextureRegion arrowRegion;
    private final BitmapFont font;

    private final Animation<TextureRegion> pouletWalkAnim;
    private final TextureRegion pouletIdle;
    private final Animation<TextureRegion> moutonWalkAnim;
    private final TextureRegion moutonIdle;

    public WorldRenderer(GameWorld world) {
        this.world = world;

        this.treeTex = new Texture(Gdx.files.internal("maps/TilesTree.png"));
        this.caveTex = new Texture(Gdx.files.internal("maps/mystic_cave.png"));
        this.exitTex = new Texture(Gdx.files.internal("images/portail.png"));
        this.projectileTex = new Texture(Gdx.files.internal("images/arrow.png"));
        this.arrowRegion = new TextureRegion(projectileTex);

        this.playerWalkAnim = AnimationUtils.loadAnimation("images/player_walk.png", 250, 250, 0.1f);
        this.playerAttackAnim = AnimationUtils.loadAnimation("images/kael_attack.png", 250, 250, 0.15f);
        this.orkWalkAnim = AnimationUtils.loadAnimation("images/ork_attack.png", 250, 250, 0.1f);

        this.playerIdle = new TextureRegion(new Texture(Gdx.files.internal("images/player.png")));
        this.orkIdle = new TextureRegion(new Texture(Gdx.files.internal("maps/ork.png")));

        this.pouletWalkAnim = AnimationUtils.loadAnimation("images/chicken.png",32, 32,0.15f);
        this.pouletIdle = pouletWalkAnim.getKeyFrames()[0];

        this.moutonWalkAnim = AnimationUtils.loadAnimation("images/sheep.png",32, 32,0.15f);
        this.moutonIdle = moutonWalkAnim.getKeyFrames()[0];

        this.font = new BitmapFont();
        this.font.setUseIntegerPositions(false);
    }

    public void render(SpriteBatch batch, OrthographicCamera camera) {
        MapManager.getInstance().render(camera);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        Player p = world.getPlayer();
        if (p != null) {
            if (p.isInvincible() && p.getStateTime() % 0.2f < 0.1f) {

            } else {
                TextureRegion currentFrame = playerIdle;
                if (p.getState() == EntityState.WALKING) {
                    currentFrame = playerWalkAnim.getKeyFrame(p.getStateTime(), true);
                } else if (p.getState() == EntityState.ATTACKING) {
                    currentFrame = playerAttackAnim.getKeyFrame(p.getStateTime(), false);
                }

                batch.draw(currentFrame, p.getPosition().x, p.getPosition().y, p.getBounds().width / 2,
                    p.getBounds().height / 2, p.getBounds().width, p.getBounds().height, 1, 1, p.getRotation());
            }
        }

        for (com.java_game_project.models.Projectile proj : world.getProjectiles()) {
            Vector2 visualPos = proj.getVisualPosition();
            batch.draw(arrowRegion, visualPos.x, visualPos.y,
                proj.getBounds().width / 2, proj.getBounds().height / 2,
                proj.getBounds().width, proj.getBounds().height, 1, 1, proj.getVisualRotation());
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

        for (Poulet poulet : world.getPoulets()) {
            TextureRegion frame =
                (poulet.getState() == EntityState.WALKING)
                    ? pouletWalkAnim.getKeyFrame(poulet.getStateTime(), true)
                    : pouletIdle;

            batch.draw(frame,
                poulet.getPosition().x, poulet.getPosition().y,
                poulet.getBounds().width / 2, poulet.getBounds().height / 2,
                poulet.getBounds().width, poulet.getBounds().height,
                1, 1, poulet.getRotation());
        }

        for (Mouton mouton : world.getMoutons()) {
            TextureRegion frame =
                (mouton.getState() == EntityState.WALKING)
                    ? moutonWalkAnim.getKeyFrame(mouton.getStateTime(), true)
                    : moutonIdle;

            batch.draw(frame,
                mouton.getPosition().x, mouton.getPosition().y,
                mouton.getBounds().width / 2, mouton.getBounds().height / 2,
                mouton.getBounds().width, mouton.getBounds().height,
                1, 1, mouton.getRotation());
        }

        if (world.getTarget() != null) {
            batch.draw(caveTex, world.getTarget().x, world.getTarget().y, world.getTarget().width,
                world.getTarget().height);
        }

        if (world.getExitZone() != null) {
            float scale = 2.0f;
            float width = world.getExitZone().width * scale;
            float height = world.getExitZone().height * scale;
            float x = world.getExitZone().x - (width - world.getExitZone().width) / 2;
            float y = world.getExitZone().y - (height - world.getExitZone().height) / 2;
            batch.draw(exitTex, x, y, width, height);
        }

        for (com.java_game_project.models.FloatingText ft : world.getFloatingTexts()) {
            font.setColor(ft.getColor().r, ft.getColor().g, ft.getColor().b, ft.getAlpha());
            font.draw(batch, ft.getText(), ft.getX(), ft.getY());
        }

        batch.end();
    }

    public void dispose() {
        treeTex.dispose();
        caveTex.dispose();
        exitTex.dispose();
        projectileTex.dispose();
        playerIdle.getTexture().dispose();
        playerIdle.getTexture().dispose();
        orkIdle.getTexture().dispose();
        font.dispose();
    }
}
