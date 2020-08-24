package com.juane.remotecontrol.network;

class SentMessageTask implements Runnable
{
    private final String message;
    private final TcpClient tcpClient;
    private final int messageType;

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

