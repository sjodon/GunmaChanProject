package asu.gunma.MiniGames.Views;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import java.util.ArrayList;

import asu.gunma.MiniGames.Controllers.AsteroidGameController;
import asu.gunma.MiniGames.Controllers.WordScrambleGameController;
import asu.gunma.MiniGames.Models.AsteroidGameModel;
import asu.gunma.speech.ActionResolver;
import asu.gunma.ui.util.GradeSystem;
import asu.gunma.ui.util.lives.LivesDrawer;

// Use this class for the front-end of the asteroid mini-game
// You'll want to use the AsteroidGameController class
public class AsteroidGameView implements Screen
{
    private AsteroidGameController controller;
    private GradeSystem gradeSystem;

    private Game game;
    private Music gameMusic;
    public static float masterVolume = 5;
    public ActionResolver speechGDX;
    private Screen previousScreen;
    private Music incorrectSound;

    // UI variables
    private Stage stage;

    private SpriteBatch batch;

    // textures
    private Texture asteroidTexture;
    private Texture rocketTexture;
    private Texture explosionTexture;

    private BitmapFont font;
    private ArrayList<BitmapFont> fontList;

    FreeTypeFontGenerator generator;
    ArrayList<FreeTypeFontGenerator.FreeTypeFontParameter> parameterList;
    FreeTypeFontGenerator.FreeTypeFontParameter parameter2;
    public Preferences prefs;

    private TextButton speakButton;
    private Label youLose;

    private ArrayList<GlyphLayout> wordLayoutList;
    private ArrayList<String> asteroidWordList;

    private LivesDrawer livesDrawer;

    private int explosionTimer;

    private Table table;

    // constants
    private static final int DEFAULT_ASTEROID_SIZE = 128;
    private static final int DEFAULT_EXPLOSION_SIZE = 160;

    public AsteroidGameView(Game game, ActionResolver speechGDX, Music music, Screen previous, Preferences prefs, AsteroidGameController controller)
    {
        this.game = game;
        this.speechGDX = speechGDX;
        this.gameMusic = music;
        this.previousScreen = previous;
        this.prefs = prefs;
        this.controller = controller;

        //fonts
        final String FONT_PATH = "irohamaru-mikami-Regular.ttf";
        generator = new FreeTypeFontGenerator(Gdx.files.internal(FONT_PATH));
        parameterList = new ArrayList<FreeTypeFontGenerator.FreeTypeFontParameter>();
        parameter2 = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontList = new ArrayList<BitmapFont>();

        // pictures
        asteroidTexture = new Texture("circle-xxl.png");
        rocketTexture = new Texture("rocket2.png");
        explosionTexture = new Texture("explosion.png");

        // initialize lists
        asteroidWordList = new ArrayList<String>();
        wordLayoutList = new ArrayList<GlyphLayout>();

        // sounds
        incorrectSound = Gdx.audio.newMusic(Gdx.files.internal("incorrect_ohno.mp3"));

        explosionTimer = 0;
    }

    // Override Screen class methods
    @Override
    public void show()
    {
        // Black background
        Gdx.gl.glClearColor(0, 0, 0, 1);
        stage = new Stage();
        batch = new SpriteBatch();
        Gdx.input.setInputProcessor(stage);
        this.livesDrawer = new LivesDrawer(this.batch);
        table = new Table();
        table.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // for displaying words on asteroids
        for (int i = 0; i < controller.getAsteroidList().size(); i++)
        {
            asteroidWordList.add(controller.getAsteroidList().get(i).getWord().getEngSpelling());
            FreeTypeFontGenerator.FreeTypeFontParameter tempParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
            tempParameter.characters = asteroidWordList.get(i);
            tempParameter.size = 16;
            tempParameter.color = Color.BLACK;
            parameterList.add(tempParameter);
            BitmapFont tempFont = generator.generateFont(parameterList.get(i));
            fontList.add(tempFont);
            GlyphLayout tempLayout = new GlyphLayout();
            tempLayout.setText(fontList.get(i), asteroidWordList.get(i), Color.BLACK,
                    DEFAULT_ASTEROID_SIZE, Align.center, true);
            wordLayoutList.add(tempLayout);
        }

        parameter2.size = 30;
        parameter2.color = Color.WHITE;
        font = generator.generateFont(parameter2);

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.pressedOffsetX = 1;
        textButtonStyle.pressedOffsetY = -1;
        textButtonStyle.font = font;
        textButtonStyle.fontColor = Color.WHITE;

        // initiate speech recognition
        try {
            speechGDX.startRecognition();

        } catch (Exception e) {
            System.out.println(e);
        }

        Label.LabelStyle youLoseStyle = new Label.LabelStyle(font, Color.WHITE);
        youLose = new Label("YOU LOSE", youLoseStyle);
        youLose.setFontScale(2);

        table.add(youLose).padBottom(100);
        table.row();

        //stage.addActor(table);
    }

