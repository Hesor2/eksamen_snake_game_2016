package dk.kea.class2016february.markus.gameengine.SnakeGame;

import android.util.Log;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import dk.kea.class2016february.markus.gameengine.Game;
import dk.kea.class2016february.markus.gameengine.State;

public class World
{
    public float minX = 0;
    //319
    public float maxX = 1999;
    public float minY = 0;
    //479
    public float maxY = 1999;
    public float foodTimer = 0;
    public static final float timerLimit = 2;

    ConnectionHandler connectionHandler;
    InstructionDecoder decoder;
    boolean online = false;
    private Random rand = new Random();

    Snake snake;
    public Camera camera;
    List<Snake> enemies = new ArrayList<>();
    List<Food> food = new ArrayList<>();
    boolean gameOver = false;

    int score;

    public World(ConnectionHandler connectionHandler)
    {
        this.connectionHandler = connectionHandler;
        this.decoder = new InstructionDecoder(connectionHandler, this);
        if (connectionHandler.isConnected())
        {
            online = true;
        }
        snake = new Snake(connectionHandler.getConnectionId(), 160, 320, 100, this);
        camera  = new Camera(snake, this);
    }

    public void update(float deltaTime, float input)
    {
//        Log.d("online", "" + online);
        if (online)
        {
            decoder.executeInstructions();
        }

        if (!gameOver)
        {
            foodTimer += 1*deltaTime;
            if (foodTimer>=World.timerLimit)
            {
                float x = rand.nextInt((int) maxX);
                float y = minY + rand.nextInt((int) (maxY - minY));

                if (online)
                {
//                Log.d("World spawn food", "" + x + " " + y);
                    decoder.sendSpawnFood(x,y);
                }
                else
                {
                    food.add(new Food(0, x, y));
                }
                foodTimer = 0;
            }

            snake.update(deltaTime, input);
            camera.update();
        }
    }

    public void addEnemy(int id, float angle, float x, float y)
    {
        enemies.add(new Snake(id, angle, x, y));
    }

    public void addEnemyBody(int snakeID, int bodyID, float x, float y)
    {
        for (Snake s : enemies)
        {
            if (snakeID == s.id)
            {
                s.bodySegments.add(new SnakeBody(bodyID, x, y));
                return;
            }
        }
    }

    public void addFood(int id, float x, float y)
    {
        food.add(new Food(id, x, y));
    }

    public void setEnemy(int id,float angle, float x, float y)
    {
        for (Snake s : enemies)
        {
            if (id == s.id)
            {
                Log.d("enemy found", "" + id);
                s.angle = angle;
                s.x = x;
                s.y = y;
                return;
            }
        }
    }

    public void setEnemyBody(int snakeID, int bodyID, float x, float y)
    {
        for (Snake s : enemies)
        {
            if (snakeID == s.id)
            {
                for (SnakeBody b : s.bodySegments)
                {
                    Log.d("enemy found " + snakeID, "body " + bodyID);
                    if (bodyID == b.id)
                    {
                        b.x = x;
                        b.y = y;
                        return;
                    }
                }
            }
        }
    }

    public void deleteEnemy(int id)
    {
        for (Snake s : enemies)
        {
            if (id == s.id)
            {
                enemies.remove(s);
                return;
            }
        }
    }

    public void deleteEnemyBody(int snakeID, int bodyID)
    {
        for (Snake s : enemies)
        {
            if (snakeID == s.id)
            {
                for (SnakeBody b : s.bodySegments)
                {
                    if (b.id == bodyID)
                    {
                        s.bodySegments.remove(b);
                        return;
                    }
                }
            }
        }
    }

    public void deleteFood(int id)
    {
        for (Food f : food)
        {
            if (id == f.id)
            {
                food.remove(f);
                return;
            }
        }
    }



}
