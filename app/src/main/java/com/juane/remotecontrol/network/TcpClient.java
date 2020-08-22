package com.juane.remotecontrol.network;

import android.util.Log;

import com.juane.remotecontrol.model.MessageTypes;
import com.juane.remotecontrol.model.RemoteConstants;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

class TcpClient {
    private int idClient;
    // while this is true, the server will continue running
    private static boolean mRun = false;
    // sends message received notifications
    private OnMessageReceived mMessageListener = null;
    // used to send messages
    private PrintWriter mBufferOut;
    // used to read messages from the server
    private BufferedReader mBufferIn;

    private final String IPAddress;
    private final int port;
    private Socket socket = null;

    /**
     * Constructor of the class. OnMessagedReceived listens for the messages received from server
     */
    public TcpClient(String IPAddress, int port) {
        this.IPAddress = IPAddress;
        this.port = port;
    }

    public boolean isConnected() {
        return mRun;
    }

    public void setMessageListener(OnMessageReceived listener) {
        this.mMessageListener = listener;
    }

    /**
     * Sends the message entered by client to the server
     *
     * @param message text entered by client
     */
    public void sendMessage(int messageType, String message) {
        if (mBufferOut != null && !mBufferOut.checkError()) {
            mBufferOut.write(messageType);

            mBufferOut.println(message);
            mBufferOut.flush();
        }
    }

    /**
     * Close the connection and release the members
     */
    public void stopClient() {
        Log.i("Debug", "stopClient");

        mRun = false;

        if(!socket.isClosed()){
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (mBufferOut != null) {
            mBufferOut.flush();
            mBufferOut.close();
        }

        mMessageListener = null;
        mBufferIn = null;
        mBufferOut = null;
    }

    public void run() throws IOException {

        mRun = true;

        //here you must put your computer's IP address.
        InetAddress serverAddress = InetAddress.getByName(IPAddress);

        Log.i("TCP Client", "Connecting...");

        //create a socket to make the connection with the server
        socket = new Socket();
        socket.connect(new InetSocketAddress(serverAddress, port), RemoteConstants.SOCKET_CONNECT_TIMEOUT);

        //sends the message to the server
        mBufferOut = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

        //receives the message which the server sends back
        mBufferIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        //in this while the client listens for the messages sent by the server
        while (mRun) {
            final int messageType = mBufferIn.read();

            // disconnected from server
            if(messageType == -1){
                break;
            }
            String dataReceived = mBufferIn.readLine();

            if(messageType == MessageTypes.CONNECT_REQUEST_MESSAGE){
                setIdClient(Integer.valueOf(dataReceived));
            }

            if (mMessageListener != null) {
                //call the method messageReceived from MyActivity class
                mMessageListener.messageReceived(messageType, dataReceived);
            }
        }

        stopClient();
    }

    void setIdClient(int id){
        idClient = id;
    }

    public int getIdClient() {
        return idClient;
    }

    //Declare the interface. The method messageReceived(String message) will must be implemented in the MyActivity
    //class at on asynckTask doInBackground
    public interface OnMessageReceived {
        void messageReceived(int messageType, String dataReceived);
    }
}
