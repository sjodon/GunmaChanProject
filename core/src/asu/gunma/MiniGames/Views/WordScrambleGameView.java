package asu.gunma.MiniGames.Views;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
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
import java.util.List;
import java.lang.Object;

import asu.gunma.DatabaseInterface.DbInterface;
import asu.gunma.DbContainers.VocabWord;
import asu.gunma.MiniGames.Controllers.WordScrambleGameController;
import asu.gunma.speech.ActionResolver;
import asu.gunma.ui.util.GradeSystem;

// Use this class for the front-end of the word scramble mini-game
// You'll want to use the WordScrambleGameController class
public class WordScrambleGameView implements Screen
{
    //DbInterface dbCallback;
    private WordScrambleGameController controller;
    private GradeSystem gradeSystem;
    private String displayWord;

    private Game game;
    private Music gameMusic;
    public static float masterVolume = 5;
    public ActionResolver speechGDX;
    private Screen previousScreen;

    boolean isPaused = false;

    // UI variables
    private Stage stage;

    private TextButton buttonTutorial;

    private SpriteBatch batch;
    private Texture texture;
    private Texture background; //background of game
    private Texture character; //shows character on screen
    private Texture rectangle; //draw rectangle for word

    private BitmapFont font;
    private BitmapFont font2;

    FreeTypeFontGenerator generator;
    FreeTypeFontGenerator.FreeTypeFontParameter parameter;
    FreeTypeFontGenerator.FreeTypeFontParameter parameter2;
    public Preferences prefs;

    private TextButton speakButton;
    private TextButton skipButton;

    private int listCounter = 0;
    private GlyphLayout displayWordLayout;
    private int targetWidth = 400;

    public WordScrambleGameView(Game game, ActionResolver speechGDX, Music music, Screen previous, Preferences prefs, WordScrambleGameController controller)
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
    }

    // Override Screen class methods
    @Override
    public void show()
    {
        Gdx.gl.glClearColor(.8f, 1, 1, 1);
        stage = new Stage();
        batch = new SpriteBatch();
        //texture = new Texture("title_gunma.png");

        background = new Texture("BG.png"); //background of game
        rectangle = new Texture("rectangle.jpeg"); //big square to hold word at the top
        character = new Texture("title_gunma.png"); //show character

        Gdx.input.setInputProcessor(stage);

        displayWord = controller.getScrambledList().get(listCounter);
        parameter.characters = displayWord;
        parameter.size = 70;
        parameter.color = Color.BLACK;
        font = generator.generateFont(parameter);
        parameter2.size = 30;
        parameter2.color = Color.BLACK;
        font2 = generator.generateFont(parameter2);

        displayWordLayout = new GlyphLayout();
        displayWordLayout.setText(font, displayWord, Color.BLACK, targetWidth, Align.center, true);

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.pressedOffsetX = 1;
        textButtonStyle.pressedOffsetY = -1;
        textButtonStyle.font = font2;
        textButtonStyle.fontColor = Color.BLACK;

        buttonTutorial = new TextButton("Play", textButtonStyle);
        speakButton = new TextButton("Speak", textButtonStyle);
        skipButton = new TextButton("Skip", textButtonStyle);
        //speakButton.setPosition(100 , Gdx.graphics.getHeight() - 550);



        speakButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                try
                {
                    speechGDX.listenOnce();
                }
                catch (Exception e)
                {
                    System.out.println(e);
                }
            }
        });

        //stage.addActor(speakButton);
    }

    @Override
    public void render(float delta)
    {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        stage.act(delta);
        stage.draw();

        batch.begin();
        batch.draw(background, 0, 0, 1050, 600); //draw background
        speakButton.setPosition(100 , 550);
        batch.draw(character, 100, 50, 200, 200); //draw little gunma dude
        batch.draw(rectangle, 300, 425, 400, 100); //draw rectangle to hold word

        batch.draw(rectangle, 200, 300, 75, 75); //draw boxes below to hold word
        batch.draw(rectangle, 300, 300, 75, 75); //draw boxes below to hold word
        batch.draw(rectangle, 400, 300, 75, 75); //draw boxes below to hold word
        batch.draw(rectangle, 500, 300, 75, 75); //draw boxes below to hold word
        batch.draw(rectangle, 600, 300, 75, 75); //draw boxes below to hold word
        batch.draw(rectangle, 700, 300, 75, 75); //draw boxes below to hold word

        font.draw(batch, displayWordLayout, 300, 500); //moved word up to insert tiny boxes below

        //speakButton.setPosition(100 , Gdx.graphics.getHeight() - 550);
        skipButton.setPosition(400, Gdx.graphics.getHeight() - 550); //set skip word button


        String spokenWord = speechGDX.getWord();
        String cWords = controller.getActiveVocabList().get(listCounter).getCorrectWords();
        String[] correctWords = cWords.split("\\s*,\\s*");
        boolean correct = gradeSystem.grade(correctWords, spokenWord);

        // check if the word that was spoken matches the correct pronunciation of the word
        // if it does, then display the green circle image, increase the score, and proceed to the next scrambled word
        if (correct)
        {
            stage.act(delta);
            stage.draw();

            System.out.println("Correct!");
            System.out.println(correctWords);//<- this is to show the correct word as well
            batch.begin();

            //batch.draw(background, 300, 300, 400, 400); //draw rectangle
            font.draw(batch, displayWordLayout, 300, 100); //where to show word

            font.draw(batch, displayWordLayout, 300, -100); //location of correct word

            listCounter++;
            System.out.println(controller.increaseScore());
            displayWord = controller.getCurrentScrambledWord(listCounter);
        }
        // if it doesn't, then display the red X and proceed to the next scrambled word
        // scrambled words and the correct spelling association can be accessed through controller.getActiveVocabList() and controller.getScrambledWordList() using listCounter

        parameter.characters = displayWord;
        parameter.size = 70;
        parameter.color = Color.BLACK;
        font = generator.generateFont(parameter);
        displayWordLayout.setText(font, displayWord, Color.BLACK, targetWidth, Align.center, true);

        stage.addActor(speakButton); //check this!!!!
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
        //font.dispose();
        //batch.dispose();
        //stage.dispose();
    }

}
