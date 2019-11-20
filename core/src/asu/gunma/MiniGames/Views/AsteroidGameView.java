package asu.gunma.MiniGames.Views;

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
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import java.util.ArrayList;

import asu.gunma.MiniGames.Controllers.AsteroidGameController;
import asu.gunma.MiniGames.Controllers.WordScrambleGameController;
import asu.gunma.MiniGames.Models.AsteroidGameModel;
import asu.gunma.speech.ActionResolver;
import asu.gunma.ui.util.GradeSystem;

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

    // UI variables
    private Stage stage;

    private SpriteBatch batch;
    private Texture asteroidTexture;
    private Texture rocketTexture;
    private BitmapFont font;
    private ArrayList<BitmapFont> fontList;

    FreeTypeFontGenerator generator;
    ArrayList<FreeTypeFontGenerator.FreeTypeFontParameter> parameterList;
    FreeTypeFontGenerator.FreeTypeFontParameter parameter2;
    public Preferences prefs;

    private TextButton speakButton;

    private ArrayList<GlyphLayout> wordLayoutList;
    private ArrayList<String> asteroidWordList;

    // constants
    private static final int DEFAULT_ASTEROID_SIZE = 128;

    public AsteroidGameView(Game game, ActionResolver speechGDX, Music music, Screen previous, Preferences prefs, AsteroidGameController controller)
    {
        this.game = game;
        this.speechGDX = speechGDX;
        this.gameMusic = music;
        this.previousScreen = previous;
        this.prefs = prefs;
        this.controller = controller;

        //font file
        final String FONT_PATH = "irohamaru-mikami-Regular.ttf";
        generator = new FreeTypeFontGenerator(Gdx.files.internal(FONT_PATH));
        parameterList = new ArrayList<FreeTypeFontGenerator.FreeTypeFontParameter>();
        parameter2 = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontList = new ArrayList<BitmapFont>();
        asteroidTexture = new Texture("circle-xxl.png");
        rocketTexture = new Texture("rocket2.png");
        asteroidWordList = new ArrayList<String>();
        wordLayoutList = new ArrayList<GlyphLayout>();
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

        // for displaying words on asteroids
        for (int i = 0; i < controller.getAsteroidList().size(); i++)
        {
            asteroidWordList.add(controller.getAsteroidList().get(i).getWord().getEngSpelling());
            FreeTypeFontGenerator.FreeTypeFontParameter tempParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
            tempParameter.characters = asteroidWordList.get(i);
            tempParameter.size = 20;
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

        //speakButton = new TextButton("Speak", textButtonStyle);
        //speakButton.setPosition(100 , Gdx.graphics.getHeight() - 550);

        //stage.addActor(speakButton);

        // initiate speech recognition
        try {
            speechGDX.startRecognition();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void render(float delta)
    {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();

        transformAsteroids(delta);

        batch.begin();

        for (int i = 0; i < controller.getAsteroidList().size(); i++)
        {
            batch.draw(rocketTexture, controller.getPlayer().getPosition().x,
                    controller.getPlayer().getPosition().y, DEFAULT_ASTEROID_SIZE,
                    DEFAULT_ASTEROID_SIZE);
            batch.draw(asteroidTexture, controller.getAsteroidList().get(i).getPosition().x,
                    controller.getAsteroidList().get(i).getPosition().y, DEFAULT_ASTEROID_SIZE,
                    DEFAULT_ASTEROID_SIZE);
            fontList.get(i).draw(batch, wordLayoutList.get(i),
                    controller.getAsteroidList().get(i).getPosition().x,
                    controller.getAsteroidList().get(i).getPosition().y
                            + 2 * DEFAULT_ASTEROID_SIZE / 3);
        }

        batch.end();

        String spokenWord = speechGDX.getWord();
        String cWords = controller.getAsteroidList().get(0).getWord().getCorrectWords();
        //System.out.println(cWords);
        String[] correctWords = cWords.split("\\s*,\\s*");
        boolean correct = gradeSystem.grade(correctWords, spokenWord);

        if (correct)
        {
            System.out.println("Correct!");
            System.out.println(controller.increaseScore());
        }
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
