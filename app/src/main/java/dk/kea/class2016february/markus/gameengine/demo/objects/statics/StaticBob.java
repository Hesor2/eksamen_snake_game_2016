package dk.kea.class2016february.markus.gameengine.demo.objects.statics;

import dk.kea.class2016february.markus.gameengine.demo.ObjectType;
import dk.kea.class2016february.markus.gameengine.demo.PhysScreen;
import dk.kea.class2016february.markus.gameengine.demo.StaticObject;

public class StaticBob extends StaticObject
{
    public StaticBob(int x, int y, PhysScreen physScreen)
    {
//        this.id = id;
        this.x = x;
        this.y = y;
        this.physScreen = physScreen;

        this.imagePointer = 0;
        this.images = new String[]
                {
                        "bobRight.png"
                };

        this.type = ObjectType.block;
        this.xSize = 96;
        this.ySize = 118;
    }
}
