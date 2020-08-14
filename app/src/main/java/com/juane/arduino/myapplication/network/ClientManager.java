package com.juane.arduino.myapplication.network;

import com.juane.arduino.myapplication.ui.main.MainFragment;

public class ClientManager {
    boolean connected = false;
    private TcpClient mTcpClient = null;

    public void connect(MainFragment mainFragment, String serverIpAddress, int port){
        try {
            mTcpClient = new TcpClient(serverIpAddress, port);

            new ConnectTask(mainFragment, mTcpClient).execute("");
            setConnected(mTcpClient.isConnected());
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
        return mTcpClient.isConnected();
    }

    private void setConnected(boolean connected) {
        this.connected = connected;
    }
}
