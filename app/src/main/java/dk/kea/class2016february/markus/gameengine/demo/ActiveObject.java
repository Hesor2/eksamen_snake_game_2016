package dk.kea.class2016february.markus.gameengine.demo;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public abstract class ActiveObject extends PhysObject
{
    public int xSpeed;
    public int ySpeed;


    public List<Collision> collisions = new ArrayList<>();

    public void destroy()
    {
        physScreen.activeObjectList.remove(this);
    }

//    public ActiveObject(ActiveObject object)
//    {
//        this.x = object.x;
//        this.y = object.y;
//        this.xSize = object.xSize;
//        this.ySize = object.ySize;
//        this.xSpeed = object.xSpeed;
//        this.ySpeed = object.ySpeed;
//
//    }

    public void move()
    {
        x += xSpeed;
        y += ySpeed;
    }

    public void checkCollision()
    {
        int leftEdge = x + xSpeed;
        int rightEdge = x + xSpeed + xSize;
        int upperEdge = y + ySpeed;
        int lowerEdge = y + ySpeed + ySize;


        int currentRightEdge = x + xSize;
        int currentLowerEdge = y + ySize;

        for (StaticObject staticObject : physScreen.staticObjectList)
        {
            if(z == staticObject.z)
            {
                int otherRightEdge = staticObject.x + staticObject.xSize;
                int otherLeftEdge = staticObject.x;
                int otherUpperEdge = staticObject.y;
                int otherLowerEdge = staticObject.y + staticObject.ySize;

                //vertical scenario
                if ((x <= otherRightEdge && x >= otherLeftEdge) || (currentRightEdge <= otherRightEdge && currentRightEdge >= otherLeftEdge) || (x <= otherLeftEdge && currentRightEdge >= otherRightEdge))
                {
                    if ((leftEdge <= otherRightEdge && leftEdge >= otherLeftEdge) || (rightEdge <= otherRightEdge && rightEdge >= otherLeftEdge) || (leftEdge <= otherLeftEdge && rightEdge >= otherRightEdge))
                    {
                        if (upperEdge <= otherLowerEdge && upperEdge >= otherUpperEdge)
                        {
                            collisions.add(new Collision(Direction.up, staticObject.type, staticObject.id, otherLowerEdge));
                        }
                        else if (lowerEdge <= otherLowerEdge && lowerEdge >= otherUpperEdge)
                        {
                            collisions.add(new Collision(Direction.down, staticObject.type, staticObject.id, otherUpperEdge));
                        }
                    }
                } //horizontal scenario
                else if ((y <= otherLowerEdge && y >= otherUpperEdge) || (currentLowerEdge <= otherLowerEdge && currentLowerEdge >= otherUpperEdge) || (y <= otherUpperEdge && currentLowerEdge >= otherLowerEdge))
                {
                    if ((upperEdge <= otherLowerEdge && upperEdge >= otherUpperEdge) || (lowerEdge <= otherLowerEdge && lowerEdge >= otherUpperEdge) || (upperEdge <= otherUpperEdge && lowerEdge >= otherLowerEdge))
                    {
                        if (leftEdge <= otherRightEdge && leftEdge >= otherLeftEdge)
                        {
                            collisions.add(new Collision(Direction.left, staticObject.type, staticObject.id, otherRightEdge));
                        }
                        else if (rightEdge <= otherRightEdge && rightEdge >= otherLeftEdge)
                        {
                            collisions.add(new Collision(Direction.right, staticObject.type, staticObject.id, otherLeftEdge));
                        }
                    }
                }
            }

        }

        for (ActiveObject activeObject : physScreen.activeObjectList)
        {
            if (activeObject.id != id)
            {
                if(z == activeObject.z)
                {
                    int otherRightEdge = activeObject.x + activeObject.xSpeed + activeObject.xSize;
                    int otherLeftEdge = activeObject.x + activeObject.xSpeed;
                    int otherUpperEdge = activeObject.y + activeObject.ySpeed;
                    int otherLowerEdge = activeObject.y + activeObject.ySpeed + activeObject.ySize;

                    int otherCurrentRightEdge = activeObject.x + activeObject.xSize;
                    int otherCurrentLowerEdge = activeObject.y + activeObject.ySpeed + activeObject.ySize;

                    //vertical scenario
                    if ((x <= otherCurrentRightEdge && x >= activeObject.x) || (currentRightEdge <= otherCurrentRightEdge && currentRightEdge >= activeObject.x) || (x <= activeObject.x && currentRightEdge >= otherCurrentRightEdge))
                    {
                        if ((leftEdge <= otherRightEdge && leftEdge >= otherLeftEdge) || (rightEdge <= otherRightEdge && rightEdge >= otherLeftEdge) || (leftEdge <= otherLeftEdge && rightEdge >= otherRightEdge))
                        {
                            if (upperEdge <= otherLowerEdge && upperEdge >= otherUpperEdge)
                            {
                                collisions.add(new Collision(Direction.up, activeObject.type, activeObject.xSpeed, activeObject.ySpeed, activeObject.id, otherLowerEdge));
                            }
                            else if (lowerEdge <= otherLowerEdge && lowerEdge >= otherUpperEdge)
                            {
                                collisions.add(new Collision(Direction.down, activeObject.type, activeObject.xSpeed, activeObject.ySpeed, activeObject.id, otherUpperEdge));
                            }
                        }
                    } //horizontal scenario
                    else if ((y <= otherCurrentLowerEdge && y >= activeObject.y) || (currentLowerEdge <= otherCurrentLowerEdge && currentLowerEdge >= activeObject.y) || (y <= activeObject.y && currentLowerEdge >= otherCurrentLowerEdge))
                    {
                        if ((upperEdge <= otherLowerEdge && upperEdge >= otherUpperEdge) || (lowerEdge <= otherLowerEdge && lowerEdge >= otherUpperEdge) || (upperEdge <= otherUpperEdge && lowerEdge >= otherLowerEdge))
                        {
                            if (leftEdge <= otherRightEdge && leftEdge >= otherLeftEdge)
                            {
                                collisions.add(new Collision(Direction.left, activeObject.type, activeObject.xSpeed, activeObject.ySpeed, activeObject.id, otherRightEdge));
                            }
                            else if (rightEdge <= otherRightEdge && rightEdge >= otherLeftEdge)
                            {
                                collisions.add(new Collision(Direction.right, activeObject.type, activeObject.xSpeed, activeObject.ySpeed, activeObject.id, otherLeftEdge));
                            }
                        }
                    }
                }
            }
        }

    }

    public void clearCollision()
    {
        collisions.clear();
    }

    public void collide(Collision collision)
    {
        switch (collision.direction)
        {
            case up:
                ySpeed = collision.location - y + 1;
                break;
            case down:
                ySpeed = collision.location - y - ySize - 1;
                break;
            case left:
                xSpeed = collision.location - x + 1;
                break;
            case right:
                xSpeed = collision.location - x - xSize - 1;
                break;
        }
    }

    public abstract void doAction();
}
