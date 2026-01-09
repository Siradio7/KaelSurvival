package com.java_game_project.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.java_game_project.Main;
import com.java_game_project.utils.AssetLoader;
import com.java_game_project.utils.Constants;

public class GameOverScreen extends AbstractScreen {
    private Stage stage;
    private Skin skin;
    private Texture background;
    private BitmapFont font;

    public GameOverScreen(Main game) {
        super(game);
    }

    @Override
    public void show() {
        camera.setToOrtho(false, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        background = AssetLoader.getTexture(Constants.BACKGROUND);
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.ENTER || keycode == Input.Keys.SPACE) {
                    game.setScreen(new GameScreen(game));
                    return true;
                }
                if (keycode == Input.Keys.ESCAPE) {
                    game.setScreen(new MenuScreen(game));
                    return true;
                }
                return false;
            }
        });

        skin = new Skin(Gdx.files.internal(Constants.SKIN));
        font = new BitmapFont(Gdx.files.internal(Constants.FONT));

        Pixmap bgPixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        bgPixmap.setColor(0, 0, 0, 0.85f);
        bgPixmap.fill();
        TextureRegionDrawable overlayDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(bgPixmap)));

        Table rootTable = new Table();
        rootTable.setFillParent(true);
        rootTable.setBackground(overlayDrawable);

        Label.LabelStyle titleStyle = new Label.LabelStyle(font, Color.FIREBRICK);
        titleStyle.font.getData().setScale(10f);

        Label gameOverLabel = new Label("GAME OVER", titleStyle);

        Label.LabelStyle textStyle = new Label.LabelStyle(font, Color.LIGHT_GRAY);
        textStyle.font.getData().setScale(2.0f);
        Label messageLabel = new Label("Kael a echoue dans sa quete\nL'espoir s'est eteint avec lui", textStyle);
        messageLabel.setAlignment(1);

        TextButton retryButton = new TextButton("REESSAYER (Entree)", skin);
        TextButton menuButton = new TextButton("MENU (Echap)", skin);

        retryButton.getLabel().setFontScale(0.4f);
        menuButton.getLabel().setFontScale(0.4f);

        rootTable.add(gameOverLabel).padBottom(50).row();
        rootTable.add(messageLabel).padBottom(60).row();

        Table buttonTable = new Table();
        buttonTable.add(retryButton).size(300, 80).padRight(30);
        buttonTable.add(menuButton).size(300, 80);

        rootTable.add(buttonTable);

        stage.addActor(rootTable);

        retryButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                audioManager.playSound(Constants.MOUSE_CLICK_SOUND);
                game.setScreen(new GameScreen(game));
            }
        });

        menuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                audioManager.playSound(Constants.MOUSE_CLICK_SOUND);
                game.setScreen(new MenuScreen(game));
            }
        });
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(background, 0, 0, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        batch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
        font.dispose();
    }
}
