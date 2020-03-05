package asu.gunma.ui.screen.menu;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import asu.gunma.DatabaseInterface.DbInterface;
import asu.gunma.DbContainers.VocabWord;
import asu.gunma.speech.ActionResolver;
import asu.gunma.ui.util.Animator;
import asu.gunma.ui.util.AssetManagement.GameAssets;
import jdk.nashorn.internal.ir.Block;

public class CustomizeMenu implements Screen {

    private Game game;
    private Music gameMusic;
    public static float masterVolume = 5;
    private ActionResolver speechGDX;
    private DbInterface dbInterface;
    private AssetManager assetManager;
    private Screen previousScreen;
    private GameAssets gameAssets;

    private final int SCREEN_BOTTOM_ADJUST = 35;
    private Animator gunmaWalkAnimation, gunmaGreenBagAnimation, gunmaBowTieAnimation, gunmaGreenHatAnimation, gunmaRainbowBagAnimation;

    // Using these are unnecessary but will make our lives easier.
    private Stage stage;
    private TextureAtlas atlas;
    private TextureAtlas.AtlasRegion region;
    private Skin testSkin;
    private Table table, table2, table3, table4, table5, table6;

    //true = instructor, false = student
    private boolean verified = true;
    private boolean active1 = true, active2 = true, active3 = true, active4 = true, active5 = true,
            active6 = true, active7 = true, active8 = true, active9 = true, active10 = true,
            active11 = false, active12 = false, active13 = false, active14 = false, active15 = false;
    public ArrayList<VocabWord> activeVocabList = new ArrayList<>();
    //temp bool until login system works
    private boolean login = false;
    private boolean deleteButtonsVisible = false;
    private int activeLimit = 10;

    private TextButton buttonCustom1, buttonCustom2, buttonCustom3, buttonCustom4, buttonCustom5, buttonCustom6,
            buttonCustom7, buttonCustom8, buttonCustom9, buttonCustom10, buttonCustom11, buttonCustom12,
            buttonCustom13, buttonCustom14, buttonCustom15;

    private TextButton deleteCustomButton11, deleteCustomButton12, deleteCustomButton13, deleteCustomButton14,
            deleteCustomButton15;

    private TextButton newButton, deleteButton, settingsButton, backButton;
    private ArrayList<TextButton> buttonList;
    private ArrayList<TextButton> deleteButtonList;
    private BitmapFont font;

    FreeTypeFontGenerator generator;
    FreeTypeFontGenerator.FreeTypeFontParameter parameter;

    private SpriteBatch batch;
    private Texture texture;

    private ScrollPane fileSelectionPane;
    private Table fileTable;
    public Preferences prefs;
    File currentFile = null;

    public CustomizeMenu(Game game, ActionResolver speechGDX, Music music, DbInterface dbInterface, Screen previousScreen, ArrayList<VocabWord> arrayList, Preferences prefs, GameAssets gameAssets) {
        this.game = game;
        this.prefs = prefs;
        this.speechGDX = speechGDX;
        this.gameMusic = music;
        this.dbInterface = dbInterface;
        this.previousScreen = previousScreen;
        this.activeVocabList = arrayList;
        this.gameAssets = gameAssets;
        gameMusic = Gdx.audio.newMusic(Gdx.files.internal(gameAssets.introMusicPath));
        gameMusic.setLooping(false);
        gameMusic.setVolume(masterVolume);
        gameMusic.play();
    }

    public CustomizeMenu(Game game, ActionResolver speechGDX, Music music, DbInterface dbInterface, Screen previousScreen, Preferences prefs, GameAssets gameAssets) {
        this.game = game;
        this.prefs = prefs;
        this.speechGDX = speechGDX;
        this.gameMusic = music;
        this.dbInterface = dbInterface;
        this.previousScreen = previousScreen;
        this.gameAssets = gameAssets;
        gameMusic = Gdx.audio.newMusic(Gdx.files.internal(gameAssets.introMusicPath));
        gameMusic.setLooping(false);
        gameMusic.setVolume(masterVolume);
        gameMusic.play();
    }

    // need to delete words from database when a delete button is clicked
    // delete buttons in wrong location


