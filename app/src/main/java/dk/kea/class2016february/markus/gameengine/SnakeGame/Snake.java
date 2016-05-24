package dk.kea.class2016february.markus.gameengine.SnakeGame;


import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import dk.kea.class2016february.markus.gameengine.BreakoutGame.Ball;


public class Snake
{
    public static final float WIDTH = 32;
    public static final float HEIGHT = 32;
    private float totalVelocity;
    float x;
    float y;
    private float velocityX;
    private float velocityY;
    float angle;

    float turnSpeed = 200;

    private float segmentTimer;
    private static float TIMERLIMIT = 0.13f;

//    private int bodyCount;
    List<SnakeBody> bodySegments;

    private int lastBodySegment;
    private boolean newBodySegment;
    private int newSegmentCount;
    private World world;

    public Snake(float x, float y, float velocity, World world)
    {
        this.x = x;
        this.y = y;
        this.world = world;

        this.segmentTimer = 0;
        this.angle = 0;
        lastBodySegment = 0;
        totalVelocity = velocity;

        lastBodySegment = 0;
        newBodySegment = false;
        newSegmentCount = 0;

        bodySegments = new ArrayList<>();
        bodySegments.add(new SnakeBody(bodySegments.size(), x, y, angle));
    }

    public void update(float deltaTime, float input)
    {
        collideFood();
        segmentTimer += 1*deltaTime;
//        Log.d("SegmenTimer", "" + segmentTimer);
        if (segmentTimer>=Snake.TIMERLIMIT)
        {
            int size = bodySegments.size();
            segmentTimer = 0;
            if (newBodySegment)
            {
                bodySegments.add(new SnakeBody(size, x, y, angle));
                newSegmentCount--;
                if (newSegmentCount == 0) newBodySegment = false;
            }
            else
            {
                SnakeBody body = bodySegments.get(lastBodySegment);
                body.x = x;
                body.y = y;
                body.angle = angle;
                lastBodySegment++;
                if (size < lastBodySegment + 1) lastBodySegment -= size;
            }
        }
        angle += turnSpeed * input * deltaTime;

        if (angle < 0) angle += 360;
        else if(angle > 360) angle -=360;

        velocityX = totalVelocity * (float) Math.cos(Math.toRadians(angle));
        velocityY = totalVelocity * (float)Math.sin(Math.toRadians(angle));

        move(deltaTime);

        collideBorder();
    }

    private void collideFood()
    {
        float rightEdge = this.x + Snake.WIDTH;
        float lowerEdge = this.y + Snake.HEIGHT;

        int size = world.food.size();
        if (size != 0)
        {
            for (int i = size-1; i >= 0; i--)
            {
                Food food = world.food.get(i);
                float otherRightEdge = food.x + Food.WIDTH;
                float otherLeftEdge = food.x;
                if ((x <= otherRightEdge && x >= otherLeftEdge) || (rightEdge <= otherRightEdge && rightEdge >= otherLeftEdge) || (x <= otherLeftEdge && rightEdge >= otherRightEdge))
                {
                    float otherUpperEdge = food.y;
                    float otherLowerEdge = food.y + Food.HEIGHT;
                    if ((y <= otherLowerEdge && y >= otherUpperEdge)||(lowerEdge <= otherLowerEdge && lowerEdge >= otherUpperEdge))
                    {
                        world.score += food.value*100;
                        newSegmentCount += food.value;
                        newBodySegment = true;

                        world.food.remove(i);
                    }
                }
            }
        }
    }

    private void collideBorder()
    {
        if(x < world.minX || x + Snake.WIDTH > world.maxX || y < world.minY || y + Snake.HEIGHT > world.maxY)
        {
            die();
        }
    }

    private void move(float deltaTime)
    {
//        Log.d("Velocity", "" + (xSpeed+ySpeed));
        x += velocityX * deltaTime;
        y += velocityY * deltaTime;

    }

    private void die()
    {
        world.gameOver = true;

        int size = bodySegments.size();
        for (int i = size - 1; i >= 0; i--)
        {
            SnakeBody segment = bodySegments.get(i);
            world.food.add(new Food(segment.x, segment.y, 1));

            bodySegments.remove(i);
        }

        world.food.add(new Food(x, y, 1));

        world.snake = null;
    }
}


