package dk.kea.class2016february.markus.gameengine;

public class KeyEventPool extends Pool<MyKeyEvent>
{

    @Override
    protected MyKeyEvent newItem()
    {
        return new MyKeyEvent();
    }
}
