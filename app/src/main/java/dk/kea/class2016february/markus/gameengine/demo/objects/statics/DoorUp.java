package dk.kea.class2016february.markus.gameengine.demo.objects.statics;

import dk.kea.class2016february.markus.gameengine.demo.ObjectType;
import dk.kea.class2016february.markus.gameengine.demo.PhysScreen;
import dk.kea.class2016february.markus.gameengine.demo.StaticObject;

public class DoorUp extends StaticObject
{
    public DoorUp(int x, int y, int z, PhysScreen physScreen)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.physScreen = physScreen;

        this.imagePointer = 0;
        this.images = new String[]
                {
                        "DoorOpen.png"
                };

        this.type = ObjectType.doorUp;
        this.xSize = 32;
        this.ySize = 64;
    }
}
