package dk.kea.class2016february.markus.gameengine.SnakeGame;

import android.graphics.Bitmap;

import dk.kea.class2016february.markus.gameengine.Game;
import dk.kea.class2016february.markus.gameengine.Screen;

public class StartScreen extends Screen
{
    Bitmap mainmenu;
    Bitmap insertCoin;
    float passedTime = 0;

    public StartScreen(Game game)
    {
        super(game);
        mainmenu = game.loadBitmap("mainmenu.png");
        insertCoin = game.loadBitmap("insertcoin.png");
    }

    @Override
    public void update(float deltaTime)
    {
        if (game.isTouchDown(0))
        {
            game.setScreen(new GameScreen(game));
            return;
        }
        passedTime = passedTime + deltaTime;
        game.drawBitmap(mainmenu,0,0);
        if ( (passedTime - (int)passedTime) > 0.5f)
        {
            game.drawBitmap(insertCoin, 160 - (insertCoin.getWidth()/2),320);
        }
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