    @Override
    public void render(float delta)
    {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();

        transformAsteroids(delta);

        batch.begin();

        // show the game only if the player still has lives
        if (controller.getNumLives() > 0)
        {
            this.livesDrawer.render();

            for (int i = 0; i < controller.getAsteroidList().size(); i++)
            {
                batch.draw(rocketTexture, controller.getPlayer().getPosition().x,
                        controller.getPlayer().getPosition().y, DEFAULT_ASTEROID_SIZE,
                        DEFAULT_ASTEROID_SIZE);
                batch.draw(asteroidTexture, controller.getAsteroidList().get(0).getPosition().x,
                        controller.getAsteroidList().get(0).getPosition().y, DEFAULT_ASTEROID_SIZE,
                        DEFAULT_ASTEROID_SIZE);
                fontList.get(0).draw(batch, wordLayoutList.get(0),
                        controller.getAsteroidList().get(0).getPosition().x,
                        controller.getAsteroidList().get(0).getPosition().y
                                + 2 * DEFAULT_ASTEROID_SIZE / 3);
            }

            // continue to show explosion
            if (explosionTimer > 0)
            {
                batch.draw(explosionTexture, controller.getPlayer().getPosition().x,
                        controller.getPlayer().getPosition().y, DEFAULT_EXPLOSION_SIZE,
                        DEFAULT_EXPLOSION_SIZE);

                explosionTimer++;
            }
        }
        else
        {
            stage.addActor(table);
            stage.draw();
        }

        batch.end();

        String spokenWord = speechGDX.getWord();
        String cWords = controller.getAsteroidList().get(0).getWord().getCorrectWords();
        //System.out.println(cWords);
        String[] correctWords = cWords.split("\\s*,\\s*");
        boolean correct = gradeSystem.grade(correctWords, spokenWord);

        if (correct)
        {
            // print statements are for debugging
            System.out.println("Correct!");
            System.out.println(controller.increaseScore());

            if (controller.destroyAsteroid(spokenWord))
            {
                System.out.println("Successfully destroyed the asteroid and added a new one to " +
                        "the screen.");

                asteroidWordList.remove(0);
                asteroidWordList.add(0, controller.getAsteroidList().get(0).getWord().getEngSpelling());
                FreeTypeFontGenerator.FreeTypeFontParameter tempParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
                tempParameter.characters = asteroidWordList.get(0);
                tempParameter.size = 16;
                tempParameter.color = Color.BLACK;
                parameterList.add(0, tempParameter);
                BitmapFont tempFont = generator.generateFont(parameterList.get(0));
                fontList.add(0, tempFont);
                GlyphLayout tempLayout = new GlyphLayout();
                tempLayout.setText(fontList.get(0), asteroidWordList.get(0), Color.BLACK,
                        DEFAULT_ASTEROID_SIZE, Align.center, true);
                wordLayoutList.add(0, tempLayout);
            }
            else
                System.out.println("Failed to destroy asteroid.");


        }
        // asteroid has reached rocket
        else if (controller.getAsteroidList().get(0).getPosition().y < 140)
        {
            System.out.println("Incorrect!");
            incorrectSound.setLooping(false);
            incorrectSound.setVolume(masterVolume);
            incorrectSound.play();
            livesDrawer.takeLife();
            controller.decreaseNumLives();
            controller.destroyAsteroid(controller.getAsteroidList().get(0).getWord().getEngSpelling());

            // initiate rocket explosion
            explosionTimer++;
        }

        if (explosionTimer > 60)
        {
            explosionTimer = 0;
        }
        System.out.println(explosionTimer);

    }

    @Override
    public void resize(int width, int height)
    {

    }

    @Override
    public void pause()
    {

    }

    @Override
    public void resume()
    {

    }

    @Override
    public void hide()
    {

    }

    @Override
    public void dispose()
    {
        font.dispose();
        batch.dispose();
        stage.dispose();
    }

    // other methods
    private void transformAsteroids(float delta)
    {
        for (int i = 0; i < controller.getAsteroidList().size(); i++)
        {
            // transforms asteroid according to the change in time, the asteroid's velocity,
            // direction, etc.
            controller.getAsteroidList().get(i).transformPosition(delta);
        }
    }
}
