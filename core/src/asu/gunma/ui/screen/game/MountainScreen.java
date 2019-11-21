package asu.gunma.ui.screen.game;

import java.util.ArrayList;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.audio.Music;

import asu.gunma.DatabaseInterface.*;
import asu.gunma.DbContainers.VocabWord;
import asu.gunma.speech.ActionResolver;
import asu.gunma.ui.screen.game.levels.Level1GameScreen;
import asu.gunma.ui.screen.game.levels.Level2GameScreen;
import asu.gunma.ui.screen.game.levels.Level3GameScreen;
import asu.gunma.ui.screen.game.levels.Level4GameScreen;
import asu.gunma.ui.screen.game.levels.Level5GameScreen;
import asu.gunma.ui.screen.menu.MainMenuScreen;
import asu.gunma.ui.util.Animator;
import asu.gunma.ui.util.AssetManagement.GameAssets;
import asu.gunma.ui.util.MountainBackgroundDrawer;

public class MountainScreen implements Screen {
    private final int SCREEN_BOTTOM_ADJUST = 35;

    DbInterface dbCallback;
    private Game game;
    private Music gameMusic;
    public static float masterVolume = 5;
    public ActionResolver speechGDX;
    private Screen previousScreen;

    Skin testSkin;
    private TextButton level1Button, level2Button, level3Button, level4Button, level5Button;

    private Texture background;

    // Game logic variables
    public ArrayList<VocabWord> activeVList;
    private GameAssets gameAssets;

    // Using these are unnecessary but will make our lives easier.
    private Stage stage;
    private Table table;
    private SpriteBatch batch;

    private Texture frenemy1, frenemy2, frenemy3, frenemy4, frenemy5;
    private Animator onionWalkAnimation;
    private Animator gunmaWalkAnimation;
    private MountainBackgroundDrawer backgroundDrawer;

    private TextButton backButton;

    private BitmapFont font;

    boolean isPaused = false;

    Preferences prefs;

    public MountainScreen(Game game, ActionResolver speechGDX, Music music, DbInterface dbCallback, Screen previous, ArrayList<VocabWord> activeList, Preferences prefs, GameAssets gameAssets) {
        this.game = game;
        this.prefs = prefs;
        this.speechGDX = speechGDX;
        this.dbCallback = dbCallback;
        this.previousScreen = previous;
        this.gameMusic = music;
        this.activeVList = activeList;
        this.gameAssets = gameAssets;
        gameMusic = Gdx.audio.newMusic(Gdx.files.internal(gameAssets.introMusicPath));
        gameMusic.setLooping(false);
        gameMusic.setVolume(masterVolume);
        gameMusic.play();
    }

