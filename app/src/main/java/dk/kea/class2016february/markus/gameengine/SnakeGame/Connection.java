package dk.kea.class2016february.markus.gameengine.SnakeGame;

import android.util.Log;

/**
 * Created by Shonix on 5/23/2016.
 */
public class Connection extends Thread
{
    ClientConnection CC;
    Connection(){
        try
        {
            CC = new ClientConnection();
            CC.start();
        } catch (Exception e)
        {
            e.printStackTrace();
        }

    }
}
