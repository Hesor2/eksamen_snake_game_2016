package dk.kea.class2016february.markus.gameengine.SnakeGame;

import android.graphics.Rect;

public class Camera
{
    public static final float WIDTH = 320;
    public static final float HEIGHT = 480;

    float x;
    float y;

    private Snake focus;
    private Rect boundary;

    private static final int HALF_WIDTH = 160;
    private static final int HALF_HEIGHT = 240;

    public Camera(Snake focus, World world)
    {

        this.focus = focus;



        boundary = new Rect((int)world.minX, (int)world.minY, (int)world.maxX, (int)world.maxY);
    }


    public void update()
    {
        float targetX = focus.x + (Snake.WIDTH/2);
        float targetY = focus.y + (Snake.HEIGHT/2);


        if (targetX - Camera.HALF_WIDTH < boundary.left) x = boundary.left;
        else if(targetX + Camera.HALF_WIDTH > boundary.right) x = boundary.right - Camera.WIDTH;
        else x = targetX - Camera.HALF_WIDTH;

        if (targetY - Camera.HALF_HEIGHT < boundary.top) y = boundary.top;
        else if(targetY + Camera.HALF_HEIGHT > boundary.bottom) y = boundary.bottom - Camera.HEIGHT;
        else y = targetY - Camera.HALF_HEIGHT;

    }

    public Rect getBoundary()
    {
        Rect cameraBoundary = new Rect((int)x, (int)y, (int)(x+WIDTH), (int)(y+Camera.HEIGHT));
        return cameraBoundary;
    }
}
