package com.juane.arduino.myapplication.ui.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.juane.arduino.myapplication.R;
import com.juane.arduino.myapplication.network.ClientManager;
import com.juane.arduino.myapplication.network.MessageTypes;
import com.juane.arduino.myapplication.utils.Utils;

public class MainFragment extends Fragment {

    private static ClientManager clientManager;

    private Button connectButton;
    private TextView connectionText;
    private TextView seekVolumeProgressText;
    private EditText ipText;
    private EditText portText;
    private SeekBar volumeSeekBar;

    private Button muteButton;
    private Button leftButton;
    private Button rightButton;
    private Button spaceButton;

    private int volumeBack = -1;
    private boolean muted = false;

    public static MainFragment newInstance() {
        clientManager = new ClientManager();

        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.main_fragment, container, false);

        connectButton = root.findViewById(R.id.buttonConexion);
        connectionText = root.findViewById(R.id.textViewConexion);
        ipText = root.findViewById(R.id.textIp);
        portText = root.findViewById(R.id.textPort);

        volumeSeekBar = root.findViewById(R.id.seekBarVolume);
        seekVolumeProgressText = root.findViewById(R.id.seekVolumeText);

        muteButton = root.findViewById(R.id.muteButton);
        leftButton = root.findViewById(R.id.leftButton);
        rightButton = root.findViewById(R.id.rightButton);
        spaceButton = root.findViewById(R.id.spaceButton);

        setIpInput();
        setButtonConexion();
        setVolumeSeekBar();

        setMuteButton();
        setLeftButton();
        setRightButton();
        setSpaceButton();

        return root;
    }

    private void setIpInput() {
        SharedPreferences sharedPref = getActivity().getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        if(sharedPref.contains(getString(R.string.IP_INPUT_KEY))){
            ipText.setText(sharedPref.getString(getString(R.string.IP_INPUT_KEY), "192.168.0.18"));
        }else{
            sharedPref.edit().putString(getString(R.string.IP_INPUT_KEY), "192.168.0.18").commit();
        }
    }

    private void setSpaceButton() {
        spaceButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                    if(clientManager.isConnected()){
                        clientManager.sendKeyMessage(MessageTypes.KEYSPACE_MESSAGE);
                    }
            }
        });
    }

    private void setRightButton() {
        rightButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                if(clientManager.isConnected()){
                    clientManager.sendKeyMessage(MessageTypes.KEYRIGHT_MESSAGE);
                }
            }
        });
    }

    private void setLeftButton() {
        leftButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                if(clientManager.isConnected()){
                    clientManager.sendKeyMessage(MessageTypes.KEYLEFT_MESSAGE);
                }
            }
        });
    }

    private void setMuteButton() {
        muteButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                if(clientManager.isConnected()){
                    if(muted){
                        clientManager.sendVolumeData(volumeBack);
                        seekVolumeProgressText.setText(String.valueOf(volumeBack));
                        volumeSeekBar.setProgress(volumeBack);
                        muted = false;
                    }else{
                        muted=true;
                        clientManager.sendVolumeData(0);
                        volumeBack = volumeSeekBar.getProgress();
                        seekVolumeProgressText.setText("0");
                        volumeSeekBar.setProgress(0);
                    }


                }
            }
        });
    }

    private void setVolumeSeekBar() {
        seekVolumeProgressText.setText(String.valueOf(volumeSeekBar.getProgress()));
        volumeSeekBar.setEnabled(false);
        volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                System.out.println("Changing progress of seek bar volume");
                seekVolumeProgressText.setText(String.valueOf(seekBar.getProgress()));

                int volumeData = seekBar.getProgress();

                if(clientManager.isConnected()){
                    clientManager.sendVolumeData(volumeData);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                System.out.println("Start tracking seek bar volume");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                System.out.println("Stop tracking seek bar volume");


            }
        });
    }

    private void setButtonConexion() {
        connectButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                if (clientManager.isConnected()) {
                    disconnect();
                } else {
                    connect();
                }
            }
        });
    }

    public void disconnect() {
        clientManager.disconnect();
        updateUIDisconnect();
    }

    private void connect() {
        String IPAddress = ipText.getText().toString();

        if (Utils.validateIP(IPAddress)) {
            SharedPreferences sharedPref = getActivity().getSharedPreferences(
                    getString(R.string.preference_file_key), Context.MODE_PRIVATE);
            sharedPref.edit().putString(getString(R.string.IP_INPUT_KEY), ipText.getText().toString()).commit();
            connectButton.setText("CONNECTING...");
            connectButton.setEnabled(false);
            clientManager.connect(this, IPAddress, Integer.parseInt(portText.getText().toString()));
        }
    }

    public void updateUIConnect(){
        connectionText.setTextColor(Color.GREEN);
        connectionText.setText("CONNECTED");
        connectButton.setText("DISCONNECT");
        connectButton.setEnabled(true);
        enableControls();
    }

    public void updateUIDisconnect(){
        connectionText.setTextColor(Color.RED);
        connectionText.setText("DISCONNECTED");
        connectButton.setText("CONNECT");
        disableControls();
        connectButton.setEnabled(true);
    }

    private void disableControls() {
        volumeSeekBar.setEnabled(false);
    }

    private void enableControls() {
        volumeSeekBar.setEnabled(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}