    //CREATE COUNTER THAT CONTROLS IF THE MODULE IS THE LAST ONE ACTIVE
    //AND PREVENT IT FROM BEING DEACTIVATED IF TRUE
    @Override
    public void show() {
        Color bgColor = gameAssets.backgroundColor;
        Gdx.gl.glClearColor(bgColor.r, bgColor.g, bgColor.b, bgColor.a);
        stage = new Stage();
        batch = new SpriteBatch();
        texture = new Texture(gameAssets.titleGunmaPath);

        Gdx.input.setInputProcessor(stage);
        assetManager = new AssetManager();
        testSkin = new Skin(Gdx.files.internal("glassy-ui.json"));

        table = new Table();

        table.setPosition(155, 275);

        this.gunmaWalkAnimation = new Animator(gameAssets.gunmaWalkAnimation, 8, 1, 0.1f);

        this.gunmaGreenBagAnimation = new Animator(gameAssets.gunmaGreenBagAnimation, 8, 1, 0.1f);
        this.gunmaBowTieAnimation = new Animator(gameAssets.gunmaBowTieAnimation, 8, 1, 0.1f);
        this.gunmaGreenHatAnimation = new Animator(gameAssets.gunmaGreenHatAnimation, 8, 1, 0.1f);
        this.gunmaRainbowBagAnimation = new Animator(gameAssets.gunmaRainbowBagAnimation, 8, 1, 0.1f);

//        font = new BitmapFont(Gdx.files.internal("font-export.fnt")); // needs a font file still
//        font.setColor(Color.BLACK); // Does nothing at the moment
//        font.getData().setScale(2);
//        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        //font file
//        final String FONT_PATH = "irohamaru-mikami-Regular.ttf";
//        generator = new FreeTypeFontGenerator(Gdx.files.internal(FONT_PATH));

        //font for vocab word
//        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

//        parameter.size = 15;
//        parameter.color = Color.BLACK;
        font = gameAssets.getFont();

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        //textButtonStyle.up = skin.getDrawable("button.up");
        //textButtonStyle.down = skin.getDrawable("button.down");
        textButtonStyle.pressedOffsetX = 1;
        textButtonStyle.pressedOffsetY = -1;
        textButtonStyle.font = font;
        textButtonStyle.fontColor = Color.BLACK;

        backButton = new TextButton(gameAssets.getResourceBundle().getString("Back"), textButtonStyle);
        backButton.setPosition(Gdx.graphics.getWidth() - 100, 0);

        backButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                speechGDX.stopRecognition();
                gameMusic.dispose();
                gameMusic = Gdx.audio.newMusic(Gdx.files.internal(gameAssets.introMusicPath));
                gameMusic.setLooping(false);
                gameMusic.setVolume(masterVolume);
                gameMusic.play();
                game.setScreen(new MainMenuScreen(game, speechGDX,  gameMusic, dbInterface,activeVocabList, prefs, gameAssets));
                dispose(); // dispose of current GameScreen
            }
        });

        // Remove this later
        table.debug();

        stage.addActor(backButton);

//        table.setTransform(true);
//        table.setScale(0.5f);
//
//        stage.addActor(table);
//        stage.addActor(settingsButton);
//        stage.addActor(backButton);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Texture border = new Texture("brown_border.png");

        int padding = 10;

        // SpriteBatch is resource intensive, try to use it for only brief moments
        batch.begin();

        batch.draw(border, Gdx.graphics.getWidth()/2 - 2*border.getWidth() - 3*padding, Gdx.graphics.getHeight()/2 + padding);
        batch.draw(this.gunmaWalkAnimation.getCurrentFrame(delta), Gdx.graphics.getWidth()/2 - 2*border.getWidth() - 3*padding, Gdx.graphics.getHeight()/2 + padding);

        batch.draw(border, Gdx.graphics.getWidth()/2 - border.getWidth() - padding, Gdx.graphics.getHeight()/2 + padding);
        batch.draw(this.gunmaGreenBagAnimation.getCurrentFrame(delta), Gdx.graphics.getWidth()/2 - border.getWidth() - padding, Gdx.graphics.getHeight()/2 + padding);

        batch.draw(border, Gdx.graphics.getWidth()/2 + padding, Gdx.graphics.getHeight()/2 + padding);
        batch.draw(this.gunmaBowTieAnimation.getCurrentFrame(delta), Gdx.graphics.getWidth()/2 + padding, Gdx.graphics.getHeight()/2 + padding);

        batch.draw(border, Gdx.graphics.getWidth()/2 + border.getWidth() + 3*padding, Gdx.graphics.getHeight()/2 + padding);
        batch.draw(this.gunmaGreenHatAnimation.getCurrentFrame(delta), Gdx.graphics.getWidth()/2 + border.getWidth() + 3*padding, Gdx.graphics.getHeight()/2 + padding);



        batch.draw(border, Gdx.graphics.getWidth()/2 - 2*border.getWidth() - 3*padding, Gdx.graphics.getHeight()/2 - border.getHeight() - padding);
        batch.draw(this.gunmaRainbowBagAnimation.getCurrentFrame(delta), Gdx.graphics.getWidth()/2 - 2*border.getWidth() - 3*padding, Gdx.graphics.getHeight()/2 - border.getHeight() - padding);

        batch.draw(border, Gdx.graphics.getWidth()/2 - border.getWidth() - padding, Gdx.graphics.getHeight()/2 - border.getHeight() - padding);

        batch.draw(border, Gdx.graphics.getWidth()/2 + padding, Gdx.graphics.getHeight()/2 - border.getHeight() - padding);

        batch.draw(border, Gdx.graphics.getWidth()/2 + border.getWidth() + 3*padding, Gdx.graphics.getHeight()/2 - border.getHeight() - padding);



//        batch.draw(border, Gdx.graphics.getWidth()/2 - 2*border.getWidth() - 2*padding, Gdx.graphics.getHeight()/2 + 2*padding);
//        batch.draw(border, Gdx.graphics.getWidth()/2 - 2*border.getWidth() - 2*padding, Gdx.graphics.getHeight()/2 - 2*border.getHeight() - 2*padding);
//        batch.draw(border, Gdx.graphics.getWidth()/2 + 2*padding, Gdx.graphics.getHeight()/2 - 2*border.getHeight() - 2*padding);
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
        testSkin.dispose();
        assetManager.dispose();
        this.gunmaWalkAnimation.dispose();
        texture.dispose();
        batch.dispose();
        stage.dispose();
    }
}
