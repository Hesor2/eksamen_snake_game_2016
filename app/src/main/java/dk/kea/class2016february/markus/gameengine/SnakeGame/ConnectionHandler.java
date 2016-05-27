package dk.kea.class2016february.markus.gameengine.SnakeGame;

import java.io.IOException;
import java.util.ArrayList;

public class ConnectionHandler
{
    char split = 007;

    private Connection connection;

    ArrayList<String> instructions = new ArrayList<>();

    public ConnectionHandler()
    {
        connection = new Connection(this);
        connection.start();
    }

    public void setConnection(Connection connection)
    {
        this.connection = connection;
    }

    public void send(String message)
    {
        connection.send(message);
    }

    public void terminateConnection()
    {
        if (connection != null)
        {
            try
            {
                connection.terminate();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public int getConnectionId()
    {
        return connection.id;
    }

    public boolean isConnected()
    {
        return connection.isConnected();
    }
}
