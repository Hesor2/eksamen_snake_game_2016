package dk.kea.class2016february.markus.gameengine.demo.objects.statics;

import dk.kea.class2016february.markus.gameengine.demo.Direction;
import dk.kea.class2016february.markus.gameengine.demo.ObjectType;
import dk.kea.class2016february.markus.gameengine.demo.PhysScreen;
import dk.kea.class2016february.markus.gameengine.demo.StaticObject;

public class Edge extends StaticObject
{
    public Edge(int z, Direction direction, PhysScreen physScreen)
    {
        this.z = z;
        switch(direction)
        {
            case up:
                this.x = 0;
                this.y = 0;
                this.images = new String[]
                        {
                                "hEdge.png"
                        };
                this.xSize = 320;
                this.ySize = 32;
                break;
            case down:
                this.x = 0;
                this.y = 448;
                this.images = new String[]
                        {
                                "hEdge.png"
                        };
                this.xSize = 320;
                this.ySize = 32;
                break;
            case left:
                this.x = 0;
                this.y = 32;
                this.images = new String[]
                        {
                                "vEdge.png"
                        };
                this.xSize = 32;
                this.ySize = 416;
                break;
            case right:
                this.x = 288;
                this.y = 32;
                this.images = new String[]
                        {
                                "vEdge.png"
                        };
                this.xSize = 32;
                this.ySize = 416;
                break;
        }
        this.physScreen = physScreen;
        this.imagePointer = 0;
        this.type = ObjectType.block;
    }
}
