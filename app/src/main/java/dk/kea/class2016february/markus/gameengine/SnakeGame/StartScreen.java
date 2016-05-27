package dk.kea.class2016february.markus.gameengine.SnakeGame;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;

import dk.kea.class2016february.markus.gameengine.Game;
import dk.kea.class2016february.markus.gameengine.Screen;

public class StartScreen extends Screen
{
    boolean pointerReleased;
    Bitmap buttons;
    Bitmap mainmenu;
    Bitmap insertCoin;
    Typeface font;
    ConnectionHandler connectionHandler;
    public StartScreen(Game game, ConnectionHandler connectionHandler)
    {
        super(game);
        this.connectionHandler = connectionHandler;

        buttons = game.loadBitmap("Snake_Buttons.png");
        mainmenu = game.loadBitmap("mainmenu.png");
        insertCoin = game.loadBitmap("insertcoin.png");
        font = game.loadFont("Qarmic_sans_Abridged.ttf");
        try
        {
//            C.terminate();
//            C = new Connection();
//            C.start();
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
                    try
                    {
                        if (connectionHandler.isConnected()) connectionHandler.terminateConnection();
                        Connection C = new Connection(connectionHandler);
                        C.start();
                        Log.d("SnakeGame", "CC Created");
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
                else if(touchY >= 279 && touchY <= 359)
                {
                    //touch ting
                    game.setScreen(new GameScreen(game, false, connectionHandler));
                    return;
                }
                else if(touchY >= 379 && touchY <= 459)
                {
                    //accel ting
                    game.setScreen(new GameScreen(game, true, connectionHandler));
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

        if (connectionHandler.isConnected())
        {
            game.drawText(font, "You are connected", 20, 120, Color.GREEN, 20);
        }

        //Touch-knap
        game.drawBitmap(buttons, 20, 279, 0, 0, 280, 80);
        //Accel-knap
        game.drawBitmap(buttons, 20, 379, 0, 80, 280, 80);
    }

    @Override
    public void pause()
    {
//        connectionHandler.terminateConnection();
    }

    @Override
    public void resume()
    {

    }

    @Override
    public void dispose()
    {
//        connectionHandler.terminateConnection();
    }
}
