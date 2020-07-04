package com.juane.arduino.myapplication.network;

import android.util.Log;
import android.widget.SeekBar;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class TcpClient {
    // while this is true, the server will continue running
    private static boolean mRun = false;
    // message to send to the server
    private String mServerMessage;
    // sends message received notifications
    private OnMessageReceived mMessageListener = null;
    // used to send messages
    private PrintWriter mBufferOut;
    // used to read messages from the server
    private BufferedReader mBufferIn;

    private String IPAddress;
    private int port;
    private SeekBar seekVolumeBar;
    Socket socket = null;

    /**
     * Constructor of the class. OnMessagedReceived listens for the messages received from server
     */
    public TcpClient(String IPAddress, int port) {
        this.IPAddress = IPAddress;
        this.port = port;
        this.seekVolumeBar = seekVolumeBar;

    }

    public static boolean isConnected() {
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

        // send mesage that we are closing the connection
        //sendMessage(Constants.CLOSED_CONNECTION + "Kazy");

        mRun = false;

        if (mBufferOut != null) {
            mBufferOut.flush();
            mBufferOut.close();
        }

        mMessageListener = null;
        mBufferIn = null;
        mBufferOut = null;
        mServerMessage = null;
    }

    public void run() throws IOException {

        mRun = true;

        //here you must put your computer's IP address.
        InetAddress serverAddr = InetAddress.getByName(IPAddress);

        Log.i("TCP Client", "Connecting...");

        //create a socket to make the connection with the server
        socket = new Socket(serverAddr, port);

        //sends the message to the server
        mBufferOut = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

        //receives the message which the server sends back
        mBufferIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        //in this while the client listens for the messages sent by the server
        while (mRun) {
            mServerMessage = mBufferIn.readLine();

            if (mServerMessage != null && mMessageListener != null) {
                //call the method messageReceived from MyActivity class
                mMessageListener.messageReceived(mServerMessage);
            }
        }
        Log.i("RESPONSE FROM SERVER", "Received Message: '" + mServerMessage + "'");

    }

    //Declare the interface. The method messageReceived(String message) will must be implemented in the MyActivity
    //class at on asynckTask doInBackground
    public interface OnMessageReceived {
        void messageReceived(String message);
    }
}
