package dk.kea.class2016february.markus.gameengine.BreakoutGame;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;

import java.util.List;

import dk.kea.class2016february.markus.gameengine.CollisionListener;
import dk.kea.class2016february.markus.gameengine.Game;
import dk.kea.class2016february.markus.gameengine.Music;
import dk.kea.class2016february.markus.gameengine.Screen;
import dk.kea.class2016february.markus.gameengine.Sound;
import dk.kea.class2016february.markus.gameengine.TouchEvent;

public class GameScreen extends Screen
{
    enum State
    {
        Paused,
        Running,
        gameOver,
    }

    Bitmap background;
    Bitmap resume;
    Bitmap gameOver;
    Typeface font;
    Sound bounceSound;
    Sound blockSound;
    State state = State.Running;
    World world;
    WorldRenderer renderer;



    public GameScreen(Game game)
    {
        super(game);
        background = game.loadBitmap("background.png");
        resume = game.loadBitmap("resume.png");
        gameOver = game.loadBitmap("gameover.png");

        font = game.loadFont("font.ttf");
        bounceSound = game.loadSound("bounce.wav");
        blockSound = game.loadSound("blocksplosion.wav");
        world = new World(new CollisionListener()
        {
            public void collisionWall(){bounceSound.play(1);}
            public void collisionPaddle(){bounceSound.play(1);}
            public void collisionBlock(){blockSound.play(1);}
        }
        );
        renderer = new WorldRenderer(game, world);


    }

    @Override
    public void update(float deltaTime)
    {
        if(world.gameover == true) state = State.gameOver;

        if(state == State.Paused && game.getTouchEvents().size() > 0)
        {
            state = State.Running;
            resume();
        }
        if(state == State.gameOver)
        {
            List<TouchEvent> events = game.getTouchEvents();
            int stop = events.size();

            for (int i = 0; i < stop; i++)
            {
                if(events.get(i).type == TouchEvent.TouchEventType.Up)
                {
                    game.setScreen(new MainMenuScreen(game));
                    return;
                }
            }
            dispose();
        }
        if(state == State.Running && game.getTouchY(0) < 36 && game.getTouchX(0) > 320 -36)
        {
            state = State.Paused;
            pause();

        }

        game.drawBitmap(background, 0, 0);
        if(state == State.Paused)
        {
            game.drawBitmap(resume, 160 - resume.getWidth()/2, 240 - resume.getHeight()/2);
        }
        if(state == State.gameOver)
        {
            game.drawBitmap(gameOver, 160 - gameOver.getWidth()/2, 240 - gameOver.getHeight()/2);
        }
        //do something with ball, blocks and paddle

        if(state == State.Running)
        {
            int touchX = -1;
            if (game.isTouchDown(0))
            {
                touchX = game.getTouchX(0);
            }
            world.update(deltaTime, game.getAccelerometer()[0], touchX);

        }

        game.drawText(font, "Points: " + Integer.toString(world.points), 20, 12, Color.GREEN, 8);
        game.drawText(font, "Oneman(s):" + Integer.toString(world.lives), 90, 12, Color.GREEN, 8);
        renderer.Render();

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
        game.music.play();
    }

    @Override
    public void dispose()
    {
        game.music.pause();
    }
}
