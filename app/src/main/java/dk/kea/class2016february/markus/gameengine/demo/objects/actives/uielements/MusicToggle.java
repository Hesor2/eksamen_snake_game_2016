package dk.kea.class2016february.markus.gameengine.demo.objects.actives.uielements;

import dk.kea.class2016february.markus.gameengine.demo.ActiveObject;
import dk.kea.class2016february.markus.gameengine.demo.ObjectType;
import dk.kea.class2016february.markus.gameengine.demo.PhysScreen;

public class MusicToggle extends ActiveObject
{
    public MusicToggle(int x, int y, PhysScreen physScreen)
    {
        this.x = x;
        this.y = y;
        this.physScreen = physScreen;

        this.imagePointer = 0;

        this.images = new String[]
                {
//                        "Play.png", "Pause.png"
                        "Buttons.png§0§0", "Buttons.png§64§0"
                };

        this.type = ObjectType.UIButton;
        this.xSize = 64;
        this.ySize = 64;
    }


    @Override
    public void doAction()
    {
        if (physScreen.game.isTouchDown(0))
        {
            int touchX = physScreen.game.getTouchX(0);
            int touchY = physScreen.game.getTouchY(0);
            if ((touchX >= x && touchX <= x+xSize) && (touchY >= y && touchY <= y+ySize))
            {
                if (physScreen.getPointerReleased(0))
                {
                    if (physScreen.musicPlaying)
                    {
                        physScreen.music.pause();
                        physScreen.musicPlaying = false;
                        imagePointer = 0;
                    }
                    else
                    {
                        physScreen.music.play();
                        physScreen.musicPlaying = true;
                        imagePointer = 1;
                    }
                }
            }
        }

    }
}
