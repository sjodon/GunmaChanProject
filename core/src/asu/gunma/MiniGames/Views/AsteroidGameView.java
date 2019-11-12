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

import asu.gunma.MiniGames.Controllers.AsteroidGameController;
import asu.gunma.MiniGames.Controllers.WordScrambleGameController;
import asu.gunma.MiniGames.Models.AsteroidGameModel;
import asu.gunma.speech.ActionResolver;
import asu.gunma.ui.util.GradeSystem;

// Use this class for the front-end of the word scramble mini-game
// You'll want to use the WordScrambleGameController class
public class AsteroidGameView implements Screen
{
    private AsteroidGameController controller;
    private GradeSystem gradeSystem;

    private Game game;
    private Music gameMusic;
    public static float masterVolume = 5;
    public ActionResolver speechGDX;
    private Screen previousScreen;

    boolean isPaused = false;

    // UI variables
    private Stage stage;

    private SpriteBatch batch;
    private Texture asteroidTexture;
    private BitmapFont font;

    FreeTypeFontGenerator generator;
    FreeTypeFontGenerator.FreeTypeFontParameter parameter;
    FreeTypeFontGenerator.FreeTypeFontParameter parameter2;
    public Preferences prefs;

    private TextButton speakButton;

    private int listCounter = 0;
    private GlyphLayout displayWordLayout;
    private int targetWidth = 400;

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
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter2 = new FreeTypeFontGenerator.FreeTypeFontParameter();

        asteroidTexture = new Texture("circle.png");
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

        parameter2.size = 30;
        parameter2.color = Color.WHITE;
        font = generator.generateFont(parameter2);

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.pressedOffsetX = 1;
        textButtonStyle.pressedOffsetY = -1;
        textButtonStyle.font = font;
        textButtonStyle.fontColor = Color.WHITE;

        speakButton = new TextButton("Speak", textButtonStyle);
        speakButton.setPosition(100 , Gdx.graphics.getHeight() - 550);

        stage.addActor(speakButton);
    }

    @Override
    public void render(float delta)
    {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();

        batch.begin();
        batch.draw(asteroidTexture, 300, 300, 100, 100);
        batch.end();
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
}
