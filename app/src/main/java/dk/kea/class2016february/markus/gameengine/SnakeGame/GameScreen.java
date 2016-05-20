package dk.kea.class2016february.markus.gameengine.SnakeGame;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;

import dk.kea.class2016february.markus.gameengine.Game;
import dk.kea.class2016february.markus.gameengine.Screen;
import dk.kea.class2016february.markus.gameengine.SnakeGame.World;
import dk.kea.class2016february.markus.gameengine.SnakeGame.WorldRenderer;

public class GameScreen extends Screen
{
    enum State
    {
        Paused, Running, gameOver
    }

    Typeface font;
    Bitmap background;
    Bitmap resume;
    Bitmap gameOver;
    State state = State.Running;
    World world;
    WorldRenderer renderer;

    public GameScreen(Game game)
    {
        super(game);

        font = game.loadFont("Qarmic_sans_Abridged.ttf");
        background = game.loadBitmap("background.png");
        resume = game.loadBitmap("resume.png");
        gameOver = game.loadBitmap("gameover.png");
        world = new World(game);
        renderer = new WorldRenderer(game, world);
    }

    @Override
    public void update(float deltaTime)
    {
        if (world.gameOver)
        {
            state = State.gameOver;
        }
        if (state == State.Paused && game.isTouchDown(0))
        {
            state = State.Running;
        }
        if (state == State.gameOver && game.isTouchDown(0))
        {
            game.setScreen(new StartScreen(game));
            return;
        }
        if (state == State.Running && game.getTouchY(0) < 36 && game.getTouchX(0) > 320 - 36)
        {
            state = State.Paused;
        }
//        game.drawBitmap(background, 0, 0);
        if (state == State.Paused)
        {
            game.drawBitmap(resume, 160 - resume.getWidth() / 2, 320);
        }


        if (state == State.Running)
        {
            int touchX = -1;
            if (game.isTouchDown(0)) touchX = game.getTouchX(0);
            world.update(deltaTime, touchX);
//            game.drawText(font, "");
        }
//        game.drawBitmap(background, 0, 0);

        renderer.render();

        if (state == State.gameOver)
        {
    //            game.drawBitmap(gameOver, 160 - gameOver.getWidth() / 2, 320);
            game.drawText(font, "Game Over", 40, 120, Color.GREEN, 40);
            game.drawText(font, "Your Score:", 100, 240, Color.GREEN, 20);
            game.drawText(font, "" + world.snake.score, 140, 260, Color.YELLOW, 30);
        }


    }

    @Override
    public void pause()
    {
        if (state == State.Running)
        {
            state = State.Paused;
        }
    }

    @Override
    public void resume()
    {

    }

    @Override
    public void dispose()
    {

    }
}