    @Override
    public void show() {
        font = gameAssets.getFont();

        Color bgColor = gameAssets.backgroundColor;
        Gdx.gl.glClearColor(bgColor.r, bgColor.g, bgColor.b, bgColor.a);
        stage = new Stage();

        batch = new SpriteBatch();
        frenemy1 = new Texture("angryneg.png");
        frenemy2 = new Texture("cabbage1.png");
        frenemy3 = new Texture("konjackun.jpg");
        frenemy4 = new Texture(gameAssets.titleGunmaPath);
        frenemy5 = new Texture(gameAssets.titleGunmaPath);

        background = new Texture("BG_temp.png");
        backgroundDrawer = new MountainBackgroundDrawer(this.batch, this.SCREEN_BOTTOM_ADJUST);
        this.onionWalkAnimation = new Animator("onion_sheet.png", 4, 2, 0.1f);
        this.gunmaWalkAnimation = new Animator("gunma_sheet.png", 8, 1, 0.1f);

        testSkin = new Skin(Gdx.files.internal("glassy-ui.json"));

        testSkin.getFont("font-big").getData().setScale(0.1f,0.8f);

        level1Button = new TextButton("", testSkin, "default");
        level1Button.setTransform(true);
        level1Button.setWidth(100);
        level1Button.setScale(0.4f);
        level1Button.setPosition(50, 400);

        level2Button = new TextButton("", testSkin, "default");
        level2Button.setTransform(true);
        level2Button.setWidth(100);
        level2Button.setScale(0.4f);
        level2Button.setPosition(100, 400);

        level3Button = new TextButton("", testSkin, "default");
        level3Button.setTransform(true);
        level3Button.setWidth(100);
        level3Button.setScale(0.4f);
        level3Button.setPosition(150, 400);

        level4Button = new TextButton("", testSkin, "default");
        level4Button.setTransform(true);
        level4Button.setWidth(100);
        level4Button.setScale(0.4f);
        level4Button.setPosition(200, 400);

        level5Button = new TextButton("", testSkin, "default");
        level5Button.setTransform(true);
        level5Button.setWidth(100);
        level5Button.setScale(0.4f);
        level5Button.setPosition(250, 400);

        Gdx.input.setInputProcessor(stage);

        // Defining the regions of sprite image we're going to create
        //atlas = new TextureAtlas("ui/button.pack"); // ???
        //skin = new Skin(atlas);

        table = new Table();
        table.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        //textButtonStyle.up = skin.getDrawable("button.up");
        //textButtonStyle.down = skin.getDrawable("button.down");
        textButtonStyle.pressedOffsetX = 1;
        textButtonStyle.pressedOffsetY = -1;
        textButtonStyle.font = font;
        textButtonStyle.fontColor = Color.BLACK;

        backButton = new TextButton(gameAssets.getResourceBundle().getString("Back"), textButtonStyle);
        backButton.setPosition(Gdx.graphics.getWidth() - 100, 0);

        Label.LabelStyle headingStyle = new Label.LabelStyle(font, Color.BLACK);

            /*
                If you want to test functions with UI instead of with console,
                add it into one of these Listeners. Each of them correspond to
                one of the buttons on the screen in top-down order.
             */

        backButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                speechGDX.stopRecognition();
                isPaused = true;
                gameMusic.dispose();
                gameMusic = Gdx.audio.newMusic(Gdx.files.internal(gameAssets.introMusicPath));
                gameMusic.setLooping(false);
                gameMusic.setVolume(masterVolume);
                gameMusic.play();
                game.setScreen(new MainMenuScreen(game, speechGDX,  gameMusic, dbCallback,activeVList, prefs, gameAssets));
                previousScreen.dispose();
                dispose(); // dispose of current MountainScreen
            }
        });

        level1Button.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                speechGDX.stopRecognition();
                isPaused = true;
                gameMusic.dispose();
                gameMusic = Gdx.audio.newMusic(Gdx.files.internal(gameAssets.introMusicPath));
                gameMusic.setLooping(false);
                gameMusic.setVolume(masterVolume);
                gameMusic.play();
                game.setScreen(new Level1GameScreen(game, speechGDX,  gameMusic, dbCallback, game.getScreen(), activeVList, prefs, gameAssets));
                previousScreen.dispose();
                dispose(); // dispose of current MountainScreen
            }
        });

        level2Button.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                speechGDX.stopRecognition();
                isPaused = true;
                gameMusic.dispose();
                gameMusic = Gdx.audio.newMusic(Gdx.files.internal(gameAssets.introMusicPath));
                gameMusic.setLooping(false);
                gameMusic.setVolume(masterVolume);
                gameMusic.play();
                game.setScreen(new Level2GameScreen(game, speechGDX,  gameMusic, dbCallback, game.getScreen(), activeVList, prefs, gameAssets));
                previousScreen.dispose();
                dispose(); // dispose of current MountainScreen
            }
        });

        level3Button.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                speechGDX.stopRecognition();
                isPaused = true;
                gameMusic.dispose();
                gameMusic = Gdx.audio.newMusic(Gdx.files.internal(gameAssets.introMusicPath));
                gameMusic.setLooping(false);
                gameMusic.setVolume(masterVolume);
                gameMusic.play();
                game.setScreen(new Level3GameScreen(game, speechGDX,  gameMusic, dbCallback, game.getScreen(), activeVList, prefs, gameAssets));
                previousScreen.dispose();
                dispose(); // dispose of current MountainScreen
            }
        });

        level4Button.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                speechGDX.stopRecognition();
                isPaused = true;
                gameMusic.dispose();
                gameMusic = Gdx.audio.newMusic(Gdx.files.internal(gameAssets.introMusicPath));
                gameMusic.setLooping(false);
                gameMusic.setVolume(masterVolume);
                gameMusic.play();
                game.setScreen(new Level4GameScreen(game, speechGDX,  gameMusic, dbCallback, game.getScreen(), activeVList, prefs, gameAssets));
                previousScreen.dispose();
                dispose(); // dispose of current MountainScreen
            }
        });

        level5Button.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                speechGDX.stopRecognition();
                isPaused = true;
                gameMusic.dispose();
                gameMusic = Gdx.audio.newMusic(Gdx.files.internal(gameAssets.introMusicPath));
                gameMusic.setLooping(false);
                gameMusic.setVolume(masterVolume);
                gameMusic.play();
                game.setScreen(new Level5GameScreen(game, speechGDX,  gameMusic, dbCallback, game.getScreen(), activeVList, prefs, gameAssets));
                previousScreen.dispose();
                dispose(); // dispose of current MountainScreen
            }
        });

        // Remove this later
        table.debug();

        stage.addActor(backButton);
        stage.addActor(level1Button);
        stage.addActor(level2Button);
        stage.addActor(level3Button);
        stage.addActor(level4Button);
        stage.addActor(level5Button);


    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // SpriteBatch is resource intensive, try to use it for only brief moments
        batch.begin();
        backgroundDrawer.render(false,false);
