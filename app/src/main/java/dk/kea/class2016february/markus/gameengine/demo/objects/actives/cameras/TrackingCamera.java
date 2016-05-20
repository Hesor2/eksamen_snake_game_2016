package dk.kea.class2016february.markus.gameengine.demo.objects.actives.cameras;

import android.graphics.Rect;

import dk.kea.class2016february.markus.gameengine.demo.ActiveObject;
import dk.kea.class2016february.markus.gameengine.demo.ObjectType;
import dk.kea.class2016february.markus.gameengine.demo.PhysScreen;

public class TrackingCamera extends ActiveObject
{
    ActiveObject focus;
    Rect boundary;
    public TrackingCamera(ActiveObject focus, int backgroundImage,PhysScreen physScreen)
    {
        this.physScreen = physScreen;

        this.focus = focus;

        this.imagePointer = backgroundImage;
        this.images = new String[]
                {
                        "background3.png", "background2.png"
                };

        this.type = ObjectType.camera;
        this.xSize = 320;
        this.ySize = 480;

        boundary = physScreen.getMapBoundary();
    }


    @Override
    public void doAction()
    {
        int targetX = focus.x + (focus.xSize/2);
        int targetY = focus.y + (focus.ySize/2);

//        x = targetX - (xSize/2);
//        y = targetY - (ySize/2);


        int halfXSize = xSize/2;
        if (targetX - halfXSize < boundary.left) x = boundary.left;
        else if(targetX + halfXSize > boundary.right) x = boundary.right - xSize;
        else x = targetX - halfXSize;

        int halfYSize = ySize/2;
        if (targetY - halfYSize < boundary.top) y = boundary.top;
        else if(targetY + halfYSize > boundary.bottom) y = boundary.bottom - ySize;
        else y = targetY - halfYSize;

        z = focus.z;
    }
}
