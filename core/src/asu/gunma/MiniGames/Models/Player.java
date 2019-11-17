package asu.gunma.MiniGames.Models;

import com.badlogic.gdx.math.Vector2;

public class Player
{
    Vector2 position;

    public Player(Vector2 position)
    {

    }

    // get methods
    public Vector2 getPosition()
    {
        return position;
    }

    // set methods
    public void setPosition(Vector2 position)
    {
        this.position = position;
    }
}
