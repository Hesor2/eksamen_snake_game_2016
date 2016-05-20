package dk.kea.class2016february.markus.gameengine.SnakeGame;

public class SnakeBody
{
    public static final float WIDTH = 32;
    public static final float HEIGHT = 32;
    float id;
    float x;
    float y;
    float angle;


    public SnakeBody(float id, float x, float y, float angle)
    {
        this.id = id;
        this.x = x;
        this.y = y;
        this.angle = angle;
    }
}
