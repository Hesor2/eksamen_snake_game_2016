package dk.kea.class2016february.markus.gameengine.demo;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import java.util.Map;

import dk.kea.class2016february.markus.gameengine.Game;
import dk.kea.class2016february.markus.gameengine.demo.objects.actives.Bob2;
import dk.kea.class2016february.markus.gameengine.demo.objects.actives.JumpBob;
import dk.kea.class2016february.markus.gameengine.demo.objects.statics.StaticBob;

public class DemoScreen extends PhysScreen
{
    int clearColor = Color.BLUE;

    public DemoScreen(Game game)
    {
        super(game);

        for(int i = 0; i < 7; i++)
        {
            addStaticObject(new StaticBob(i*96, 400, this));
        }
        addActiveObject(new Bob2(-45, 0, this));
        addStaticObject(new StaticBob(200, 281, this));

        addActiveObject(new JumpBob(100, 100, this));
    }

    public void update(float deltaTime)
    {
        game.clearFramebuffer(clearColor);

        updateObjects();

        paintObjects();
    }

    public void pause()
    {
//        Log.d("SimpleScreen", "we are pausing");
//        Log.d("IDs", "Active Objects");
//        for (ActiveObject object : activeObjectList)
//        {
//            Log.d("id", "" + object.id);
//        }
//        Log.d("IDs", "Static Objects");
//        for (StaticObject object : staticObjectList)
//        {
//            Log.d("id", "" + object.id);
//        }
//        Log.d("Paths", "Sprites");
//        {
//            for (Map.Entry<String, Bitmap> entry : sprites.entrySet())
//            {
//                Log.d("Path", entry.getKey());
//            }
//        }
    }

    public void resume()
    {
        Log.d("SimpleScreen", "we are resuming");
    }

    public void dispose()
    {
        Log.d("SimpleScreen", "we are disposing the game");
    }


}
