package dk.kea.class2016february.markus.gameengine;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import java.util.Random;

public class SimpleScreen extends Screen
{
    Bitmap bob;
    float x = -128;
    int y = 0;
    Random rand = new Random();
    int clearColor = Color.BLUE;
    Sound sound;
    Music music;
    boolean userWantsMusic = false;

    public SimpleScreen(Game game)
    {
        super(game);
        bob = game.loadBitmap("bob.png");
        sound = game.loadSound("explosion.ogg");
        music = game.loadMusic("music.ogg");
    }

    public void update(float deltaTime)
    {
        Log.d("deltaTime", "" + deltaTime);
        game.clearFramebuffer(clearColor);

        x = x + 50*deltaTime;
        if(x > game.getOffscreenWidth()) x = -128;
        game.drawBitmap(bob, (int)x, 10);
//        Log.d("FPS: ", "" + game.getFramerate());
        if(game.isTouchDown(0))
        {
            if(userWantsMusic)
            {
                music.pause();
                userWantsMusic = false;
            }
            else
            {
                music.play();
                userWantsMusic = true;
            }
        }


//        for (int pointer = 0; pointer < 5; pointer++)
//        {
//            if (game.isTouchDown(pointer))
//            {
//                game.drawBitmap(bob, game.getTouchX(pointer), game.getTouchY(pointer));
//                sound.play(1);
//            }
//        }
//        float x = -game.getAccelerometer()[0];
//        float y = game.getAccelerometer()[1];
//        x = (x/10) * game.getOffscreenWidth()/2 + game.getOffscreenWidth()/2;
//        y = (y/10) * game.getOffscreenHeight()/2 + game.getOffscreenHeight()/2;
//
//        game.drawBitmap(bob, (int)x-64, (int)y-64);


    }

    public void pause()
    {
        music.pause();
        Log.d("SimpleScreen", "we are pausing");
    }

    public void resume()
    {
        if(userWantsMusic) music.play();
        Log.d("SimpleScreen", "we are resuming");
    }

    public void dispose()
    {
        Log.d("SimpleScreen", "we are disposing the game");
        sound.dispose();
    }


}
