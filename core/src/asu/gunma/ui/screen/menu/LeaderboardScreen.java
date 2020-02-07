package asu.gunma.ui.screen.menu;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.awt.Font;
import java.util.ArrayList;

import asu.gunma.DatabaseInterface.DbInterface;
import asu.gunma.DbContainers.VocabWord;
import asu.gunma.speech.ActionResolver;
import asu.gunma.ui.screen.game.FlashcardScreen;
import asu.gunma.ui.screen.game.GameScreen;
import asu.gunma.ui.util.Animator;
import asu.gunma.ui.util.AssetManagement.GameAssets;
import asu.gunma.ui.util.BackgroundDrawer;

public class LeaderboardScreen implements Screen {

    // This is needed to change screens later on
    private Game game;
    public ActionResolver speechGDX;
    public DbInterface dbCallback;
    public Music gameMusic;
    public static float masterVolume = 5;
    public ArrayList<VocabWord> activeVList = new ArrayList<>();
    private GameAssets gameAssets;
    private final int SCREEN_BOTTOM_ADJUST = 35;

    // Using these are unnecessary but will make our lives easier.
    private Stage stage;
    private TextureAtlas atlas;
    private Skin skin;
    private Table table;
    private Texture background;

    private int testInt = 0;

    /*
        We will need 5 different buttons for this menu:
          1. Video Tutorials
          2. Flashcards
          3. Game #1
          4. Game #2
          5. Game #3
        This is based on the Project Proposal, I'd like to change this
        before the final release.
     */
    private Label buttonTutorial, buttonFlashcard, buttonGameFirst, buttonGameSecond, buttonGameThird, buttonLeaderboard, buttonSettings;
    private TextButton backButton;

    private SpriteBatch batch;
    private Animator onionWalkAnimation;
    private Animator gunmaWalkAnimation;
    private BackgroundDrawer backgroundDrawer;

    private BitmapFont font;
    private Label heading;

    FreeTypeFontGenerator generator;
    FreeTypeFontGenerator.FreeTypeFontParameter parameter;
    FreeTypeFontGenerator.FreeTypeFontParameter parameter2;
    public Preferences prefs;

    public LeaderboardScreen(Game game, ActionResolver speechGDX, Music music, DbInterface dbCallback, ArrayList<VocabWord> activeList, Preferences prefs, GameAssets gameAssets) {
        this.game = game;
        this.prefs = prefs;
        this.speechGDX = speechGDX;
        this.gameMusic = music;
        this.dbCallback = dbCallback;
        this.activeVList = activeList;
        this.gameAssets = gameAssets;
    }

