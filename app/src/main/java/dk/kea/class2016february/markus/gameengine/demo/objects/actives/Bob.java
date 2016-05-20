package dk.kea.class2016february.markus.gameengine.demo.objects.actives;

import android.util.Log;

import dk.kea.class2016february.markus.gameengine.demo.ActiveObject;
import dk.kea.class2016february.markus.gameengine.demo.Collision;
import dk.kea.class2016february.markus.gameengine.demo.ObjectType;
import dk.kea.class2016february.markus.gameengine.demo.PhysScreen;

public class Bob extends ActiveObject
{
    public Bob(int x, int y, PhysScreen physScreen)
    {
//        this.id = id;
        this.x = x;
        this.y = y;
        this.physScreen = physScreen;

        this.imagePointer = 0;
        this.images = new String[]
                {
                        "bobRight.png"
                };

        this.type = ObjectType.player;
        this.xSize = 96;
        this.ySize = 118;
    }

    @Override
    public void doAction()
    {
        if(physScreen.game.isTouchDown(0))
        {
            int touchX = physScreen.game.getTouchX(0);
            int touchY = physScreen.game.getTouchY(0);
            if(touchX > x && xSpeed < 6)
            {
                xSpeed++;
            }
            else if(touchX < x && xSpeed > -6)
            {
                xSpeed--;
            }
            if(touchY > y && ySpeed < 6)
            {
                ySpeed++;
            }
            else if(touchY < y && ySpeed > -6)
            {
                ySpeed--;
            }
        }
        else
        {
            if (xSpeed > 0)
            {
                xSpeed--;
            }
            else if(xSpeed < 0)
            {
                xSpeed++;
            }
            if(ySpeed > 0)
            {
                ySpeed--;
            }
            else if(ySpeed < 0)
            {
                ySpeed++;
            }
        }
        checkCollision();
        if(!collisions.isEmpty())
        {
            for (Collision collision : collisions)
            {
                Log.d("collision", collision.direction + " " + collision.objectType);
            }
        }
        move();
        clearCollision();
    }
}
