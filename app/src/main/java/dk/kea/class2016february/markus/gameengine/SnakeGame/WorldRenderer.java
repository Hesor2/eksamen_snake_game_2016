package dk.kea.class2016february.markus.gameengine.SnakeGame;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.Log;

import dk.kea.class2016february.markus.gameengine.Game;

public class WorldRenderer
{
    Game game;
    World world;

    Rect cameraBoundary;

    Bitmap snaketestImage;

    Bitmap backgroundImage;
    Bitmap snakeheadImage;
    Bitmap snakebodyImage;
    Bitmap enemyheadImage;
    Bitmap enemybodyImage;
    Bitmap foodImage;

    public WorldRenderer(Game game, World world)
    {
        this.game = game;
        this.world = world;

        this.snaketestImage = game.loadBitmap("Snake_Test.png");

        this.backgroundImage = game.loadBitmap("bck.png");
        this.snakeheadImage = game.loadBitmap("snakeface.png");
        this.snakebodyImage = game.loadBitmap("snakebody.png");
        this.enemyheadImage = game.loadBitmap("snakeface red.png");
        this.enemybodyImage = game.loadBitmap("snakebody red.png");
        this.foodImage = game.loadBitmap("food.png");
    }

    public void render()
    {
        Bitmap rotatedBitmap;
        cameraBoundary = world.camera.getBoundary();

        //Baggrund tegnes ud fra kameraets information med følgende metode fra GameEngine
        //drawBitmap(Bitmap bitmap, int x, int y, int srcX, int srcY, int srcWidth, int srcHeight)
        game.drawBitmap(backgroundImage, 0, 0, (int)cameraBoundary.left, (int)cameraBoundary.top, (int)Camera.WIDTH, (int)Camera.HEIGHT);
        //Spillets objekter tegnes ud fra World-objektets information
        for (Food food : world.food)
        {
            float foodRight = food.x + Food.WIDTH;
            float foodBottom = food.y + Food.HEIGHT;
            //objekterne tjekkes for, hvor vidt de er inde for kameraets rækkevidde
            if (food.x <= cameraBoundary.right && foodRight >= cameraBoundary.left && food.y <= cameraBoundary.bottom && foodBottom >= cameraBoundary.top)
            {
                //objekterne tegnes på canvas relativt til kameraets position
                game.drawBitmap(foodImage, (int) food.x - cameraBoundary.left, (int) food.y - cameraBoundary.top);
            }
        }

        //Snake
        if (!world.gameOver)
        {
            for (SnakeBody bodySegment : world.snake.bodySegments)
            {
                float bodyRight = bodySegment.x + SnakeBody.WIDTH;
                float bodyBottom = bodySegment.y + SnakeBody.HEIGHT;
                if (bodySegment.x <= cameraBoundary.right && bodyRight >= cameraBoundary.left && bodySegment.y <= cameraBoundary.bottom && bodyBottom >= cameraBoundary.top)
                {
                    game.drawBitmap(snakebodyImage, (int) bodySegment.x - cameraBoundary.left, (int) bodySegment.y - cameraBoundary.top);
                }
            }

            rotatedBitmap = game.rotateBitmap(snakeheadImage, world.snake.angle);
            game.drawBitmap(rotatedBitmap, (int)world.snake.x - cameraBoundary.left, (int)world.snake.y - cameraBoundary.top);
        }


        //Snake Head

        //test1
//        game.drawBitmap(snaketestImage, (int) world.snake.x - cameraBoundary.left, (int) world.snake.y - cameraBoundary.top);
//        Matrix matrix = new Matrix();
//        matrix.preRotate(world.snake.angle);
//        matrix.postTranslate(world.snake.x - cameraBoundary.left, world.snake.y - cameraBoundary.top);

        //test2
//        matrix.setTranslate(16f, 16f);
//        matrix.postRotate(world.snake.angle);
//        matrix.postTranslate(world.snake.x - 16f - cameraBoundary.left, world.snake.y - 16f - cameraBoundary.top);

//        game.drawBitmap(snakeheadImage, matrix);

        //Enemies
        for (Snake enemy : world.enemies)
        {
            for (SnakeBody bodySegment : enemy.bodySegments)
            {
                float bodyRight = bodySegment.x + SnakeBody.WIDTH;
                float bodyBottom = bodySegment.y + SnakeBody.HEIGHT;
                if (bodySegment.x <= cameraBoundary.right && bodyRight >= cameraBoundary.left && bodySegment.y <= cameraBoundary.bottom && bodyBottom >= cameraBoundary.top)
                {
//                    rotatedBitmap = game.rotateBitmap(enemybodyImage, bodySegment.angle);
//                    game.drawBitmap(rotatedBitmap, (int) bodySegment.x - cameraBoundary.left, (int) bodySegment.y - cameraBoundary.top);

                    game.drawBitmap(enemybodyImage, (int) bodySegment.x - cameraBoundary.left, (int) bodySegment.y - cameraBoundary.top);
                }
            }
            float snakeRight = enemy.x + Snake.WIDTH;
            float snakeBottom = enemy.y + Snake.HEIGHT;
            if (enemy.x <= cameraBoundary.right && snakeRight >= cameraBoundary.left && enemy.y <= cameraBoundary.bottom && snakeBottom >= cameraBoundary.top)
            {
                rotatedBitmap = game.rotateBitmap(enemyheadImage, enemy.angle);
                game.drawBitmap(rotatedBitmap, (int) enemy.x - cameraBoundary.left, (int) enemy.y - cameraBoundary.top);
            }
        }
    }
}
