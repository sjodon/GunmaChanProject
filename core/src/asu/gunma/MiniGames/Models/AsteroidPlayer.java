package asu.gunma.MiniGames.Models;

import com.badlogic.gdx.math.Vector2;

public class AsteroidPlayer
{
    private Vector2 position;

    public AsteroidPlayer(Vector2 position)
    {
        setPosition(position);
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
