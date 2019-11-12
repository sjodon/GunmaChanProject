package asu.gunma.MiniGames.Models;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

import asu.gunma.DbContainers.VocabWord;

public class AsteroidGameModel
{
    private int level; // current level that the player is on (levels 1 - 5)
    private int score = 0;
    private ArrayList<VocabWord> activeVocabList; // list of words that may be included in the mini-game
    private ArrayList<Asteroid> asteroidList;

    public AsteroidGameModel(int level, int score, ArrayList<VocabWord> activeVocabList)
    {
        setLevel(level);
        setScore(score);
        setActiveVocabList(activeVocabList);
        setAsteroidList(null);
    }

    // get methods
    public int getScore()
    {
        return score;
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

    // set methods
    public void setScore(int score)
    {
        if (score >= 0)
            this.score = score;
        else
            this.score = 0;
    }

    public void setLevel(int level)
    {
        if (level >= 1 && level <= 5)
            this.level = level;
        else
            this.level = 1;
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
                Asteroid asteroid = new Asteroid(null, 1, 0, new Vector2(0, 0));
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
        else if (asteroidList.size() < level)
        {
            for (int i = size - 1; i < level; i++)
            {
                Asteroid asteroid = new Asteroid(null, 1, 0, new Vector2(0, 0));
                asteroidList.add(asteroid);
            }
        }
        else
            this.asteroidList = asteroidList;
    }
}