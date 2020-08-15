package com.juane.remotecontrol.network;

class SentMessageTask implements Runnable
{
    String message;
    private TcpClient tcpClient;
    int messageType;

    public SentMessageTask(int messageType, String message, TcpClient tcpClient){
        this.message = message;
        this.tcpClient = tcpClient;
        this.messageType = messageType;
    }

    @Override
    public void run()
    {
        tcpClient.sendMessage(messageType, message);
    }
}

