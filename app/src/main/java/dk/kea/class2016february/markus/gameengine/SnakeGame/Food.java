package dk.kea.class2016february.markus.gameengine.SnakeGame;

public class Food
{
    float x;
    float y;
    int value;

    public static final float WIDTH = 15;
    public static final float HEIGHT = 15;

    public Food(float x, float y, int value)
    {
        this.x = x;
        this.y = y;
        this.value = value;
    }

}
