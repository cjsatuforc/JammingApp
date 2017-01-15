package com.example.diefunction.jammingapp;

/**
 * Created by DieFunction on 1/14/2017.
 */
import android.os.AsyncTask;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.io.PrintWriter;
import java.util.Timer;

public class Client extends AsyncTask<Void, Void, Void> {

    String dstAddress;
    int dstPort;
    String cmd;
    Timer timer = new Timer();
    Client(String addr, int port, String cmd) {
        dstAddress = addr;
        dstPort = port;
        this.cmd = cmd;
    }

    @Override
    protected Void doInBackground(Void... arg0) {

        Socket socket = null;

        try {
            socket = new Socket(dstAddress, dstPort);
            PrintWriter send = new PrintWriter(socket.getOutputStream(), true);
            send.print(cmd);
            send.close();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                }
            }
        }
        return null;
    }


}
