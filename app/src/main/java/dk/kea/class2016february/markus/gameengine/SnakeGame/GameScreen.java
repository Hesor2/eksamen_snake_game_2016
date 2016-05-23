package dk.kea.class2016february.markus.gameengine.SnakeGame;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;

import java.net.Socket;

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

    boolean accelerometer;

    Typeface font;
    Bitmap background;
    Bitmap resume;
    Bitmap gameOver;
    State state = State.Running;
    World world;
    WorldRenderer renderer;

    public GameScreen(Game game, boolean useAccelerometer, Socket socket)
    {
        super(game);
        this.accelerometer = useAccelerometer;



        font = game.loadFont("Qarmic_sans_Abridged.ttf");
        background = game.loadBitmap("background.png");
        resume = game.loadBitmap("resume.png");
        gameOver = game.loadBitmap("gameover.png");
        world = new World(game, socket);
        renderer = new WorldRenderer(game, world);

        resume();
    }

    @Override
    public void update(float deltaTime)
    {
        if (world.gameOver)
        {
            state = State.gameOver;
        }
//        if (state == State.Paused && game.isTouchDown(0))
//        {
//            state = State.Running;
//            resume();
//        }
        if (state == State.gameOver && game.isTouchDown(0))
        {
            dispose();
            game.setScreen(new StartScreen(game));
            return;
        }
//        if (state == State.Running && game.getTouchY(0) < 36 && game.getTouchX(0) > 320 - 36)
//        {
//            state = State.Paused;
//            pause();
//        }
//        game.drawBitmap(background, 0, 0);
//        if (state == State.Paused)
//        {
//            game.drawBitmap(resume, 160 - resume.getWidth() / 2, 320);
//        }


        if (state == State.Running)
        {
            float input = 0;
            if (accelerometer)
            {
                input = -game.getAccelerometer()[0] / 10;
//                Log.d("accel", "" + input);
            }
            else
            {
                if (game.isTouchDown(0))
                {
                    int touchX = game.getTouchX(0);
                    if (touchX < 49) input = -1;
                    else if (touchX > 270) input = 1;
                }
            }
            world.update(deltaTime, input);
        }

        renderer.render();

        if (state == State.gameOver)
        {
            game.drawText(font, "Game Over", 40, 120, Color.GREEN, 40);
            game.drawText(font, "Your Score:", 100, 240, Color.GREEN, 20);
            game.drawText(font, "" + world.snake.score, 140, 260, Color.YELLOW, 30);
        }
    }

    @Override
    public void pause()
    {
        //super.onPause();
        if(state == State.Running)
        {
            state = State.Paused;

        }
        game.music.pause();
    }

    @Override
    public void resume()
    {
        game.music.start();
    }

    @Override
    public void dispose()
    {
        game.music.pause();
    }
}
