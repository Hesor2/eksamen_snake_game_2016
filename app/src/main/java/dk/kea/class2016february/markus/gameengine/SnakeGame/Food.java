package dk.kea.class2016february.markus.gameengine.SnakeGame;

public class Food
{
    int id;
    float x;
    float y;
    int value;

    public static final float WIDTH = 15;
    public static final float HEIGHT = 15;

    public Food(int id, float x, float y)
    {
        this.id = id;
        this.x = x;
        this.y = y;
        this.value = 1;
    }

}
