package dk.kea.class2016february.markus.gameengine;

public abstract class Screen
{
    public final Game game;

    public Screen(Game game)
    {
        this.game = game;
    }

    public abstract void update(float deltaTime);
    public abstract void pause();
    public abstract void resume();
    public abstract void dispose();
}
