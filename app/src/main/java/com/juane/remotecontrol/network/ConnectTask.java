package com.juane.remotecontrol.network;

import android.os.AsyncTask;
import android.util.Log;

import com.juane.remotecontrol.ui.fragments.MainFragment;

import java.io.IOException;

public class ConnectTask extends AsyncTask<String, String, TcpClient> {
    private TcpClient mTcpClient;
    private MainFragment mainFragment;

    public ConnectTask(MainFragment mainFragment, TcpClient tcpClient) {
       this.mainFragment = mainFragment;
       mTcpClient = tcpClient;
    }

    @Override
    protected TcpClient doInBackground(String... message) {
        //we create a TCPClient object and
        mTcpClient.setMessageListener(new TcpClient.OnMessageReceived() {
            @Override
            //here the messageReceived method is implemented
            public void messageReceived(String message) {

                if(message.contains("welcome")){
                    publishProgress("update ui connect");
                }

                Log.i("Debug", "Input server message: " + message);
            }
        });

        try {
            mTcpClient.run();
        }catch(IOException e){
            Log.i("TCP", "Connection with server closed");
        } finally {
            //the socket must be closed. It is not possible to reconnect to this socket
            // after it is closed, which means a new socket instance has to be created.
            mTcpClient.stopClient();
        }

        return null;
    }


    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);

        if(values[0].contains("update ui connect")){
            mainFragment.updateUIConnect();
        }
    }

    @Override
    protected void onPostExecute(TcpClient tcpClient) {
        super.onPostExecute(tcpClient);
        mainFragment.updateUIDisconnect();
    }
}