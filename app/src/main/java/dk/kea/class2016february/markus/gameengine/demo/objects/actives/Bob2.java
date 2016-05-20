package dk.kea.class2016february.markus.gameengine.demo.objects.actives;

import dk.kea.class2016february.markus.gameengine.demo.ActiveObject;
import dk.kea.class2016february.markus.gameengine.demo.ObjectType;
import dk.kea.class2016february.markus.gameengine.demo.PhysScreen;

public class Bob2 extends ActiveObject
{
    public Bob2(int x, int y, PhysScreen physScreen)
    {
//        this.id = id;
        this.x = x;
        this.y = y;
        this.physScreen = physScreen;

        this.xSize = 96;
        this.ySize = 118;

        this.ySpeed = 2;
        this.type = ObjectType.movingPlatform;

        this.imagePointer = 0;
        this.images = new String[]
                {
                        "bobRight.png"
                };
    }

    @Override
    public void doAction()
    {
        if(y == 0)
        {
            ySpeed = 2;
        }
        else if(y + ySpeed < 0)
        {
            while(y + ySpeed != 0)
            {
                ySpeed--;
            }
        }
        else if (y + ySize == physScreen.game.getOffscreenHeight())
        {
            ySpeed = -2;
        }
        else if(y+ ySize + ySpeed > physScreen.game.getOffscreenHeight())
        {
            while(y + ySize + ySpeed != physScreen.game.getOffscreenHeight())
            {
                ySpeed++;
            }
        }

        move();

    }
}
