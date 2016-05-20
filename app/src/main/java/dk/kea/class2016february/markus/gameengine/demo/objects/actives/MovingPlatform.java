package dk.kea.class2016february.markus.gameengine.demo.objects.actives;

import dk.kea.class2016february.markus.gameengine.demo.ActiveObject;
import dk.kea.class2016february.markus.gameengine.demo.Axis;
import dk.kea.class2016february.markus.gameengine.demo.ObjectType;
import dk.kea.class2016february.markus.gameengine.demo.PhysScreen;

public class MovingPlatform extends ActiveObject
{
    private int home;
    private int destination;
    private Axis axis;

    private boolean increasing = true;

    public MovingPlatform(int x, int y, int z, Axis axis, int destination, PhysScreen physScreen)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.axis = axis;

        if(axis == Axis.x)this.home = x;
        else if(axis == Axis.y) this.home = y;
        this.destination = destination;

        if(home >= destination)
        {
            throw new IllegalArgumentException("MovingPlatforms must have a higher destination value than home value");
        }

        this.physScreen = physScreen;

        this.xSize = 64;
        this.ySize = 32;

        this.type = ObjectType.movingPlatform;

        this.imagePointer = 0;
        this.images = new String[]
                {
                        "64x32.png"
                };
    }

    @Override
    public void doAction()
    {
        if (axis == Axis.x)
        {
            if(increasing)
            {
                if(x + xSize == destination)
                {
                    increasing = false;
                }
                else if(x + xSize + xSpeed > destination)
                {
                    xSpeed = destination - x - xSize;
                }
                else
                {
                    xSpeed = 1;
                }
            }
            else
            {
                if(x == home)
                {
                    increasing = true;
                }
                else if(x + xSpeed < home)
                {
                    xSpeed = home - x;
                }
                else
                {
                    xSpeed = -1;
                }
            }
        }
        else if(axis == Axis.y)
        {
            if(increasing)
            {
                if(y+ySize == destination)
                {
                    increasing = false;
                }
                else if(y + ySize + ySpeed > destination)
                {
                    ySpeed = destination - y - ySize;
                }
                else
                {
                    ySpeed = 1;
                }
            }
            else
            {
                if(y == home)
                {
                    increasing = true;
                }
                else if(y + ySpeed < home)
                {
                    ySpeed = home - y;
                }
                else
                {
                    ySpeed = -1;
                }
            }
        }

        move();

    }
}
