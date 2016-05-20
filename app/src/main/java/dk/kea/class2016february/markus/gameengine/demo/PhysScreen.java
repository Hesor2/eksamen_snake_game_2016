package dk.kea.class2016february.markus.gameengine.demo;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dk.kea.class2016february.markus.gameengine.Game;
import dk.kea.class2016february.markus.gameengine.Music;
import dk.kea.class2016february.markus.gameengine.Screen;
import dk.kea.class2016february.markus.gameengine.demo.objects.statics.*;


public abstract class PhysScreen extends Screen
{
    private boolean[] pointerReleased =
            {
                    true, true, true
            };
    public List<ActiveObject> activeObjectList = new ArrayList<>();
    public List<StaticObject> staticObjectList = new ArrayList<>();
    private int objectCount = 0;

    public List<ActiveObject> UIElements = new ArrayList<>();
    private int UICount = 0;

    private Map<String, Bitmap> sprites = new HashMap<>();

    private Rect mapBoundary;
    private Rect UIArea;

    public ActiveObject player;
    public ActiveObject camera;

    public Music music;
    public boolean musicPlaying = false;

    public PhysScreen(Game game)
    {
        super(game);
    }

    public void setUIArea(int size, Direction orientation)
    {
        //left, up, right, bottom
        switch (orientation)
        {
            case left:
                UIArea = new Rect(0, 0, size, 480);
                break;
            case right:
                UIArea = new Rect(320 - size, 0, 320, 480);
                break;
            case up:
                UIArea = new Rect(0, 0, 320, size);
                break;
            case down:
                UIArea = new Rect(0, 480 - size, 320, 480);
                break;
        }
    }

    public boolean isInUIArea(int pointer)
    {
        boolean inUI = false;
        int pointerX = game.getTouchX(pointer);
        int pointerY = game.getTouchY(pointer);
        if (pointerX >= UIArea.left && pointerX <= UIArea.right)
        {
            if (pointerY >= UIArea.top && pointerY <= UIArea.bottom)
            {
                inUI = true;
            }
        }
        return inUI;
    }

    public void addUIElement(ActiveObject activeObject)
    {
        activeObject.x += UIArea.left;
        activeObject.y += UIArea.top;
        if (activeObject.x < UIArea.left && activeObject.x + activeObject.xSize > UIArea.right)
        {
            if (activeObject.y < UIArea.top && activeObject.y + activeObject.ySize > UIArea.bottom)
            {
                throw new IllegalArgumentException("UI Elements must be within assigned UIArea. Top and left corner of UIArea is indexed 0,0");
            }
        }
        activeObject.id = UICount;
        UIElements.add(activeObject);
        UICount++;

    }

    public void setMapBoundary(int xSize, int ySize)
    {
        mapBoundary = new Rect(0, 0, xSize, ySize);
    }

    public Rect getMapBoundary()
    {
        return mapBoundary;
    }

    public void addActiveObject(ActiveObject activeObject)
    {
        if (activeObject.type == ObjectType.player)
        {
            boolean hasPlayer = false;
            for (ActiveObject object : activeObjectList)
            {
                if (object.type == ObjectType.player) hasPlayer = true;
            }
            if (hasPlayer)
            {
                throw new IllegalArgumentException("You can only have a single player-object on screen");
            }
        }
        activeObject.id = objectCount;
        activeObjectList.add(activeObject);
        objectCount++;

    }

    public void addStaticObject(StaticObject staticObject)
    {
        staticObject.id = objectCount;
        staticObjectList.add(staticObject);
        objectCount++;
    }

    public void addDoor(int x, int y, int z)
    {
        addStaticObject(new DoorDown(x, y, z, this));
        addStaticObject(new DoorUp(x, y, z - 1, this));
    }

    public void updateObjects()
    {
        for (ActiveObject object : activeObjectList)
        {
            object.doAction();
        }
        if (camera != null) camera.doAction();
        if (UIElements != null)
        {
            for (ActiveObject object : UIElements)
            {
                object.doAction();
            }
        }
    }

