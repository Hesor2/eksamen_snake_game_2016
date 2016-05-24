package dk.kea.class2016february.markus.gameengine.SnakeGame;

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
    public float maxX = 1919;
    public float minY = 0;
    //479
    public float maxY = 1079;
    public float foodTimer = 0;
    public static final float timerLimit = 2;

    public Game game;
    boolean online = false;
    private Random rand = new Random();

    Snake snake = new Snake(160, 320, 100, this);
    public Camera camera = new Camera(snake, this);
    List<Snake> enemies = new ArrayList<>();
    List<Food> food = new ArrayList<>();
    boolean gameOver = false;

    int score;

    public World(Game game, Socket socket)
    {
        this.game = game;
        if (socket != null) online = true;
        //test
        enemies.add(new Snake(160, 500, 100, this));
        for (int i = 0; i < 10; i++)
        {

            enemies.get(0).update(0.8f,-1);
        }
    }

    public void update(float deltaTime, float input)
    {
        if (online)
        {

        }
        else
        {
            foodTimer += 1*deltaTime;
            if (foodTimer>=World.timerLimit)
            {
                float x = rand.nextInt((int) maxX);
                float y = minY + rand.nextInt((int) (maxY - minY));

                int value = rand.nextInt(3);
//                value = 0;
                food.add(new Food(x, y, value+1));
                foodTimer = 0;
            }
        }

        if (!gameOver)
        {
            snake.update(deltaTime, input);
            camera.update();
        }
    }
}
