package dk.kea.class2016february.markus.gameengine.demo;

import dk.kea.class2016february.markus.gameengine.Game;
import dk.kea.class2016february.markus.gameengine.Screen;

public class DemoGame extends Game
{
    @Override
    public Screen createStartScreen()
    {
        return new DemoScreen2(this);
    }
}
