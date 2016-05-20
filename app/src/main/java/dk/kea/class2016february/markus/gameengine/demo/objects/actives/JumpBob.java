package dk.kea.class2016february.markus.gameengine.demo.objects.actives;

import android.util.Log;

import dk.kea.class2016february.markus.gameengine.demo.ActiveObject;
import dk.kea.class2016february.markus.gameengine.demo.Collision;
import dk.kea.class2016february.markus.gameengine.demo.Direction;
import dk.kea.class2016february.markus.gameengine.demo.ObjectType;
import dk.kea.class2016february.markus.gameengine.demo.PhysScreen;

public class JumpBob extends ActiveObject
{
    private boolean grounded = false;
    private int airTime;

    private int jumpSpeed = 14;
    private int airLimit = 10;
    private int neutralAirLimit = 2;

    private boolean upCollision;
    private boolean downCollision;
    private boolean leftCollision;
    private boolean rightCollision;

    public JumpBob(int x, int y, PhysScreen physScreen)
    {
//        this.id = id;
        this.x = x;
        this.y = y;
        this.physScreen = physScreen;

        this.imagePointer = 0;

        this.images = new String[]
        {
                "bobRight.png", "bobLeft.png"
        };

        this.type = ObjectType.player;
        this.xSize = 96;
        this.ySize = 118;
    }

    @Override
    public void doAction()
    {
        upCollision = false;
        downCollision = false;
        leftCollision = false;
        rightCollision = false;
        if (physScreen.game.isTouchDown(0))
        {
            int touchX = physScreen.game.getTouchX(0) - xSize / 2;

            if (touchX > x && xSpeed < 2)
            {
                imagePointer = 0;
                if (x + xSpeed > touchX)
                {
                    xSpeed = touchX - x;
                }
                else
                {
                    xSpeed++;
                }
            }
            else if (touchX < x && xSpeed > -2)
            {
                imagePointer = 1;
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
            if (xSpeed > 0) xSpeed--;
            else if (xSpeed < 0) xSpeed++;
        }

        if (physScreen.game.isTouchDown(1))
        {
            if (grounded)
            {
                ySpeed = -jumpSpeed;
                grounded = false;
                airTime = 0;
            }
            else
            {
                airTime++;
                if (airTime > airLimit)
                {
                    if (ySpeed < 6) ySpeed++;
                }
                if (y + ySize == physScreen.game.getOffscreenHeight())
                {
                    ySpeed = 0;
                    grounded = true;
                }
                else if (y + ySize + ySpeed > physScreen.game.getOffscreenHeight())
                {
                    ySpeed = physScreen.game.getOffscreenHeight() - y - ySize;
                }
            }
        }
        else if (!grounded)
        {
            airTime++;
            if (airTime > neutralAirLimit)
            {
                if (ySpeed < 6) ySpeed++;
            }
            if (y + ySize == physScreen.game.getOffscreenHeight())
            {
                ySpeed = 0;
                grounded = true;
            }
            else if (y + ySize + ySpeed > physScreen.game.getOffscreenHeight())
            {
                ySpeed = physScreen.game.getOffscreenHeight() - y - ySize;
            }
        }

        checkCollision();
        if (!collisions.isEmpty())
        {
            for (Collision collision : collisions)
            {
                setCollision(collision);
//                if(collision.objectId == 7)
//                {
//                    Log.d("Collision on platform", "" + collision.direction);
//                }
//                Log.d("collision", collision.direction + " " + collision.objectType);
                switch (collision.objectType)
                {
                    case block:
                        if(collision.direction == Direction.down) grounded = true;
                        collide(collision);
                        break;
                    case movingPlatform:
                        switch (collision.direction)
                        {
                            case up:
                                if (y >= collision.location)
                                {
                                    ySpeed = collision.objectYSpeed;
                                    airTime = airLimit + 1;
                                }
                                else
                                {
                                    collide(collision);
                                    airTime = airLimit + 1;
                                }
                                break;
                            case down:
                                grounded = true;
                                collide(collision);
                                xSpeed += collision.objectXSpeed;
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
//                        if (collision.direction == Direction.down)
//                        {
//                            grounded = true;
//
//                            collide(collision);
//
//                            xSpeed += collision.objectXSpeed;
//                        }
//                        else if (collision.direction == Direction.up)
//                        {
//                            if (y >= collision.location)
//                            {
//                                ySpeed = collision.objectYSpeed;
//                                airTime = airLimit + 1;
//                            }
//                            else
//                            {
//                                collide(collision);
//                                airTime = airLimit + 1;
//                            }
//                        }
//                        else if (collision.direction == Direction.right)
//                        {
//                            if (x + xSize >= collision.location)
//                            {
//                                xSpeed = collision.objectXSpeed;
//                            }
//                            else
//                            {
//                                collide(collision);
//                            }
//                        }
//                        else if (collision.direction == Direction.left)
//                        {
//                            if (x <= collision.location)
//                            {
//                                xSpeed = collision.objectXSpeed;
//                            }
//                            else
//                            {
//                                collide(collision);
//                            }
//                        }
                        break;
                }
            }
        }
        else
        {
            grounded = false;
        }
        if((upCollision & downCollision) | (leftCollision & rightCollision))
        {
            x = 0;
            y = 0;
            xSpeed = 0;
            ySpeed = 0;
            Log.d("PLayer", "Lost due to getting crushed");
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
}
