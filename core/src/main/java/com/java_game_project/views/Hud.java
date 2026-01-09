package com.java_game_project.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.java_game_project.models.Player;
import com.java_game_project.utils.Constants;

public class Hud implements Disposable {
    public Stage stage;
    private Viewport viewport;

    private Integer worldTimer;
    private float timeCount;

    private Label countdownLabel;
    private Label timeLabel;
    private Label healthLabel;
    private Label healthValueLabel;

    private Player player;
    private BitmapFont font;

    private Image healthBar;
    private float maxBarWidth = 200f;
    private TextureRegionDrawable greenDrawable, yellowDrawable, redDrawable;

    public Hud(SpriteBatch sb, Player player) {
        this.player = player;
        worldTimer = 0;
        timeCount = 0;

        viewport = new StretchViewport(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);

        Pixmap barPixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);

        barPixmap.setColor(Color.GREEN);
        barPixmap.fill();
        greenDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(barPixmap)));

        barPixmap.setColor(Color.YELLOW);
        barPixmap.fill();
        yellowDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(barPixmap)));

        barPixmap.setColor(Color.RED);
        barPixmap.fill();
        redDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(barPixmap)));

        barPixmap.dispose();

        font = new BitmapFont(Gdx.files.internal(Constants.FONT));

        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.LIGHT_GRAY);
        labelStyle.font.getData().setScale(1.2f);

        Label.LabelStyle valueStyle = new Label.LabelStyle(font, Color.WHITE);
        valueStyle.font.getData().setScale(2.0f);

        Table rootTable = new Table();
        rootTable.top().padTop(20).padLeft(20).padRight(20);
        rootTable.setFillParent(true);

        timeLabel = new Label("TIME", labelStyle);
        countdownLabel = new Label(String.format("%03d", worldTimer), valueStyle);

        Table timeTable = new Table();
        timeTable.add(timeLabel).align(Align.left).row();
        timeTable.add(countdownLabel).align(Align.left);

        healthLabel = new Label("HEALTH", labelStyle);
        healthValueLabel = new Label("100%", valueStyle);

        healthBar = new Image(greenDrawable);
        healthBar.setOrigin(Align.right);

        Table healthTable = new Table();
        healthTable.add(healthLabel).align(Align.right).row();
        healthTable.add(healthValueLabel).align(Align.right).row();
        healthTable.add(healthBar).size(maxBarWidth, 20).align(Align.right).padTop(5);

        rootTable.add(timeTable).expandX().align(Align.topLeft);
        rootTable.add(healthTable).expandX().align(Align.topRight);

        stage.addActor(rootTable);
    }

    public void update(float dt) {
        timeCount += dt;
        if (timeCount >= 1) {
            worldTimer++;
            countdownLabel.setText(String.format("%03d", worldTimer));
            timeCount = 0;
        }

        if (player != null) {
            float healthPercent = (float) player.getHealth() / player.getMaxHealth();
            healthValueLabel.setText(player.getHealth() + "%");

            if (healthPercent > 0.5f) {
                healthBar.setDrawable(greenDrawable);
            } else if (healthPercent > 0.25f) {
                healthBar.setDrawable(yellowDrawable);
            } else {
                healthBar.setDrawable(redDrawable);
            }

            if (healthPercent < 0.3f) {
                healthValueLabel.setColor(Color.RED);
            } else {
                healthValueLabel.setColor(Color.WHITE);
            }

            healthBar.setScaleX(healthPercent);
        }
    }

    @Override
    public void dispose() {
        stage.dispose();
        font.dispose();
    }
}
