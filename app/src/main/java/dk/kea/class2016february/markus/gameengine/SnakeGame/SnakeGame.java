package dk.kea.class2016february.markus.gameengine.SnakeGame;

import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

import dk.kea.class2016february.markus.gameengine.Game;
import dk.kea.class2016february.markus.gameengine.Screen;

public class SnakeGame extends Game
{
    private ConnectionHandler connectionHandler = new ConnectionHandler();
    @Override
    public Screen createStartScreen()
    {

        music = this.loadMusic("Death By Glamour_Extended.mp3");
        return new StartScreen(this, connectionHandler);
    }

}
