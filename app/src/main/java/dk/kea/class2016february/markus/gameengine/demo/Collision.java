package dk.kea.class2016february.markus.gameengine.demo;

public class Collision
{
    public Direction direction;

    public ObjectType objectType;
    public int objectXSpeed;
    public int objectYSpeed;

    public int objectId;
    public int location;

    public Collision(Direction direction, ObjectType objectType, int objectXSpeed, int objectYSpeed,int objectId, int location)
    {
        this.direction = direction;

        this.objectType = objectType;
        this.objectXSpeed = objectXSpeed;
        this.objectYSpeed = objectYSpeed;

        this.objectId = objectId;
        this.location = location;
    }

    public Collision(Direction direction, ObjectType objectType, int objectId, int location)
    {
        this.direction = direction;

        this.objectType = objectType;
        this.objectXSpeed = 0;
        this.objectYSpeed = 0;

        this.objectId = objectId;
        this.location = location;
    }
}