    public void paintObjects()
    {
        if (camera == null)
        {
            for (ActiveObject object : activeObjectList)
            {
                String image = object.images[object.imagePointer];
                if (!sprites.containsKey(image)) sprites.put(image, game.loadBitmap(image));
                game.drawBitmap(sprites.get(image), object.x, object.y);
            }
            for (StaticObject object : staticObjectList)
            {
                String image = object.images[object.imagePointer];
                if (!sprites.containsKey(image)) sprites.put(image, game.loadBitmap(image));
                game.drawBitmap(sprites.get(image), object.x, object.y);
            }
        }
        else
        {
            int cameraLowerEdge = camera.y + camera.ySize;
            int cameraRightEdge = camera.x + camera.xSize;

            if (camera.imagePointer != -1)
            {
                String image = camera.images[camera.imagePointer];
                if (!sprites.containsKey(image)) sprites.put(image, game.loadBitmap(image));
                game.drawBitmap(sprites.get(image), 0, 0);
            }

            for (StaticObject object : staticObjectList)
            {
                String image = object.images[object.imagePointer];
                if (!sprites.containsKey(image)) sprites.put(image, game.loadBitmap(image));

                if (camera.z == object.z)
                {
                    int objectRightEdge = object.x + object.xSize;

                    if ((camera.x <= objectRightEdge && camera.x >= object.x) || (cameraRightEdge <= objectRightEdge && cameraRightEdge >= object.x) || (camera.x <= object.x && cameraRightEdge >= objectRightEdge))
                    {
                        int objectLowerEdge = object.y + object.ySize;
                        if ((camera.y <= objectLowerEdge && camera.y >= object.y) || (cameraLowerEdge <= objectLowerEdge && cameraLowerEdge >= object.y) || (camera.y <= object.y && cameraLowerEdge >= objectLowerEdge))
                        {
                            game.drawBitmap(sprites.get(image), object.x - camera.x, object.y - camera.y);
                        }
                    }
                }
            }

            for (ActiveObject object : activeObjectList)
            {
                String image = object.images[object.imagePointer];
                if (!sprites.containsKey(image)) sprites.put(image, game.loadBitmap(image));

                if (camera.z == object.z)
                {
                    int objectRightEdge = object.x + object.xSize;

                    if ((camera.x <= objectRightEdge && camera.x >= object.x) || (cameraRightEdge <= objectRightEdge && cameraRightEdge >= object.x) || (camera.x <= object.x && cameraRightEdge >= objectRightEdge))
                    {
                        int objectLowerEdge = object.y + object.ySize;
                        if ((camera.y <= objectLowerEdge && camera.y >= object.y) || (cameraLowerEdge <= objectLowerEdge && cameraLowerEdge >= object.y) || (camera.y <= object.y && cameraLowerEdge >= objectLowerEdge))
                        {
                            game.drawBitmap(sprites.get(image), object.x - camera.x, object.y - camera.y);
                        }
                    }
                }
            }
        }
        if (UIElements != null)
        {
            for (ActiveObject object : UIElements)
            {
                String image = object.images[object.imagePointer];
                String[] coordinates;
                if (image.contains("§"))
                {
                    coordinates = image.split("§");
                    if (!sprites.containsKey(coordinates[0]))
                    {
                        sprites.put(image, game.loadBitmap(coordinates[0]));
                    }
                    game.drawBitmap(sprites.get(image), object.x, object.y, Integer.parseInt(coordinates[1]), Integer.parseInt(coordinates[2]),object.xSize, object.ySize);
                }
                else
                {
                    game.drawBitmap(sprites.get(image), object.x, object.y);
                }


            }
        }
    }

    public void setCamera(ActiveObject camera)
    {
        this.camera = camera;
    }

    public int getTouchX(int pointer)
    {
        if (camera == null)
        {
            return game.getTouchX(pointer);
        }
        else
        {
            return game.getTouchX(pointer) + camera.x;
        }
    }

    public int getTouchY(int pointer)
    {
        if (camera == null)
        {
            return game.getTouchY(pointer);
        }
        else
        {
            return game.getTouchY(pointer) + camera.y;
        }
    }

    public boolean getPointerReleased(int pointer)
    {
        return pointerReleased[pointer];
    }

    public void setPointerReleased()
    {
        int length = pointerReleased.length;
        for (int i = 0; i < length; i++)
        {
            if (game.isTouchDown(i))
            {
                pointerReleased[i] = false;
            }
            else
            {
                pointerReleased[i] = true;
            }
        }
    }
}
