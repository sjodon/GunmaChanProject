package asu.gunma.MiniGames.Models;

import com.badlogic.gdx.math.Vector2;
import java.util.ArrayList;
import asu.gunma.DbContainers.VocabWord;

public class AsteroidGameModel
{
    // private fields
    private int level; // current level that the player is on (levels 1 - 5)
    private int score;
    private int numLives;
    private ArrayList<VocabWord> activeVocabList; // list of words that may be included in the mini-game
    private ArrayList<Asteroid> asteroidList;
    private AsteroidPlayer player;

    // constants
    public static final int MIN_LEVEL = 1;
    public static final int MAX_LEVEL = 5;
    public static final int DEFAULT_LEVEL = 1;
    public static final int MIN_SCORE = 0;
    public static final int DEFAULT_SCORE = 0;
    public static final int MAX_LIVES = 5;
    public static final int MIN_LIVES = 1;
    public static final int DEFAULT_NUM_LIVES = 5;
    public static final int MIN_NUM_ASTEROIDS = 0;
    public static final float DEFAULT_VELOCITY = 1f;
    public static final float DEFAULT_DIRECTION = 0f;
    public static final Vector2 DEFAULT_ASTEROID_POS = new Vector2(0f, 0f);
    public static final Vector2 DEFAULT_PLAYER_POS = new Vector2(0f, 0f);

    // constructor
    public AsteroidGameModel(int level, int score, int numLives, ArrayList<VocabWord> activeVocabList)
    {
        setLevel(level);
        setScore(score);
        setNumLives(numLives);
        setActiveVocabList(activeVocabList);
        setAsteroidList(null);
        setPlayer(null);
    }

    // get methods
    public int getScore()
    {
        return score;
    }

    public int getNumLives()
    {
        return numLives;
    }

    public int getLevel()
    {
        return level;
    }

    public ArrayList<VocabWord> getActiveVocabList()
    {
        return activeVocabList;
    }

    public ArrayList<Asteroid> getAsteroidList()
    {
        return asteroidList;
    }

    public AsteroidPlayer getPlayer()
    {
        return player;
    }

    // set methods
    public void setScore(int score)
    {
        if (score >= MIN_SCORE)
            this.score = score;
        else
            this.score = DEFAULT_SCORE;
    }

    public void setNumLives(int numLives)
    {
        if (numLives >= MIN_LIVES && numLives <= MAX_LIVES)
            this.numLives = numLives;
        else
            this.numLives = DEFAULT_NUM_LIVES;
    }

    public void setLevel(int level)
    {
        if (level >= MIN_LEVEL && level <= MAX_LEVEL)
            this.level = level;
        else
            this.level = DEFAULT_LEVEL;
    }

    public void setActiveVocabList(ArrayList<VocabWord> activeVocabList)
    {
        if (activeVocabList != null)
            this.activeVocabList = activeVocabList;
        else
            this.activeVocabList = new ArrayList<VocabWord>();
    }

    public void setAsteroidList(ArrayList<Asteroid> asteroidList)
    {
        if (asteroidList == null)
        {
            asteroidList = new ArrayList<Asteroid>();

            for (int i = 0; i < level; i++)
            {
                Asteroid asteroid = new Asteroid(null, DEFAULT_VELOCITY, DEFAULT_DIRECTION, DEFAULT_ASTEROID_POS);
                asteroidList.add(asteroid);
            }

            return;
        }

        int size = asteroidList.size();

        // the number of asteroids sent towards the player must be equal to the level number
        if (size > level)
        {
            for (int i = size - 1; i >= level; i--)
            {
                asteroidList.remove(i);
            }
        }
        else if (size < level)
        {
            for (int i = size - 1; i < level; i++)
            {
                Asteroid asteroid = new Asteroid(null, DEFAULT_VELOCITY, DEFAULT_DIRECTION, DEFAULT_ASTEROID_POS);
                asteroidList.add(asteroid);
            }
        }
        else
            this.asteroidList = asteroidList;
    }

    public void setPlayer(AsteroidPlayer player)
    {
        if (player != null)
            this.player = player;
        else
            this.player = new AsteroidPlayer(DEFAULT_PLAYER_POS);
    }
}