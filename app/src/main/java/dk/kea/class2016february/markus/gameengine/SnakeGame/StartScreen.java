package dk.kea.class2016february.markus.gameengine.SnakeGame;

import android.graphics.Bitmap;
import android.util.Log;

import dk.kea.class2016february.markus.gameengine.Game;
import dk.kea.class2016february.markus.gameengine.Screen;

public class StartScreen extends Screen
{
    boolean pointerReleased;
    Bitmap buttons;
    Bitmap mainmenu;
    Bitmap insertCoin;
    float passedTime = 0;
    Connection C;
    public StartScreen(Game game)
    {

        super(game);
        buttons = game.loadBitmap("Snake_Buttons.png");
        mainmenu = game.loadBitmap("mainmenu.png");
        insertCoin = game.loadBitmap("insertcoin.png");
        try
        {
            C = new Connection();
            Log.d("SnakeGame", "CC Created");
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void update(float deltaTime)
    {
        if (game.isTouchDown(0))
        {
            if (pointerReleased)
            {
                int touchY = game.getTouchY(0);
                if (touchY >= 20 && touchY <= 100)
                {
                    //connect ting

                }
                else if(touchY >= 279 && touchY <= 359)
                {
                    //touch ting
                    game.setScreen(new GameScreen(game, false, null));
                    return;
                }
                else if(touchY >= 379 && touchY <= 459)
                {
                    //accel ting
                    game.setScreen(new GameScreen(game, true, null));
                    return;
                }
            }
            pointerReleased = false;
        }
        else
        {
            pointerReleased = true;
        }
        game.drawBitmap(mainmenu,0,0);
        // button width 280, height 80
        //Connect-knap
        game.drawBitmap(buttons, 20, 20, 0, 160, 280, 80);
        //Touch-knap
        game.drawBitmap(buttons, 20, 279, 0, 0, 280, 80);
        //Accel-knap
        game.drawBitmap(buttons, 20, 379, 0, 80, 280, 80);
    }

    @Override
    public void pause()
    {

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
