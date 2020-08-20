package com.juane.remotecontrol.network;

import android.content.Context;

import com.juane.remotecontrol.ui.fragments.MainFragment;

public class ClientManager {
    private boolean connected = false;
    private TcpClient mTcpClient = null;

    public void connect(Context ctx, String serverIpAddress, int port){
        if(!connected) {
            try {
                mTcpClient = new TcpClient(serverIpAddress, port);

                new ConnectTask(ctx, mTcpClient).execute("");
                setConnected(TcpClient.isConnected());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void disconnect(){
        if(connected) {
            if (TcpClient.isConnected()) {
                mTcpClient.stopClient();
                setConnected(false);
            }
        }
    }

    public void sendMessage(int keyMessage, String data){
        new Thread(new SentMessageTask(keyMessage, data, mTcpClient)).start();
    }

    public boolean isConnected() {
        return TcpClient.isConnected();
    }

    private void setConnected(boolean connected) {
        this.connected = connected;
    }
}