    @Override
    public void show() {
        //font file
//        generator = new FreeTypeFontGenerator(Gdx.files.internal(gameAssets.fontPath));
        final String FONT_PATH = "irohamaru-mikami-Regular.ttf";

        generator = new FreeTypeFontGenerator(Gdx.files.internal(FONT_PATH));

        //font for vocab word
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        //font for other words
        parameter2 = new FreeTypeFontGenerator.FreeTypeFontParameter();

        font = gameAssets.getFont();

        Color bgColor = gameAssets.backgroundColor;
        Gdx.gl.glClearColor(bgColor.r, bgColor.g, bgColor.b, bgColor.a);
        stage = new Stage();

        batch = new SpriteBatch();

        background = new Texture("BG_temp.png");
        backgroundDrawer = new BackgroundDrawer(this.batch, this.SCREEN_BOTTOM_ADJUST);
        this.onionWalkAnimation = new Animator("onion_sheet.png", 4, 2, 0.1f);
        this.gunmaWalkAnimation = new Animator("gunma_sheet.png", 8, 1, 0.1f);

        Gdx.input.setInputProcessor(stage);

        table = new Table();
        table.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        Skin skin = gameAssets.getColorSkin(gameAssets.color4, "color4");
        Label.LabelStyle itemStyle = new Label.LabelStyle();
        //itemStyle.up = skin.getDrawable("button.up");
        //itemStyle.down = skin.getDrawable("button.down");
        itemStyle.font = font;
        itemStyle.fontColor = Color.BLACK;
//        itemStyle.background = skin.newDrawable("color4", gameAssets.color4);

        // IMPORTANT: needs localization support
        Label place1 = new Label("1", itemStyle);
        Label place2 = new Label("2", itemStyle);
        Label place3 = new Label("3", itemStyle);
        Label place4 = new Label("4", itemStyle);
        Label place5 = new Label("5", itemStyle);
        Label place6 = new Label("6", itemStyle);
        Label place7 = new Label("7", itemStyle);
        Label place8 = new Label("8", itemStyle);
        Label place9 = new Label("9", itemStyle);
        Label place10 = new Label("10", itemStyle);

        Label name1 = new Label("ABCDEFGHIJKLMNO", itemStyle);
        Label name2 = new Label("ABCDEFGHIJKLMNO", itemStyle);
        Label name3 = new Label("ABCDEFGHIJKLMNO", itemStyle);
        Label name4 = new Label("ABCDEFGHIJKLMNO", itemStyle);
        Label name5 = new Label("ABCDEFGHIJKLMNO", itemStyle);
        Label name6 = new Label("ABCDEFGHIJKLMNO", itemStyle);
        Label name7 = new Label("ABCDEFGHIJKLMNO", itemStyle);
        Label name8 = new Label("ABCDEFGHIJKLMNO", itemStyle);
        Label name9 = new Label("ABCDEFGHIJKLMNO", itemStyle);
        Label name10 = new Label("ABCDEFGHIJKLMNO", itemStyle);

        Label score1 = new Label("100", itemStyle);
        Label score2 = new Label("200", itemStyle);
        Label score3 = new Label("300", itemStyle);
        Label score4 = new Label("400", itemStyle);
        Label score5 = new Label("500", itemStyle);
        Label score6 = new Label("600", itemStyle);
        Label score7 = new Label("700", itemStyle);
        Label score8 = new Label("800", itemStyle);
        Label score9 = new Label("900", itemStyle);
        Label score10 = new Label("1000", itemStyle);


        table.add(place1).padBottom(15).expandX();
        table.add(name1).padBottom(15).expandX();
        table.add(score1).padBottom(15).expandX();
        table.row();
        table.add(place2).padBottom(15).expandX();
        table.add(name2).padBottom(15).expandX();
        table.add(score2).padBottom(15).expandX();
        table.row();
        table.add(place3).padBottom(15).expandX();
        table.add(name3).padBottom(15).expandX();
        table.add(score3).padBottom(15).expandX();
        table.row();
        table.add(place4).padBottom(15).expandX();
        table.add(name4).padBottom(15).expandX();
        table.add(score4).padBottom(15).expandX();
        table.row();
        table.add(place5).padBottom(15).expandX();
        table.add(name5).padBottom(15).expandX();
        table.add(score5).padBottom(15).expandX();
        table.row();
        table.add(place6).padBottom(15).expandX();
        table.add(name6).padBottom(15).expandX();
        table.add(score6).padBottom(15).expandX();
        table.row();
        table.add(place7).padBottom(15).expandX();
        table.add(name7).padBottom(15).expandX();
        table.add(score7).padBottom(15).expandX();
        table.row();
        table.add(place8).padBottom(15).expandX();
        table.add(name8).padBottom(15).expandX();
        table.add(score8).padBottom(15).expandX();
        table.row();
        table.add(place9).padBottom(15).expandX();
        table.add(name9).padBottom(15).expandX();
        table.add(score9).padBottom(15).expandX();
        table.row();
        table.add(place10).padBottom(15).expandX();
        table.add(name10).padBottom(15).expandX();
        table.add(score10).padBottom(15).expandX();
        table.row();

        stage.addActor(table);

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
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
                game.setScreen(new MainMenuScreen(game, speechGDX,  gameMusic, dbCallback,activeVList, prefs, gameAssets));
                dispose(); // dispose of current GameScreen
            }
        });

        stage.addActor(backButton);


//        table.add(buttonTutorial).padBottom(15);
//        table.row();
//        table.add(buttonFlashcard).padBottom(15);
//        table.row();
//        table.add(buttonGameFirst).padBottom(15);
//        table.row();
//        table.add(buttonGameSecond).padBottom(15);
//        table.row();
//        table.add(buttonGameThird).padBottom(15);
//        table.row();
//        table.add(buttonSettings);
//        table.row();
//
//        stage.addActor(table);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // SpriteBatch is resource intensive, try to use it for only brief moments
        batch.begin();
        backgroundDrawer.render(false,false);
        batch.draw(this.gunmaWalkAnimation.getCurrentFrame(delta), 60, 35 + this.SCREEN_BOTTOM_ADJUST);
//        batch.draw(this.gunmaWalkAnimation.getCurrentFrame(delta), 200, 35 + this.SCREEN_BOTTOM_ADJUST);
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
        batch.dispose();
        stage.dispose();
    }

}

