package com.java_game_project.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.java_game_project.Main;
import com.java_game_project.utils.AssetLoader;
import com.java_game_project.utils.AudioManager;
import com.java_game_project.utils.Constants;
import com.java_game_project.utils.NarrationConstants;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public class VictoryScreen extends AbstractScreen {
    private Stage stage;
    private Texture background;
    private Texture caveImage;
    private Skin skin;
    private BitmapFont font;
    private BitmapFont titleFont;

    public VictoryScreen(Main game) {
        super(game);
    }

    @Override
    public void show() {
        AudioManager.getInstance().stopMusic();

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        background = AssetLoader.getTexture(Constants.BACKGROUND);
        caveImage = AssetLoader.getTexture("maps/mystic_cave.png");

        if (Gdx.files.internal(Constants.SKIN).exists()) {
            skin = new Skin(Gdx.files.internal(Constants.SKIN));
        } else {
            skin = new Skin(Gdx.files.internal("assets/" + Constants.SKIN));
        }

        if (Gdx.files.internal(Constants.FONT).exists()) {
            font = new BitmapFont(Gdx.files.internal(Constants.FONT));
            titleFont = new BitmapFont(Gdx.files.internal(Constants.FONT));
        } else {
            font = new BitmapFont(Gdx.files.internal("assets/" + Constants.FONT));
            titleFont = new BitmapFont(Gdx.files.internal("assets/" + Constants.FONT));
        }

        Label.LabelStyle textStyle = new Label.LabelStyle(font, Color.WHITE);
        Label.LabelStyle titleStyle = new Label.LabelStyle(titleFont, new Color(1f, 0.84f, 0f, 1f));

        Label mainTitle = new Label("VICTOIRE !", titleStyle);
        mainTitle.setFontScale(2.0f);
        mainTitle.setAlignment(Align.center);

        Label subTitle = new Label("Chapitre I Termine", textStyle);
        subTitle.setFontScale(1.2f);
        subTitle.setColor(Color.LIGHT_GRAY);
        subTitle.setAlignment(Align.center);

        Image caveIcon = new Image(caveImage);

        Label storyLabel = new Label(NarrationConstants.ENDING_TEXT, textStyle);
        storyLabel.setAlignment(Align.center);
        storyLabel.setFontScale(0.9f);
        storyLabel.setWrap(true);

        TextButton menuButton = new TextButton("RETOUR AU MENU", skin);
        menuButton.getLabel().setFontScale(0.6f);

        Table table = new Table();
        table.setFillParent(true);
        table.center();
        mainTitle.setOrigin(Align.center);
        mainTitle.setScale(0);

        subTitle.getColor().a = 0;
        caveIcon.getColor().a = 0;
        storyLabel.getColor().a = 0;
        menuButton.getColor().a = 0;

        Image bgImage = new Image(background);
        bgImage.setFillParent(true);
        bgImage.setColor(0.6f, 0.4f, 0.8f, 1f);
        stage.addActor(bgImage);
        table.add(mainTitle).padBottom(10).row();
        table.add(subTitle).padBottom(30).row();
        table.add(caveIcon).size(100, 100).padBottom(30).row();
        table.add(storyLabel).width(Constants.WINDOW_WIDTH * 0.8f).padBottom(50).row();
        table.add(menuButton).size(350, 80);

        stage.addActor(table);

        mainTitle.addAction(Actions.scaleTo(1, 1, 1.0f, Interpolation.swingOut));
        subTitle.addAction(Actions.sequence(Actions.delay(0.5f), Actions.fadeIn(1f)));

        caveIcon.setOrigin(Align.center);
        caveIcon.setScale(0.5f);
        caveIcon.addAction(Actions.sequence(
                Actions.delay(1.0f),
                Actions.parallel(Actions.fadeIn(1f),
                        Actions.scaleTo(1f, 1f, 1.5f, Interpolation.pow2))));

        storyLabel.addAction(Actions.sequence(Actions.delay(2.0f), Actions.fadeIn(2.0f)));

        menuButton.addAction(Actions.sequence(Actions.delay(4.5f), Actions.fadeIn(1.0f)));

        menuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MenuScreen(game));
            }
        });

        addHoverEffect(menuButton);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        stage.act(delta);
        stage.draw();
    }

    private void addHoverEffect(final TextButton button) {
        button.addListener(new InputListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (pointer == -1) {
                    button.setTransform(true);
                    button.setScale(1.1f);
                    button.setOrigin(Align.center);
                }
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                if (pointer == -1)
                    button.setScale(1.0f);
            }
        });
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
        font.dispose();
        titleFont.dispose();
        skin.dispose();
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }
}
