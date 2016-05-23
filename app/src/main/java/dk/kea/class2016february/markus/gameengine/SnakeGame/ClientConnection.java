package dk.kea.class2016february.markus.gameengine.SnakeGame;

import android.os.AsyncTask;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientConnection extends Thread
{
    String ipAdress = "192.168.1.122";
    int port = 7778;
    Socket socket;
    DataOutputStream outToServer;

    public void run(){
        Log.d("Fuck dig", "markus");
        threadGo();
    }
    public void threadGo(){
        Thread thread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    socket = new Socket(ipAdress,port);
                    outToServer  =  new DataOutputStream(socket.getOutputStream());
                    outToServer.writeBytes("hej");
                    outToServer.close();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }
}