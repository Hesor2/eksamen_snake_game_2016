package dk.kea.class2016february.markus.gameengine.BreakoutGame;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import dk.kea.class2016february.markus.gameengine.CollisionListener;

public class World
{
    public static final float min_X = 0;
    public static final float max_X = 319;
    public static final float min_Y = 32;
    public static final float max_Y = 479;

    int points = 0;
    int lives = 3;
    boolean justLostLife = false;

    boolean gameover = false;

    int level = 1;
    int paddleHits = 0;


    Ball ball = new Ball();

    Paddle paddle = new Paddle();

    List<Block> blocks = new ArrayList<>();

    CollisionListener listener;

    public World(CollisionListener listener)
    {
        generateBlocks();
        this.listener = listener;
    }


    public void update(float deltaTime, float accelX, float touchX)
    {
        if (justLostLife)
        {
            if (touchX > 0)
            {
                Log.d("you touched screen at", "" + touchX);
                justLostLife = false;
                Ball tempBall = new Ball();
                ball.velocityX = tempBall.velocityX;
                ball.velocityY = tempBall.velocityY;
            }
            else
            {
                ball.x = paddle.x + Paddle.WIDTH/2 - Ball.WIDTH/2;
            }
        }
        if(ball.y + Ball.HEIGHT > max_Y - Ball.HEIGHT)
        {
            lives--;
            ball.x = paddle.x + Paddle.WIDTH/2;
            ball.y = paddle.y - Ball.HEIGHT - 10;
            ball.velocityY = 0;
            ball.velocityX = 0;
            justLostLife = true;
            if (lives == 0)
            {
                gameover = true;
                return;
            }
        }


        if(blocks.size() == 0)
        {
            level++;
            ball = new Ball();
            ball.velocityX = ball.velocityX * (1.0f + (float)level * 0.5f);
            ball.velocityY = ball.velocityY * (1.0f + (float)level * 0.5f);
            paddle = new Paddle();
            generateBlocks();

        }


        ball.x = ball.x + ball.velocityX * deltaTime;
        ball.y = ball.y + ball.velocityY * deltaTime;

        collideWalls();

        paddle.x = paddle.x - (accelX * 100 * deltaTime);


        if(paddle.x < min_X)
        {
            paddle.x = min_X;
        }
        if(paddle.x + paddle.WIDTH > max_X)
        {
            paddle.x = max_X - paddle.WIDTH;
        }

        collideBallPaddle();
        collideBallBlocks(deltaTime);
    }

    private void collideBallBlocks(float deltaTime)
    {
        int stop = blocks.size();
        Block block;

        for(int i = 0; i < stop; i++)
        {
            block = blocks.get(i);

            if(collideRects(ball.x, ball.y, Ball.WIDTH, Ball.HEIGHT, block.x, block.y, Block.WIDTH, Block.HEIGHT))
            {
                points = points + (10 - block.type) * 100;
                blocks.remove(i);
                i--;
                stop--;
                float oldVelocityX = ball.velocityX;
                float oldVelocityY = ball.velocityY;

                reflectBall(ball, block);
                ball.x = ball.x - oldVelocityX * deltaTime * 1.01f;
                ball.y = ball.y - oldVelocityY * deltaTime * 1.01f;
                listener.collisionBlock();

            }
        }
    }

    private void reflectBall(Ball ball, Block block)
    {
        //check top left
        if(collideRects(ball.x, ball.y, Ball.WIDTH, Ball.HEIGHT, block.x, block.y, 1, 1))
        {
            if(ball.velocityX > 0) ball.velocityX = -ball.velocityX;
            if(ball.velocityY > 0) ball.velocityY = -ball.velocityY;
            return;

        }
        //check top right
        if(collideRects(ball.x, ball.y, Ball.WIDTH, Ball.HEIGHT, block.x + Block.WIDTH, block.y,1, 1))
        {
            if(ball.velocityX < 0) ball.velocityX = -ball.velocityX;
            if(ball.velocityY > 0) ball.velocityY = -ball.velocityY;
            return;
        }

        //check bottom left

        if(collideRects(ball.x, ball.y, Ball.WIDTH, Ball.HEIGHT, block.x, block.y + Block.HEIGHT, 1, 1))
        {
            if(ball.velocityX > 0) ball.velocityX = -ball.velocityX;
            if(ball.velocityY < 0) ball.velocityY = -ball.velocityY;
            return;
        }

        //check bottom right
        if(collideRects(ball.x, ball.y, Ball.WIDTH, Ball.HEIGHT, block.x + Block.WIDTH, block.y + Block.HEIGHT, 1, 1))
        {
            if(ball.velocityX < 0) ball.velocityX = -ball.velocityX;
            if(ball.velocityY > 0) ball.velocityY = -ball.velocityY;
            return;
        }

        //check top edge of block
        if(collideRects(ball.x, ball.y, Ball.WIDTH, Ball.HEIGHT, block.x, block.y, Block.WIDTH, 1))
        {
            if(ball.velocityY > 0) ball.velocityY = -ball.velocityY;
        }

        //check bottom edge
        if(collideRects(ball.x, ball.y, Ball.WIDTH, Ball.HEIGHT, block.x, block.y + Block.HEIGHT, Block.WIDTH, 1))
        {
            if(ball.velocityY < 0) ball.velocityY = -ball.velocityY;
            return;
        }

        //check left edge
        if(collideRects(ball.x, ball.y, Ball.WIDTH, Ball.HEIGHT, block.x, block.y, 1, Block.HEIGHT))
        {
            if(ball.velocityX > 0) ball.velocityX = -ball.velocityX;
            return;
        }

        //check right edge
        if(collideRects(ball.x, ball.y, Ball.WIDTH, Ball.HEIGHT, block.x + Block.WIDTH, block.y, 1, Block.HEIGHT))
        {
            if(ball.velocityX < 0) ball.velocityX = -ball.velocityX;
            return;
        }
    }

