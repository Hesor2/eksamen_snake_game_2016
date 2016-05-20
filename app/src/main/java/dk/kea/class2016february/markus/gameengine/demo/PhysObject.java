package dk.kea.class2016february.markus.gameengine.demo;

public abstract class PhysObject
{
    public int id;
    public int x;
    public int y;
    public int z;
    public int xSize;
    public int ySize;

    public int imagePointer;
    public String[] images;

    public ObjectType type;

    public PhysScreen physScreen;

    public abstract void destroy();
}
