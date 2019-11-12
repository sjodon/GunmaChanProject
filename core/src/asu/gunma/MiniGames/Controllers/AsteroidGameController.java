package asu.gunma.MiniGames.Controllers;

import java.util.ArrayList;

import asu.gunma.DbContainers.VocabWord;
import asu.gunma.MiniGames.Models.Asteroid;
import asu.gunma.MiniGames.Models.AsteroidGameModel;

public class AsteroidGameController
{
    private AsteroidGameModel model;

    public AsteroidGameController(AsteroidGameModel model)
    {
        if (model != null)
            this.model = model;
        else
            this.model = new AsteroidGameModel(1, 0, null);
    }

    // get methods
    public AsteroidGameModel getModel()
    {
        return model;
    }

    public int getLevel()
    {
        return model.getLevel();
    }

    public int getScore()
    {
        return model.getScore();
    }

    public ArrayList<VocabWord> getActiveVocabList()
    {
        return model.getActiveVocabList();
    }

    public ArrayList<Asteroid> getAsteroidList()
    {
        return model.getAsteroidList();
    }

    // set methods
    public void setModel(AsteroidGameModel model)
    {
        this.model = model;
    }

    public void setLevel(int level)
    {
        model.setLevel(level);
    }

    public void setScore(int score)
    {
        model.setScore(score);
    }

    public void setActiveVocabList(ArrayList<VocabWord> activeVocabList)
    {
        model.setActiveVocabList(activeVocabList);
    }

    public void setAsteroidList(ArrayList<Asteroid> asteroidList)
    {
        model.setAsteroidList(asteroidList);
    }

    // other methods
    public void transformAsteroidPosition(float x, float y, int index)
    {
        model.getAsteroidList().get(index).transformPosition(x, y);
    }
}