//        batch.draw(this.onionWalkAnimation.getCurrentFrame(delta), 60, 35 + this.SCREEN_BOTTOM_ADJUST);
//        batch.draw(this.gunmaWalkAnimation.getCurrentFrame(delta), 200, 35 + this.SCREEN_BOTTOM_ADJUST);
        batch.draw(frenemy1, Gdx.graphics.getWidth()/2 - frenemy1.getWidth()/4 + 400, Gdx.graphics.getHeight()/4 - frenemy1.getHeight()/2 + 400, frenemy1.getWidth()/2, frenemy1.getHeight()/2);
        batch.draw(frenemy2, Gdx.graphics.getWidth()/2 - frenemy2.getWidth()/4 + 500, Gdx.graphics.getHeight()/4 - frenemy2.getHeight()/2 + 400, frenemy2.getWidth()/2, frenemy2.getHeight()/2);
        batch.draw(frenemy3, Gdx.graphics.getWidth()/2 - frenemy3.getWidth()/4 + 600, Gdx.graphics.getHeight()/4 - frenemy3.getHeight()/2 + 400, frenemy3.getWidth()/2, frenemy3.getHeight()/2);
        batch.draw(frenemy4, Gdx.graphics.getWidth()/2 - frenemy4.getWidth()/4 + 700, Gdx.graphics.getHeight()/4 - frenemy4.getHeight()/2 + 400, frenemy4.getWidth()/2, frenemy4.getHeight()/2);
        batch.draw(frenemy5, Gdx.graphics.getWidth()/2 - frenemy5.getWidth()/4 + 800, Gdx.graphics.getHeight()/4 - frenemy5.getHeight()/2 + 400, frenemy5.getWidth()/2, frenemy5.getHeight()/2);
        batch.end();

        stage.act(delta); // optional to pass delta value
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {
        gameMusic.pause();
    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        font.dispose();
        frenemy1.dispose();
        frenemy2.dispose();
        frenemy3.dispose();
        frenemy4.dispose();
        frenemy5.dispose();
        batch.dispose();
        stage.dispose();
    }

}

