package dk.kea.class2016february.markus.gameengine.SnakeGame;


import android.util.Log;

import java.util.ArrayList;
import java.util.List;



public class Snake
{
    public static final float WIDTH = 32;
    public static final float HEIGHT = 32;
    private float totalVelocity;

    int id;
    float x;
    float y;
    private float velocityX;
    private float velocityY;
    float angle;

    float turnSpeed = 200;

    private float segmentTimer;
    private static float TIMERLIMIT = 0.13f;

//    private int bodyCount;
    List<SnakeBody> bodySegments = new ArrayList<>();;

    private int lastBodySegment;
    private boolean newBodySegment;
    private int newSegmentCount;
    private World world;

    public Snake(int id, float x, float y, float velocity, World world)
    {
        this.id = id;
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

        bodySegments.add(new SnakeBody(0, x, y));

        if (world.online)
        {
            world.decoder.sendNewSnake(id,angle,x,y);
            world.decoder.sendNewBody(id, 0, x, y);
        }
    }

    public Snake(int id, float angle, float x, float y)
    {
        this. id = id;
        this. x = x;
        this.y = y;
        this. angle = angle;
    }

    public void update(float deltaTime, float input)
    {
        collideFood();
        segmentTimer += 1*deltaTime;
        if (segmentTimer>=Snake.TIMERLIMIT)
        {
            int size = bodySegments.size();
            segmentTimer = 0;
            if (newBodySegment)
            {
                bodySegments.add(new SnakeBody(size, x, y));
                if (world.online)
                {
                    world.decoder.sendNewBody(id, size, x, y);
                }
                newSegmentCount--;
                if (newSegmentCount == 0) newBodySegment = false;
            }
            else
            {
                SnakeBody body = bodySegments.get(lastBodySegment);
                body.x = x;
                body.y = y;
                if (world.online)
                {
                    world.decoder.sendSetBody(id, body.id, x, y);
                }
                lastBodySegment++;
                if (size < lastBodySegment + 1) lastBodySegment -= size;
            }
        }

        //angle ændres i forhold til brugerinput
        //da input kan være både positivt, 0 eller negativt, benyttes et enkelt regnestykke
        angle += turnSpeed * input * deltaTime;
        //angle holdes konstant mellem 0 og 360
        if (angle < 0) angle += 360;
        else if(angle > 360) angle -=360;



        move(deltaTime);

        collideBorder();
        collideEnemy();
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

                        if (world.online)
                        {
                            world.decoder.sendDelFood(id, food.id);
                        }
                        world.food.remove(i);

                    }
                }
            }
        }
    }

    private void collideEnemy()
    {
        float rightEdge = this.x + Snake.WIDTH;
        float lowerEdge = this.y + Snake.HEIGHT;

        for (Snake enemy : world.enemies)
        {
            for (SnakeBody body : enemy.bodySegments)
            {
                float otherRightEdge = body.x + SnakeBody.WIDTH;
                float otherLeftEdge = body.x;
                if ((x <= otherRightEdge && x >= otherLeftEdge) || (rightEdge <= otherRightEdge && rightEdge >= otherLeftEdge) || (x <= otherLeftEdge && rightEdge >= otherRightEdge))
                {
                    float otherUpperEdge = body.y;
                    float otherLowerEdge = body.y + SnakeBody.HEIGHT;
                    if ((y <= otherLowerEdge && y >= otherUpperEdge) || (lowerEdge <= otherLowerEdge && lowerEdge >= otherUpperEdge))
                    {
                        die();
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
        //angle er grader vinkel, hvor vinlen 0 peger mod højre på skærmen
        //angle benyttes til at danne en retning til vector ved omdannelse til radianer
        //derefter bruges bruges sinus eller cosinus til udregning af ændringen i enten x eller y akserne
        //disse ganges med totalVelocity, der fungerer som vektorens længde
        //dette giver relative koordinater til en vector med udgangspunkt fra koordinaterne 0,0
        velocityX = (float) Math.cos(Math.toRadians(angle)) * totalVelocity;
        velocityY = (float)Math.sin(Math.toRadians(angle)) * totalVelocity;
        //relative koordinater ganges med deltatid og lægges oveni nuværende koordinater
        x += velocityX * deltaTime;
        y += velocityY * deltaTime;

        if (world.online)
        {
            world.decoder.sendSetSnake(id, angle, x, y);
        }

    }

    private void die()
    {
        world.gameOver = true;

        int size = bodySegments.size();
//        for (int i = size - 1; i >= 0; i--)
//        {
//            SnakeBody segment = bodySegments.get(i);
//            if (world.online)
//            {
//                world.decoder.sendSpawnFood(segment.x, segment.y);
//            }
//            else
//            {
//                world.food.add(new Food(0, segment.x, segment.y));
//                bodySegments.remove(i);
//            }
//        }

        if (world.online)
        {
            world.decoder.sendSpawnFood(x, y);
            world.decoder.sendDelSnake(id);
        }
        else
        {
            world.food.add(new Food(0, x, y));
        }

        world.snake = null;
    }
}