    private boolean collideRects(float x, float y, float width, float height, float x2, float y2, float width2, float height2)
    {
        if(x < x2 + width2 &&
                x + width > x2 &&
                y < y2 + height2 &&
                y + height > y2)
        {
            return true;
        }

        return false;
    }

    private void collideBallPaddle()
    {
        if(ball.y > paddle.y) return;
        //check if ball hits left paddle corner
        if(ball.x + Ball.WIDTH >= paddle.x &&
                ball.x + Ball.WIDTH <= paddle.x + 3 &&
                ball.y + Ball.HEIGHT + 2 >= paddle.y)

        {
            ball.y = paddle.y - Ball.HEIGHT - 2;
            ball.velocityY = -ball.velocityY;
            if(ball.velocityX > 0) ball.velocityX = -ball.velocityX;
            listener.collisionPaddle();
            advanceBlocks();
            return;
        }
        //check if ball hits right paddle corner
        if(ball.x < paddle.x + Paddle.WIDTH &&
                ball.x > paddle.x + Paddle.WIDTH - 3 &&
                ball.y + Ball.HEIGHT + 2 >= paddle.y)
        {
            ball.y = paddle.y - Ball.HEIGHT - 2;
            ball.velocityY = -ball.velocityY;
            if(ball.velocityX < 0) ball.velocityX = -ball.velocityX;
            listener.collisionPaddle();
            advanceBlocks();
            return;
        }
        //check if ball hits top edge of the paddle
        if(ball.x + Ball.WIDTH >= paddle.x &&
                ball.x < paddle.x + Paddle.WIDTH &&
                ball.y + Ball.HEIGHT + 2 >= paddle.y)
        {
            ball.y = paddle.y - Ball.HEIGHT - 2;
            ball.velocityY = -ball.velocityY;
            listener.collisionPaddle();
            advanceBlocks();
            return;
        }
    }

    private void advanceBlocks()
    {
        paddleHits++;
        if (paddleHits == 3)
        {
            paddleHits = 0;

            int stop = blocks.size();
            Block tempBlock;

            for (int i = 0; i < stop; i++)
            {
                tempBlock = blocks.get(i);
                tempBlock.y += (int)Block.HEIGHT/2;
            }
        }
    }

    private void collideWalls()
    {
        if(ball.x < min_X)
        {
            ball.velocityX = - ball.velocityX;
            ball.x = min_X;
            listener.collisionWall();
        }
        else if(ball.x + Ball.WIDTH > max_X)
        {
            ball.velocityX = - ball.velocityX;
            ball.x = max_X - Ball.WIDTH;
            listener.collisionWall();
        }
        if(ball.y < min_Y)
        {
            ball.velocityY = - ball.velocityY;
            ball.y = min_Y;
            listener.collisionWall();
        }


    }

    private void generateBlocks()
    {
        blocks.clear();

        int startY = (int)(min_Y + Ball.HEIGHT * 2);
        int type = 0;

        //Level stuff, super simple.
        startY = startY + level * 20;
        if(startY > 200) startY = 200;

        for(int y = startY; y < startY + 8 * Block.HEIGHT; y = y + (int)Block.HEIGHT, type++)
        {
            for(int x = 20; x < 320 - Block.WIDTH/2; x = x + (int)Block.WIDTH)
            {
                blocks.add(new Block(x, y, type));
            }
        }
    }



}
