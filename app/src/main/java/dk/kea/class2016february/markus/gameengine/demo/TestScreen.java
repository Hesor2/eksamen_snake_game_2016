package dk.kea.class2016february.markus.gameengine.demo;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import java.util.Map;

import dk.kea.class2016february.markus.gameengine.Game;
import dk.kea.class2016february.markus.gameengine.demo.objects.actives.Bob2;
import dk.kea.class2016february.markus.gameengine.demo.objects.actives.JumpBob;
import dk.kea.class2016february.markus.gameengine.demo.objects.statics.StaticBob;
import dk.kea.class2016february.markus.gameengine.demo.objects.statics.StaticTest;

public class TestScreen extends PhysScreen
{
    int clearColor = Color.BLUE;

    public TestScreen(Game game)
    {
        super(game);

        addStaticObject(new StaticTest(0, 0, "bobLeft.png", this));
        addStaticObject(new StaticTest(0, 118, "bobRight.png",this));
        addStaticObject(new StaticTest(0, 236, "32x32.png",this));
        addStaticObject(new StaticTest(32, 236, "32x32.png",this));
        addStaticObject(new StaticTest(100, 236, "32x32.png",this));
        addStaticObject(new StaticTest(100, 290, "32x32.png",this));
        addStaticObject(new StaticTest(100, 322, "32x32.png",this));
        addStaticObject(new StaticTest(132, 290, "32x32.png",this));
        addStaticObject(new StaticTest(132, 322, "32x32.png",this));

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
