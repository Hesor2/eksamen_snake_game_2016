package dk.kea.class2016february.markus.gameengine.demo;

public class Sprite
{
    String path;

    //default 0
    int xSrc;
    int ySrc;

    public Sprite(String path, int x, int y)
    {
        this.path = path;
        this.xSrc = x;
        this.ySrc = y;
    }

    public Sprite(String path)
    {
        this.path = path;
        this.xSrc = 0;
        this.ySrc = 0;
    }
}
