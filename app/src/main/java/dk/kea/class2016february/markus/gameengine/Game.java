package dk.kea.class2016february.markus.gameengine;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public abstract class Game extends Activity implements Runnable, View.OnKeyListener, SensorEventListener
{
    private Thread mainLoopThread;
    private State state = State.Paused;
    private List<State> stateChanges = new ArrayList<>();
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private Screen screen;
    private Canvas canvas;
    private Bitmap offscreenSurface;
    private boolean[] pressedKeys = new boolean[256];

    private float ratioX = 0;
    private float ratioY = 0;

    private KeyEventPool keyEventPool = new KeyEventPool();
    private List<MyKeyEvent> keyEvents = new ArrayList<>();
    private List<MyKeyEvent> keyEventBuffer = new ArrayList<>();

    private TouchHandler touchHandler;
    private TouchEventPool touchEventPool = new TouchEventPool();
    private List<TouchEvent> touchEvents = new ArrayList<>();
    private List<TouchEvent> touchEventBuffer = new ArrayList<>();

    private Paint paint = new Paint();

    private float[] accelerometer = new float[3];

    private SoundPool soundPool;
    public Music music;

    private int framesPerSecond = -1;

    public abstract Screen createStartScreen();

    protected void onCreate(Bundle instanceBundle)
    {
        super.onCreate(instanceBundle);
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //probably not needed - addFlags() calls setFlags()
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        surfaceView = new SurfaceView(this);
        setContentView(surfaceView);
        surfaceHolder = surfaceView.getHolder();
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        this.soundPool = new SoundPool(20, AudioManager.STREAM_MUSIC, 0);
        screen = createStartScreen();
        if (surfaceView.getWidth() > surfaceView.getHeight())
        {
            setOffscreenSurface(480, 320);
        }
        else
        {
            setOffscreenSurface(320, 480);
        }
        surfaceView.setFocusableInTouchMode(true);
        surfaceView.requestFocus();
        surfaceView.setOnKeyListener(this);
        touchHandler = new MultiTouchHandler(surfaceView, touchEventBuffer, touchEventPool);
        SensorManager manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (manager.getSensorList(Sensor.TYPE_ACCELEROMETER).size() != 0)
        {
            Sensor accelerometer = manager.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
            manager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
        }


    }

    public void setOffscreenSurface(int width, int height)
    {
        if (offscreenSurface != null) offscreenSurface.recycle();
        offscreenSurface = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        canvas = new Canvas(offscreenSurface);
    }

    public void setScreen(Screen newScreen)
    {
        if (this.screen != null) this.screen.dispose();
        this.screen = newScreen;
    }

    public Typeface loadFont(String fileName)
    {
        Typeface font = Typeface.createFromAsset(getAssets(), fileName);
        if(font == null)
        {
            throw new RuntimeException("font did not load sadface ;_;");
        }
        return font;
    }

    public void drawText(Typeface font, String text, int x, int y, int colour, int size)
    {
        paint.setTypeface(font);
        paint.setTextSize(size);
        paint.setColor(colour);
        canvas.drawText(text, x, y+size, paint);
    }

    public Bitmap loadBitmap(String fileName)
    {
        InputStream in = null;
        Bitmap bitmap = null;
        try
        {
            in = getAssets().open(fileName);
            bitmap = BitmapFactory.decodeStream(in);
            if (bitmap == null)
                throw new RuntimeException("Could not get a bitmap from the file" + fileName);
            return bitmap;
        } catch (IOException e)
        {
            throw new RuntimeException("Could not load the file" + fileName);
        } finally
        {
            if (in != null)
            {
                try
                {
                    in.close();
                } catch (IOException e)
                {
                    Log.d("Closing InputStream", "Shit");
                }
            }
        }
    }

    public Music loadMusic(String fileName)
    {
        try
        {
            AssetFileDescriptor assetFileDescriptor = getAssets().openFd(fileName);
            return new Music(assetFileDescriptor);
        } catch (IOException e)
        {
            throw new RuntimeException("Could not load music file: " + fileName);
        }
    }

    public Sound loadSound(String fileName)
    {
        try
        {
            Log.d("Load sound", fileName);
            AssetFileDescriptor assetFileDescriptor = getAssets().openFd(fileName);
            int soundId = soundPool.load(assetFileDescriptor, 0);
            Sound sound = new Sound(soundPool, soundId);
            return sound;
        } catch (IOException e)
        {
            throw new RuntimeException("Could not load soundfile: " + fileName + " BAD ERROR");
        }
    }

    public void clearFramebuffer(int color)
    {
        if (canvas != null) canvas.drawColor(color);
    }

    public int getFramebufferWidth()
    {
        return surfaceView.getWidth();
    }

    public int getFramebufferHeight()
    {
        return surfaceView.getHeight();
    }

    public int getOffscreenWidth()
    {
        return offscreenSurface.getWidth();
    }

    public int getOffscreenHeight()
    {
        return offscreenSurface.getHeight();
    }

    public void drawBitmap(Bitmap bitmap, int x, int y)
    {
        if (canvas != null) canvas.drawBitmap(bitmap, x, y, null);
    }

    Rect src = new Rect();
    Rect dst = new Rect();

    public void drawBitmap(Bitmap bitmap, int x, int y, int srcX, int srcY, int srcWidth, int srcHeight)
    {
        if (canvas == null) return;

        src.left = srcX;
        src.top = srcY;
        src.right = srcX + srcWidth;
        src.bottom = srcY + srcHeight;

        dst.left = x;
        dst.top = y;
        dst.right = x + srcWidth;
        dst.bottom = y + srcHeight;

        canvas.drawBitmap(bitmap, src, dst, null);

    }

    public static Bitmap rotateBitmap(Bitmap source, float angle)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event)
    {
        if (event.getAction() == KeyEvent.ACTION_DOWN) pressedKeys[keyCode] = true;
        else if (event.getAction() == KeyEvent.ACTION_UP) pressedKeys[keyCode] = false;
        return false;
    }

    public boolean isKeyPressed(int keyCode)
    {
        return pressedKeys[keyCode];
    }

    public boolean isTouchDown(int pointer)
    {
        return touchHandler.isTouchDown(pointer);
    }

    public int getTouchX(int pointer)
    {
        if (ratioX == 0 || ratioY > 10000)
        {
            ratioX = (float) offscreenSurface.getWidth() / (float) surfaceView.getWidth();
        }

//        float ratioX = (float)offscreenSurface.getWidth() / (float)surfaceView.getWidth();
        int x = touchHandler.getTouchX(pointer);
        x = (int) (x * ratioX);
        return x;
    }

    public int getTouchY(int pointer)
    {
        if (ratioY == 0 || ratioY > 10000)
        {
            ratioY = (float) offscreenSurface.getHeight() / (float) surfaceView.getHeight();
        }

//        float ratioY = (float)offscreenSurface.getHeight() / (float)surfaceView.getHeight();
        int y = touchHandler.getTouchY(pointer);
        y = (int) (y * ratioY);
        return y;
    }

    private void fillEvents()
    {
        synchronized (keyEventBuffer)
        {
            int stop = keyEventBuffer.size();
            for (int i = 0; i < stop; i++)
            {
                keyEvents.add(keyEventBuffer.get(i));
            }
            keyEventBuffer.clear();
        }
        synchronized (touchEventBuffer)
        {
            int stop = touchEventBuffer.size();
            for (int i = 0; i < stop; i++)
            {
                touchEvents.add(touchEventBuffer.get(i));
            }
            touchEventBuffer.clear();
        }

    }

    private void freeEvents()
    {
        synchronized (keyEvents)
        {
            int stop = keyEvents.size();
            for (int i = 0; i < stop; i++)
            {
                keyEventPool.free(keyEvents.get(i));
            }
        }
        synchronized (touchEvents)
        {
            int stop = touchEvents.size();
            for (int i = 0; i < stop; i++)
            {
                touchEventPool.free(touchEvents.get(i));
            }
        }
    }


    public List<MyKeyEvent> getKeyEvents()
    {
        return keyEvents;
    }

    public List<TouchEvent> getTouchEvents()
    {
        return touchEvents;
    }

    public float[] getAccelerometer()
    {
        return accelerometer;
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {

    }

    public void onSensorChanged(SensorEvent event)
    {
        System.arraycopy(event.values, 0, accelerometer, 0, 3);
    }

    public int getFramerate()
    {
        return framesPerSecond;
    }

    //This is the main method for the game loop
    public void run()
    {
        int frames = 0;
        long lastTime = System.nanoTime();
        long startTime = lastTime;
        long currentTime = lastTime;

        while (true)
        {
            synchronized (stateChanges)
            {
                for (int i = 0; i < stateChanges.size(); i++)
                {
                    state = stateChanges.get(i);
                    if (state == State.Disposed)
                    {
                        if (screen != null) screen.dispose();
                        Log.d("Game", "State is disposed");
                    }
                    else if (state == State.Paused)
                    {
                        if (screen != null) screen.pause();
                        Log.d("Game", "State is paused");
                    }
                    else if (state == State.Resumed)
                    {
                        if (screen != null) screen.resume();
                        state = State.Running;
                        Log.d("Game", "State is resumed -> Running");
                    }
                }
                stateChanges.clear();
            }
            if (state == State.Running)
            {
                if (!surfaceHolder.getSurface().isValid()) continue;
                Canvas physicalCanvas = surfaceHolder.lockCanvas();

                fillEvents();
                currentTime = System.nanoTime();
                if (screen != null) screen.update((currentTime - lastTime) / 1000000000.0f);
                lastTime = currentTime;
                freeEvents();

                src.left = 0;
                src.top = 0;
                src.right = offscreenSurface.getWidth() - 1;
                src.bottom = offscreenSurface.getHeight() - 1;
                dst.left = 0;
                dst.top = 0;
                dst.right = physicalCanvas.getWidth();
                dst.bottom = physicalCanvas.getHeight();

                physicalCanvas.drawBitmap(offscreenSurface, src, dst, null);
                surfaceHolder.unlockCanvasAndPost(physicalCanvas);
                physicalCanvas = null;
            }
            frames = frames + 1;
            if (System.nanoTime() - startTime > 1000000000)
            {
                framesPerSecond = frames;
                frames = 0;
                startTime = System.nanoTime();
            }
        }
    }

    public void onPause()
    {
        super.onPause();
        synchronized (stateChanges)
        {
            if (isFinishing())
            {
                stateChanges.add(stateChanges.size(), State.Disposed);
                ((SensorManager) getSystemService(Context.SENSOR_SERVICE)).unregisterListener(this);
            }
            else
            {
                stateChanges.add(stateChanges.size(), State.Paused);
            }
        }
        try
        {
            mainLoopThread.join();
        } catch (InterruptedException e)
        {

        }
        if (isFinishing()) soundPool.release();
    }

    public void onResume()
    {
        super.onResume();
        mainLoopThread = new Thread(this);
        mainLoopThread.start();
        synchronized (stateChanges)
        {
            stateChanges.add(stateChanges.size(), State.Resumed);
        }
    }


}
