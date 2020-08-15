package com.juane.remotecontrol.network;

import com.juane.remotecontrol.ui.fragments.MainFragment;

public class ClientManager {
    private boolean connected = false;
    private TcpClient mTcpClient = null;

    public void connect(MainFragment mainFragment, String serverIpAddress, int port){
        try {
            mTcpClient = new TcpClient(serverIpAddress, port);

            new ConnectTask(mainFragment, mTcpClient).execute("");
            setConnected(TcpClient.isConnected());
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void sendVolumeData(int volumeData){
        new Thread(new SentMessageTask(MessageTypes.VOLUME_MESSAGE, String.valueOf(volumeData), mTcpClient)).start();
    }

    public void sendKeyMessage(int keyMessage){
        new Thread(new SentMessageTask(keyMessage, null, mTcpClient)).start();
    }

    public void disconnect(){

        if(TcpClient.isConnected())
        {
            mTcpClient.stopClient();
            setConnected(false);
        }
    }

    public boolean isConnected() {
        return TcpClient.isConnected();
    }

    private void setConnected(boolean connected) {
        this.connected = connected;
    }
}
