package dk.kea.class2016february.markus.gameengine.SnakeGame;

import dk.kea.class2016february.markus.gameengine.Game;
import dk.kea.class2016february.markus.gameengine.Screen;

public class SnakeGame extends Game
{
    @Override
    public Screen createStartScreen()
    {
        music = this.loadMusic("Death By Glamour_Extended.mp3");
        return new StartScreen(this);
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
