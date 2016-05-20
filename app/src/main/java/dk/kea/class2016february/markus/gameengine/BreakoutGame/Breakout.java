package dk.kea.class2016february.markus.gameengine.BreakoutGame;


import dk.kea.class2016february.markus.gameengine.Game;
import dk.kea.class2016february.markus.gameengine.Screen;

public class Breakout extends Game
{
    @Override
    public Screen createStartScreen()
    {
        music = this.loadMusic("music.ogg");
        return new MainMenuScreen(this);
    }

    public void onPause()
    {
        super.onPause();
        music.pause();
    }

    public void onResume()
    {
        super.onResume();
        music.play();
    }
}
