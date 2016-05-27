package dk.kea.class2016february.markus.gameengine.SnakeGame;

import android.util.Log;

import java.io.DataOutputStream;
import java.net.Socket;


public class Command extends Thread
{
    Socket socket;
    String message;

    DataOutputStream commandOutput;


    public Command(Socket socket, String message)
    {
        this.socket = socket;
        this.message = message;
    }

    public void run()
    {
        try
        {
            Log.d("To Server", message);
            commandOutput = new DataOutputStream(socket.getOutputStream());
            commandOutput.writeBytes(message + '\n');
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}

