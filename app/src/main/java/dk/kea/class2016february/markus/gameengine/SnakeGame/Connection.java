package dk.kea.class2016february.markus.gameengine.SnakeGame;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import dk.kea.class2016february.markus.gameengine.Game;

/**
 * Created by Shonix on 5/23/2016.
 */
public class Connection extends Thread
{
    ConnectionHandler handler;
    String ipAdress = "10.111.180.94";
    int port = 7777;
    Socket socket;
    BufferedReader inFromServer;

    public int id = -1;

    public Connection(ConnectionHandler handler)
    {
        this.handler = handler;
    }

    public boolean isConnected()
    {
        if(socket == null) return false;
        else return true;
    }

    public void run()
    {
        try
        {
            socket = new Socket(ipAdress,port);
            inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            send("LOGIN");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        if(socket != null)
        {
            boolean going = true;
            while (going)
            {
                try
                {
                    String message = inFromServer.readLine();
                    Log.d("From Server", message);
                    String[] params = message.split(String.valueOf(handler.split));
                    synchronized (handler.instructions)
                    {
                        if (params[0].equals("CONFIRM"))
                        {
                            id = Integer.parseInt(params[1]);
                            handler.setConnection(this);
                            Log.d("id", message);
                        }
                        else
                        {
                            handler.instructions.add(message);
                        }
                    }
                } catch (IOException e)
                {
                    going = false;
                    handler.setConnection(null);
                    Log.d("Connection", "closed");
                }
            }
        }
    }

    public void send(String message)
    {
        Command command = new Command(socket, message);
        command.start();
    }

    public void terminate() throws IOException
    {
//        Command command
        socket.close();
    }
}
