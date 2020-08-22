package com.juane.remotecontrol.network;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.juane.remotecontrol.MainActivity;
import com.juane.remotecontrol.model.MessageTypes;
import com.juane.remotecontrol.model.RemoteConstants;

import java.io.IOException;

public class ConnectTask extends AsyncTask<String, String, Void> {
    private TcpClient mTcpClient;
    private Context context;

    public ConnectTask(Context context, TcpClient tcpClient) {
       this.context = context;
       mTcpClient = tcpClient;
    }

    @Override
    protected Void doInBackground(String... message) {
        mTcpClient.setMessageListener(new TcpClient.OnMessageReceived() {
            @Override
            //here the messageReceived method is implemented
            public void messageReceived(int messageType, String dataReceived) {
                if(messageType == MessageTypes.CONNECT_REQUEST_MESSAGE){
                    publishProgress(RemoteConstants.UPDATE_UI_CONNECT_DATA_MESSAGE);
                }

                Log.i("Debug", "Input server message: " + dataReceived);
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
        if(values[0].contains(RemoteConstants.UPDATE_UI_CONNECT_DATA_MESSAGE)){
            ((MainActivity)context).getMainFragment().updateUIConnect();
            ((MainActivity)context).getPowerFragment().updateUIConnect();
        }
    }

    @Override
    protected void onPostExecute(Void v) {
        ((MainActivity)context).getMainFragment().updateUIDisconnect();
        ((MainActivity)context).getPowerFragment().updateUIDisconnect();
    }
}