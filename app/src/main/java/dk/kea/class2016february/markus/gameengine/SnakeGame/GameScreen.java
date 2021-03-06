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
import dk.kea.class2016february.markus.gameengine.Sound;

public class GameScreen extends Screen
{
    enum State
    {
        Paused, Running, gameOver
    }

    boolean accelerometer;
    private boolean pointerReleased;

    Typeface font;
    Bitmap background;
    Bitmap resume;
    Bitmap gameOver;
    Sound trombone;
    State state = State.Running;
    ConnectionHandler connectionHandler;
    World world;
    WorldRenderer renderer;

    public GameScreen(Game game, boolean useAccelerometer, ConnectionHandler connectionHandler)
    {
        super(game);
        this.connectionHandler = connectionHandler;
        this.accelerometer = useAccelerometer;

        font = game.loadFont("Qarmic_sans_Abridged.ttf");
        background = game.loadBitmap("background.png");
        resume = game.loadBitmap("resume.png");
        gameOver = game.loadBitmap("gameover.png");
        trombone = game.loadSound("Sad_Trombone.mp3");

        world = new World(connectionHandler);
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
        if (state == State.gameOver && game.isTouchDown(0) && pointerReleased)
        {
            dispose();
            game.setScreen(new StartScreen(game, connectionHandler));
            return;
        }

        //GameScreen klargører brugerinputs til world
        float input = 0;
        if (state == State.Running)
        {
            //der benyttes enten accelerometer eller touch-funktionalitet
            if (accelerometer)
            {
                //accelerometer giver mellem 10 eller -10 naturligt
                //divideres med 10 for at have en fælles standard
                input = -game.getAccelerometer()[0] / 10;
            }
            else
            {
                if (game.isTouchDown(0))
                {
                    //som touch gives enten 1, 0 eller -1
                    int touchX = game.getTouchX(0);
                    if (touchX < 49) input = -1;
                    else if (touchX > 270) input = 1;
                }
            }
        }
        world.update(deltaTime, input);
        renderer.render();

        if (state == State.Running)
        {
            game.drawText(font, "Score:", 200, 10, Color.GREEN, 14);
            game.drawText(font, "" + world.score, 260, 10, Color.YELLOW, 14);
        }
        else if (state == State.gameOver)
        {
            if(game.music.isPlaying())
            {
                game.music.pause();
                trombone.play(0.8f);
            }
            game.drawText(font, "Game Over", 40, 120, Color.GREEN, 40);
            game.drawText(font, "Your Score:", 100, 240, Color.GREEN, 20);
            game.drawText(font, "" + world.score, 140, 260, Color.YELLOW, 30);
        }

        pointerReleased = !game.isTouchDown(0);
        Log.d("pointerReleased", "" + pointerReleased);
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
//        connectionHandler.terminateConnection();
    }

    @Override
    public void resume()
    {
        game.music.start();
    }

    @Override
    public void dispose()
    {
        connectionHandler.send("CLOSE" + connectionHandler.split + connectionHandler.getConnectionId());
        game.music.pause();
//        connectionHandler.terminateConnection();
    }
}
