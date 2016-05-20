package dk.kea.class2016february.markus.gameengine.SnakeGame;

import dk.kea.class2016february.markus.gameengine.Game;
import dk.kea.class2016february.markus.gameengine.Screen;

public class SnakeGame extends Game
{
    @Override
    public Screen createStartScreen()
    {
        return new StartScreen(this);
    }
}
