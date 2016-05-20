package dk.kea.class2016february.markus.gameengine;

/**
 * Created by Markus on 29-02-2016.
 */
public interface TouchHandler
{
    public boolean isTouchDown(int pointer);
    public int getTouchX(int pointer);
    public int getTouchY(int pointer);
}
