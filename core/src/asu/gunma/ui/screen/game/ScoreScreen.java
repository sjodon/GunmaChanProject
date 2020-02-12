package asu.gunma.ui.screen.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import asu.gunma.DatabaseInterface.DbInterface;
import asu.gunma.DbContainers.VocabWord;
import asu.gunma.speech.ActionResolver;
import asu.gunma.ui.screen.menu.LeaderboardScreen;
import asu.gunma.ui.screen.menu.MainMenuScreen;
import asu.gunma.ui.util.Animator;
import asu.gunma.ui.util.AssetManagement.GameAssets;
import asu.gunma.ui.util.BackgroundDrawer;
import asu.gunma.ui.util.GradeSystem;

public class ScoreScreen implements Screen {
    //size of round word list
    private int GAME_LIST_SIZE;
    private int currentWordIndex;

    private final int SCREEN_BOTTOM_ADJUST = 35;
    private final int CORRECT_DISPLAY_DURATION = 20;
    private final int INCORRECT_DISPLAY_DURATION = 20;

    private TextButton testButton;
    private int correctDisplayTimer;
    private int incorrectDisplayTimer;

    DbInterface dbCallback;
    private Game game;
    private Music gameMusic;
    public static float masterVolume = 5;
    public ActionResolver speechGDX;
    private Screen previousScreen;

    // Game logic variables
    private int score = 0;
    private int listCounter = 0;
    private String displayWord;
    private List<VocabWord> dbListWords;
    public ArrayList<VocabWord> activeVList;
    private GameAssets gameAssets;

    // Using these are unnecessary but will make our lives easier.
    private Stage stage;
    private TextureAtlas atlas;
    private Skin skin;
    private Table table;
    private SpriteBatch batch;

    private boolean isGameOver;

    private BitmapFont font;
    private BitmapFont font2;

    FreeTypeFontGenerator generator;
    FreeTypeFontGenerator.FreeTypeFontParameter parameter;
    FreeTypeFontGenerator.FreeTypeFontParameter parameter2;

    private Texture gunmaSprite;
    private Texture gunmaFaintedSprite;
    private Texture background;

    private GlyphLayout displayWordLayout;
    private int targetWidth = 400;

    // Animation declarations
    private Animator onionWalkAnimation;
    private Animator gunmaWalkAnimation;

    private BackgroundDrawer backgroundDrawer;
    public Texture stars;

    boolean isPaused = false;
    String cWords;
    String[] correctWordList;
    Label heading;
    TextButton continueButton;

    ArrayList<VocabWord> gameWords = new ArrayList<>();

    Preferences prefs;
    int numStars = 0;
    int levelNumber = 1;

    public ScoreScreen(Game game, ActionResolver speechGDX, Music music, DbInterface dbCallback, Screen previous, ArrayList<VocabWord> activeList, Preferences prefs, GameAssets gameAssets, int score, int numStars, int levelNumber) {
        this.game = game;
        this.prefs = prefs;
        this.speechGDX = speechGDX;
        this.dbCallback = dbCallback;
        this.previousScreen = previous;
        this.gameMusic = music;
        this.activeVList = activeList;
        this.gameAssets = gameAssets;
        this.score = score;
        this.numStars = numStars;
        this.levelNumber = levelNumber;
        gameMusic = Gdx.audio.newMusic(Gdx.files.internal(gameAssets.introMusicPath));
        gameMusic.setLooping(false);
        gameMusic.setVolume(masterVolume);
        gameMusic.play();
    }

