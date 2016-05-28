package dk.kea.class2016february.markus.gameengine.SnakeGame;

import android.util.Log;

import java.util.ArrayList;

public class InstructionDecoder
{
    ArrayList<String> currentInstructions = new ArrayList<>();

    ConnectionHandler connectionHandler;
    World world;


    public InstructionDecoder(ConnectionHandler connectionHandler, World world)
    {
        this.connectionHandler = connectionHandler;
        this.world = world;
    }

    public void executeInstructions()
    {
        currentInstructions.clear();
        synchronized (connectionHandler.instructions)
        {
            for (String s : connectionHandler.instructions)
            {
                currentInstructions.add(s);
            }
            connectionHandler.instructions.clear();
        }
            for (String s : currentInstructions)
            {
                String[] params = s.split(String.valueOf(connectionHandler.split));
                switch (params[0])
                {
                    case "NEW":
                        switch (params[1])
                        {
                            case "SNAKE":
                                int id = Integer.parseInt(params[2]);
                                float angle = Float.parseFloat(params[3]);
                                float x = Float.parseFloat(params[4]);
                                float y = Float.parseFloat(params[5]);
                                world.addEnemy(id, angle, x, y);
                                break;
                            case "BODY":
                                int snakeID = Integer.parseInt(params[2]);
                                int bodyID = Integer.parseInt(params[3]);
                                float bodyX = Float.parseFloat(params[4]);
                                float bodyY = Float.parseFloat(params[5]);
                                world.addEnemyBody(snakeID, bodyID, bodyX, bodyY);
                                break;
                            case "FOOD":
                                int foodID = Integer.parseInt(params[2]);
                                float foodX = Float.parseFloat(params[3]);
                                float foodY = Float.parseFloat(params[4]);
                                world.addFood(foodID, foodX, foodY);
                                break;
                        }
                        break;
                    case "SET":
                        switch (params[1])
                        {
                            case "SNAKE":
                                int id = Integer.parseInt(params[2]);
                                float angle = Float.parseFloat(params[3]);
                                float x = Float.parseFloat(params[4]);
                                float y = Float.parseFloat(params[5]);
                                world.setEnemy(id, angle, x, y);
                                break;
                            case "BODY":
                                int snakeID = Integer.parseInt(params[2]);
                                int bodyID = Integer.parseInt(params[3]);
                                float bodyX = Float.parseFloat(params[4]);
                                float bodyY = Float.parseFloat(params[5]);
                                world.setEnemyBody(snakeID, bodyID, bodyX, bodyY);
                                break;
                        }
                        break;
                    case "DEL":
                        switch (params[1])
                        {
                            case "SNAKE":
                                int id = Integer.parseInt(params[2]);
                                world.deleteEnemy(id);
                                break;
                            case "BODY":
                                int snakeID = Integer.parseInt(params[2]);
                                int bodyID = Integer.parseInt(params[3]);
                                world.deleteEnemyBody(snakeID, bodyID);
                                break;
                            case "FOOD":
                                int foodID = Integer.parseInt(params[2]);
                                world.deleteFood(foodID);
                                break;
                        }
                        break;
                }
            }
    }

    public void sendNewSnake(int id, float angle, float x, float y)
    {
        connectionHandler.send("NEW" + connectionHandler.split + "SNAKE" + connectionHandler.split + id + connectionHandler.split + angle + connectionHandler.split + x + connectionHandler.split + y + '\n');
    }

    public void sendNewBody(int snakeID, int bodyID, float x, float y)
    {
        connectionHandler.send("NEW" + connectionHandler.split + "BODY" + connectionHandler.split + snakeID + connectionHandler.split + bodyID + connectionHandler.split + x + connectionHandler.split + y + '\n');
    }

    public void sendSetSnake(int id, float angle, float x, float y)
    {
        connectionHandler.send("SET" + connectionHandler.split + "SNAKE" + connectionHandler.split + id + connectionHandler.split + angle + connectionHandler.split + x + connectionHandler.split + y + '\n');
    }

    public void sendSetBody(int snakeID, int bodyID, float x, float y)
    {
        connectionHandler.send("SET" + connectionHandler.split + "BODY" + connectionHandler.split + snakeID + connectionHandler.split + bodyID + connectionHandler.split + x + connectionHandler.split + y + '\n');
    }

    public void sendDelSnake(int id)
    {
        connectionHandler.send("DEL" + connectionHandler.split + "SNAKE" + connectionHandler.split + id + '\n');
    }

    public void sendDelBody(int snakeID, int bodyID)
    {
        connectionHandler.send("DEL" + connectionHandler.split + "BODY" + connectionHandler.split + snakeID + connectionHandler.split + bodyID + '\n');
    }

    public void sendDelFood(int id)
    {
        connectionHandler.send("DEL" + connectionHandler.split + "FOOD" + connectionHandler.split + id + '\n');
    }

    public void sendSpawnFood(float x, float y)
    {
        connectionHandler.send("SPAWN" + connectionHandler.split + "FOOD" + connectionHandler.split + x + connectionHandler.split + y + '\n');
    }
}
