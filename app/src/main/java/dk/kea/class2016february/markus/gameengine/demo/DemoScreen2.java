package dk.kea.class2016february.markus.gameengine.demo;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import java.util.Map;

import dk.kea.class2016february.markus.gameengine.Game;
import dk.kea.class2016february.markus.gameengine.Music;
import dk.kea.class2016february.markus.gameengine.demo.objects.actives.cameras.TrackingCamera;
import dk.kea.class2016february.markus.gameengine.demo.objects.actives.uielements.MusicToggle;
import dk.kea.class2016february.markus.gameengine.demo.objects.statics.*;
import dk.kea.class2016february.markus.gameengine.demo.objects.actives.*;

public class DemoScreen2 extends PhysScreen
{
    int clearColor;

    public DemoScreen2(Game game)
    {
        super(game);
        setMapBoundary(1000, 1000);
        setUIArea(64, Direction.down);
        clearColor = Color.rgb(132, 53, 125);
        music = game.loadMusic("Nihilist.mp3");
        musicPlaying = false;

        //UI
        addUIElement(new MusicToggle(128, 0, this));

        //layer 0
        addStaticObject(new Edge(0, Direction.down, this));

        addStaticObject(new Block(224, 304, 0, this));
        addStaticObject(new Block(192, 304, 0, this));
        for (int i = 64; i < 320; i += 32)
        {
            addStaticObject(new Block(i, 128, 0, this));
        }
        for (int i = 32; i <= 98; i += 32)
        {
            addStaticObject(new Block(i, 640, 0, this));
        }
        addDoor(64, 64, 0);
        addDoor(192, 446, 0);
        addDoor(64, 578, 0);

        addActiveObject(new MovingPlatform(256, 304, 0, Axis.y, 544, this));
        addActiveObject(new MovingPlatform(64, 304, 0, Axis.x, 191, this));
        addActiveObject(new MovingPlatform(0, 128, 0, Axis.y, 336, this));

        player = new Platformer(0, 354, 0, this);
        addActiveObject(player);

        //layer -1
        addStaticObject(new Edge(-1, Direction.down, this));
        addStaticObject(new Edge(-1, Direction.left, this));
        addStaticObject(new Edge(-1, Direction.right, this));
        for (int i = 64; i < 320; i += 32)
        {
            addStaticObject(new Block(i, 128, -1, this));
        }

        for (int i = 64; i < 384; i += 32)
        {
            addStaticObject(new Block(i, 640, -1, this));
        }


//        for (ActiveObject object : activeObjectList)
//        {
//            object.y += 64;
//        }
//
        for (StaticObject object : staticObjectList)
        {
            if (object instanceof Edge) object.y += 64;
        }

        //Camera
        setCamera(new TrackingCamera(player, 1, this));



    }

    public void update(float deltaTime)
    {
        game.clearFramebuffer(clearColor);
        updateObjects();
        paintObjects();
        setPointerReleased();
    }

    public void pause()
    {
        music.pause();
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
        if(musicPlaying) music.play();
        Log.d("SimpleScreen", "we are resuming");
    }

    public void dispose()
    {
        Log.d("SimpleScreen", "we are disposing the game");
    }


}