    @Override
    public void show() {
        gameWords = new ArrayList<>();
        // gameMusic.play();
        this.correctDisplayTimer = 0;
        this.incorrectDisplayTimer = 0;

        Gdx.gl.glClearColor(.8f, 1, 1, 1);
        stage = new Stage();

        batch = new SpriteBatch();
        gunmaSprite = new Texture(gameAssets.gunmaSpritePath);
        this.gunmaFaintedSprite = new Texture(gameAssets.gunmaFaintedSpritePath);
        //onionIdleSprite = new Texture("")

        background = new Texture(gameAssets.backgroundImagePath);
        backgroundDrawer = new BackgroundDrawer(this.batch, this.SCREEN_BOTTOM_ADJUST, gameAssets);

        // Animation initializations
        this.onionWalkAnimation = new Animator(gameAssets.onionWalkAnimationPath, 4, 2, 0.1f);
        this.gunmaWalkAnimation = new Animator(gameAssets.gunmaWalkAnimationPath, 8, 1, 0.1f);


        Gdx.input.setInputProcessor(stage);

        // Defining the regions of sprite image we're going to create
        //atlas = new TextureAtlas("ui/button.pack"); // ???
        //skin = new Skin(atlas);

        table = new Table();
        table.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        //font file
        generator = new FreeTypeFontGenerator(Gdx.files.internal(gameAssets.fontPath));

        //font for vocab word
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        //font for other words
        parameter2 = new FreeTypeFontGenerator.FreeTypeFontParameter();

        //Adds 2 of each active word into gameWords
        for(int i = 0; i < activeVList.size(); i++) {
            gameWords.add(activeVList.get(i));
            gameWords.add(activeVList.get(i));
        }

        GAME_LIST_SIZE = gameWords.size();

        //db list of vocab words
        //dbListWords = dbCallback.getDbVocab();
        currentWordIndex = randomIndex(gameWords.size());
        displayWord = gameWords.get(currentWordIndex).getEngSpelling();

        //spliced correct words for grading
        cWords = gameWords.get(currentWordIndex).getCorrectWords();
        correctWordList = cWords.split("\\s*,\\s*");

        //setting font values
        parameter.characters = displayWord;
        parameter.size = 70;
        parameter.color = Color.BLACK;
        font = generator.generateFont(parameter);
        parameter2.size = 30;
        parameter2.color = Color.BLACK;
//        font2 = generator.generateFont(parameter2);
        font2 = gameAssets.getFont();

        //Alignment and Text Wrapping for Vocab Word
        displayWordLayout = new GlyphLayout();
        displayWordLayout.setText(font, displayWord, Color.BLACK, targetWidth, Align.center, true);

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        //textButtonStyle.up = skin.getDrawable("button.up");
        //textButtonStyle.down = skin.getDrawable("button.down");
        textButtonStyle.pressedOffsetX = 1;
        textButtonStyle.pressedOffsetY = -1;
        textButtonStyle.font = font2;
        textButtonStyle.fontColor = Color.BLACK;

        Label.LabelStyle headingStyle = new Label.LabelStyle(font2, Color.BLACK);
        //

        heading = new Label(gameAssets.getResourceBundle().getString("YourScore") + " " + score, headingStyle);
        stars = new Texture(gameAssets.getStarPath(numStars));
        Skin skin = gameAssets.getColorSkin(gameAssets.color2, "color2");
        textButtonStyle.up = skin.newDrawable("color2", gameAssets.color2);

        continueButton = new TextButton(gameAssets.getResourceBundle().getString("Continue"), textButtonStyle);

        heading.setFontScale(2);

            /*
                If you want to test functions with UI instead of with console,
                add it into one of these Listeners. Each of them correspond to
                one of the buttons on the screen in top-down order.
             */



        continueButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                speechGDX.stopRecognition();
                isPaused = true;
                gameMusic.dispose();
                gameMusic = Gdx.audio.newMusic(Gdx.files.internal(gameAssets.introMusicPath));
                gameMusic.setLooping(false);
                gameMusic.setVolume(masterVolume);
                gameMusic.play();
                game.setScreen(new LeaderboardScreen(game, speechGDX, gameMusic, dbCallback, activeVList, prefs, gameAssets, levelNumber));
                dispose(); // dispose of current GameScreen
            }
        });

        continueButton.pad(15);
        // Remove this later
        table.add(heading).padBottom(20);
        table.row();
        table.add(continueButton);
//        table.add(buttonTutorial);
//        table.row();

        // Remove this later
        //table.debug();

        stage.addActor(table);


        //Start Speech Recognition
        try {
            speechGDX.startRecognition();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // SpriteBatch is resource intensive, try to use it for only brief moments
        batch.begin();
        backgroundDrawer.render(true, this.isGameOver);

        //batch.draw(background, 0, 0);


        batch.draw(stars, Gdx.graphics.getWidth()/2 - stars.getWidth()/4, Gdx.graphics.getHeight()/2 + stars.getHeight()/2, stars.getWidth()/2, stars.getHeight()/2);

        batch.draw(this.gunmaFaintedSprite, 70, 10 + this.SCREEN_BOTTOM_ADJUST);

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
        speechGDX.stopRecognition();
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        gameWords.clear();
        font.dispose();
        font2.dispose();
        background.dispose();
        stars.dispose();

        this.backgroundDrawer.dispose();
        this.onionWalkAnimation.dispose();
        this.gunmaWalkAnimation.dispose();
        batch.dispose();
        stage.dispose();

    }

    private int randomIndex(int size) {

        if(size == 1) {
            return 0;
        }

        Random rand = new Random();
        int randomInt = rand.nextInt(size - 1);
        return randomInt;
    }
}
