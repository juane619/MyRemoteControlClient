package com.juane.remotecontrol.network;

import android.content.Context;

import com.juane.remotecontrol.model.MessageTypes;
import com.juane.remotecontrol.ui.fragments.MainFragment;

public class ClientManager {
    private TcpClient mTcpClient = null;

    public void connect(Context ctx, String serverIpAddress, int port){
        if(!isConnected()) {
            try {
                mTcpClient = new TcpClient(serverIpAddress, port);

                new ConnectTask(ctx, mTcpClient).execute("");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void disconnect(){
        if(isConnected()) {
            // send message to close connection with server
            sendMessage(MessageTypes.DISCONNECT_REQUEST_MESSAGE, String.valueOf(mTcpClient.getIdClient()));
            //mTcpClient.stopClient();
        }
    }

    public void sendMessage(int keyMessage, String data){
        new Thread(new SentMessageTask(keyMessage, data, mTcpClient)).start();
    }

    public boolean isConnected() {
        return mTcpClient.isConnected();
    }
}
