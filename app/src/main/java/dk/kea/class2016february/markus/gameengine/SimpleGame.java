package dk.kea.class2016february.markus.gameengine;

public class SimpleGame extends Game
{
    @Override
    public Screen createStartScreen()
    {
        return new SimpleScreen(this);
    }



}
