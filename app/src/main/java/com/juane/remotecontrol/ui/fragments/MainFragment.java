package com.juane.remotecontrol.ui.fragments;

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

import com.juane.remotecontrol.MainActivity;
import com.juane.remotecontrol.R;
import com.juane.remotecontrol.model.MessageTypes;
import com.juane.remotecontrol.utils.Utils;

import java.util.Objects;

public class MainFragment extends RemoteFragment {
    private Button connectButton;
    private TextView connectionText;
    private TextView seekVolumeProgressText;
    private EditText ipText;
    private EditText portText;

    private SeekBar volumeSeekBar;
    private SeekBar brightnessSeekbar;

    private Button muteButton;
    private Button leftButton;
    private Button rightButton;
    private Button spaceButton;

    private int volumeBack = -1;
    private boolean muted = false;

    public MainFragment() {
        super();
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
        versionText = root.findViewById(R.id.versionTextView);

        volumeSeekBar = root.findViewById(R.id.seekBarVolume);
        seekVolumeProgressText = root.findViewById(R.id.seekVolumeText);

        brightnessSeekbar = root.findViewById(R.id.seekBarBrightness);

        muteButton = root.findViewById(R.id.muteButton);
        leftButton = root.findViewById(R.id.leftButton);
        rightButton = root.findViewById(R.id.rightButton);
        spaceButton = root.findViewById(R.id.spaceButton);

        setVersionTextView();
        setIpInput();
        setConnectButton();
        setVolumeSeekBar();
        setBrightnessSeekBar();

        setMuteButton();
        setLeftButton();
        setRightButton();
        setSpaceButton();

        return root;
    }

    public void updateUIConnect(){
        connectionText.setTextColor(Color.GREEN);
        connectionText.setText("CONNECTED");
        connectButton.setText("DISCONNECT");

        enableControls();
    }

    public void updateUIDisconnect(){
        connectionText.setTextColor(Color.RED);
        connectionText.setText("DISCONNECTED");
        connectButton.setText("CONNECT");
        disableControls();
        connectButton.setEnabled(true);
    }


    private void setIpInput() {
        SharedPreferences sharedPref = Objects.requireNonNull(getActivity()).getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        if(sharedPref.contains(getString(R.string.input_IP_KEY))){
            ipText.setText(sharedPref.getString(getString(R.string.input_IP_KEY), "192.168.0.18"));
        }else{
            sharedPref.edit().putString(getString(R.string.input_IP_KEY), "192.168.0.18").commit();
        }
    }

    private void setSpaceButton() {
        spaceButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                    if(clientManager.isConnected()){
                        clientManager.sendMessage(MessageTypes.KEYSPACE_MESSAGE, null);
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
                    clientManager.sendMessage(MessageTypes.KEYRIGHT_MESSAGE, null);
                }
            }
        });

        rightButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(clientManager.isConnected()){
                    clientManager.sendMessage(MessageTypes.KEYRIGHT_LONG_MESSAGE, null);
                    return true;
                }
                return false;
            }
        });
    }

    private void setLeftButton() {
        leftButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                if(clientManager.isConnected()){
                    clientManager.sendMessage(MessageTypes.KEYLEFT_MESSAGE, null);
                }
            }
        });

        leftButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(clientManager.isConnected()){
                    clientManager.sendMessage(MessageTypes.KEYLEFT_LONG_MESSAGE, null);
                    return true;
                }
                return false;
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
                        clientManager.sendMessage(MessageTypes.VOLUME_MESSAGE, String.valueOf(volumeBack));
                        seekVolumeProgressText.setText(String.valueOf(volumeBack));
                        volumeSeekBar.setProgress(volumeBack);
                        muted = false;
                    }else{
                        muted=true;
                        clientManager.sendMessage(MessageTypes.VOLUME_MESSAGE, String.valueOf(0));
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
                    clientManager.sendMessage(MessageTypes.VOLUME_MESSAGE, String.valueOf(volumeData));
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

    private void setBrightnessSeekBar() {
        brightnessSeekbar.setEnabled(false);

        brightnessSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                System.out.println("Changing progress of brightness bar");

                int brightnessData = seekBar.getProgress();

                if(clientManager.isConnected()){
                    clientManager.sendMessage(MessageTypes.BRIGHTNESS_MESSAGE, String.valueOf(brightnessData));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                System.out.println("Start tracking seek brightness bar");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                System.out.println("Stop tracking seek brightness bar");
            }
        });
    }

    private void setConnectButton() {
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

    private void disconnect() {
        if(clientManager.isConnected()) {
            clientManager.disconnect();
            updateUIDisconnect();
            ((MainActivity) Objects.requireNonNull(getActivity())).getPowerFragment().updateUIDisconnect();
        }
    }

    private void connect() {
        String IPAddress = ipText.getText().toString();

        if (Utils.validateIP(IPAddress)) {
            SharedPreferences sharedPref = Objects.requireNonNull(getActivity()).getSharedPreferences(
                    getString(R.string.preference_file_key), Context.MODE_PRIVATE);
            sharedPref.edit().putString(getString(R.string.input_IP_KEY), ipText.getText().toString()).commit();
            connectButton.setText("CONNECTING...");
            connectButton.setEnabled(false);
            clientManager.connect(getContext(), IPAddress, Integer.parseInt(portText.getText().toString()));
        }
    }

    private void disableControls() {
        volumeSeekBar.setEnabled(false);
        brightnessSeekbar.setEnabled(false);
        muteButton.setEnabled(false);
        leftButton.setEnabled(false);
        rightButton.setEnabled(false);
        spaceButton.setEnabled(false);
    }

    private void enableControls() {
        connectButton.setEnabled(true);
        volumeSeekBar.setEnabled(true);
        brightnessSeekbar.setEnabled(true);
        muteButton.setEnabled(true);
        leftButton.setEnabled(true);
        rightButton.setEnabled(true);
        spaceButton.setEnabled(true);
    }

}
