package dk.kea.class2016february.markus.gameengine.SnakeGame;

public class SnakeBody
{
    public static final float WIDTH = 32;
    public static final float HEIGHT = 32;
    int id;
    float x;
    float y;

    public SnakeBody(int id, float x, float y)
    {
        this.id = id;
        this.x = x;
        this.y = y;
    }
}
