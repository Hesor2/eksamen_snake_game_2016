package dk.kea.class2016february.markus.gameengine.demo.objects.actives;

import android.util.Log;

import dk.kea.class2016february.markus.gameengine.demo.ActiveObject;
import dk.kea.class2016february.markus.gameengine.demo.Collision;
import dk.kea.class2016february.markus.gameengine.demo.DemoScreen2;
import dk.kea.class2016february.markus.gameengine.demo.Direction;
import dk.kea.class2016february.markus.gameengine.demo.ObjectType;
import dk.kea.class2016february.markus.gameengine.demo.PhysScreen;

public class Platformer extends ActiveObject
{
    private boolean grounded = false;

    private int airTime = 0;
    private final int jumpSpeed = 10;
    private final int fallSpeed = 6;
    private final int airLimit = 14;
    private final int neutralAirLimit = 3;

    private final int deadZone = 10;

    private int animation = -1;
    private int animationPointer = 0;
    private final int animationLimit = 20;



    private boolean upCollision;
    private boolean downCollision;
    private boolean leftCollision;
    private boolean rightCollision;

    private int mapBottom;

    public Platformer(int x, int y, int z, PhysScreen physScreen)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.physScreen = physScreen;
        this.mapBottom = physScreen.getMapBoundary().bottom;

        this.imagePointer = 1;

        this.images = new String[]
                {
                        "SansStandLeft.png", "SansStandRight.png", "SansWalkLeft.png", "SansWalkRight.png", "SansJumpLeft.png", "SansJumpRight.png"
                };

        this.type = ObjectType.player;
        this.xSize = 32;
        this.ySize = 64;
    }

    @Override
    public void doAction()
    {
        upCollision = false;
        downCollision = false;
        leftCollision = false;
        rightCollision = false;

        boolean dead = false;

        boolean interact = false;

        if (physScreen.game.isTouchDown(0))
        {
            if (!physScreen.isInUIArea(0))
            {
                int touchX = physScreen.getTouchX(0) - xSize / 2;

                if (touchX > x + deadZone)
                {
                    if (grounded)
                    {
                        animateWalk(3);
                    }
                    else
                    {
                        animation = -1;
                        imagePointer = 5;
                    }
                    if (xSpeed < 2)
                    {
                        if (x + xSpeed > touchX)
                        {
                            xSpeed = touchX - x;
                        }
                        else
                        {
                            xSpeed++;
                        }
                    }
                }
                else if (touchX < x - deadZone)
                {
                    if (grounded)
                    {
                        animateWalk(2);
                    }
                    else imagePointer = 4;

                    if (xSpeed > -2)
                    {
                        if (x + xSpeed < touchX)
                        {
                            xSpeed = touchX - x;
                        }
                        else
                        {
                            xSpeed--;
                        }
                    }
                }
                else
                {
                    if (physScreen.getPointerReleased(0)) interact = true;
                }

            }
        }
        else
        {
            if (xSpeed > 0) xSpeed--;
            else if (xSpeed < 0) xSpeed++;
            if (animation == 2 || imagePointer == 4) imagePointer = 0;
            else if (animation == 3 || imagePointer == 5) imagePointer = 1;
            animation = -1;
        }
        if (physScreen.game.isTouchDown(1))
        {
            if (grounded)
            {
                if (physScreen.getPointerReleased(1))
                {
                    ySpeed = -jumpSpeed;
                    grounded = false;
                    airTime = airLimit;
//                    pointerReleased[1] = false;
                }
            }
        }
        else
        {
            if (airTime > neutralAirLimit) airTime = neutralAirLimit;
        }

        if (airTime == 0)
        {
            if (ySpeed < fallSpeed) ySpeed++;
        }
        else if (airTime > 0) airTime--;
        else Log.d("Airtime < 0", "this shouldn't happen");

        checkCollision();
        if (!collisions.isEmpty())
        {
            for (Collision collision : collisions)
            {
                switch (collision.objectType)
                {
                    case block:
                        setCollision(collision);
//                        Log.d("Collision", "" + collision.direction);
                        if (collision.direction == Direction.down)
                        {
                            grounded = true;
                            airTime = 0;
                        }
                        else if (collision.direction == Direction.up) airTime = 0;
                        collide(collision);
                        break;
                    case movingPlatform:
                        setCollision(collision);
                        switch (collision.direction)
                        {
                            case up:
                                collide(collision);
                                airTime = 0;
                                break;
                            case down:
                                grounded = true;
                                airTime = 0;
                                collide(collision);
                                if (!physScreen.game.isTouchDown(0))
                                {
                                    xSpeed = collision.objectXSpeed;
                                }
                                break;
                            case left:
                                if (x <= collision.location)
                                {
                                    xSpeed = collision.objectXSpeed;
                                }
                                else
                                {
                                    collide(collision);
                                }
                                break;
                            case right:
                                if (x + xSize >= collision.location)
                                {
                                    xSpeed = collision.objectXSpeed;
                                }
                                else
                                {
                                    collide(collision);
                                }

                                break;
                        }
                        break;
                    case doorDown:
                        if(interact) z--;
                        break;
                    case doorUp:
                        if(interact) z++;
                        break;
                }
            }
        }
        if ((upCollision & downCollision) | (leftCollision & rightCollision) || y >= mapBottom)
        {
            dead = true;
        }

        if (dead)
        {
//            x = 0;
//            y = 290;
//            xSpeed = 0;
//            ySpeed = 0;
//            Log.d("Player", "dead");
            if(physScreen instanceof DemoScreen2)
            {
                physScreen.game.setScreen(new DemoScreen2(physScreen.game) );
                return;
            }
        }
        else move();
        clearCollision();
    }

    private void setCollision(Collision collision)
    {
        switch (collision.direction)
        {
            case up:
                upCollision = true;
                break;
            case down:
                downCollision = true;
                break;
            case left:
                leftCollision = true;
                break;
            case right:
                rightCollision = true;
                break;
        }
    }

    private void animateWalk(int animation)
    {
        int alt = 0;
        if (animation == 3) alt = 1;

        if (this.animation != animation)
        {
            this.animation = animation;
            imagePointer = animation;
            animationPointer = 0;
        }
        else
        {
            animationPointer += 1;
            if (animationPointer >= animationLimit)
            {
                if (imagePointer == animation) imagePointer = alt;
                else imagePointer = animation;
                animationPointer = 0;
            }
        }
    }
}
