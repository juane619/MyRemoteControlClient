package com.juane.remotecontrol.ui.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.juane.remotecontrol.R;
import com.juane.remotecontrol.model.MessageTypes;
import com.juane.remotecontrol.model.PowerActionEnum;

import java.util.concurrent.TimeUnit;

public class PowerFragment extends RemoteFragment {
    private static final String INITIAL_TIME = "00:00:00";
    PowerActionEnum selectedAction = PowerActionEnum.POWER_OFF;
    private ImageButton imageButtonPowerOff, imageButtonSleep;
    private TimePicker myTimePicker;
    private TextView textViewCountdown;
    private Button buttonRun, buttonCancel;
    private CountDownTimer countDownTimer;
    private boolean isRunning = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_power, container, false);

        versionText = root.findViewById(R.id.versionTextView);
        imageButtonPowerOff = root.findViewById(R.id.imageButtonPowerOff);
        imageButtonSleep = root.findViewById(R.id.imageButtonSleep);
        myTimePicker = root.findViewById(R.id.timePicker1);
        textViewCountdown = root.findViewById(R.id.textViewCountdown);
        buttonRun = root.findViewById(R.id.buttonRun);
        buttonCancel = root.findViewById(R.id.buttonCancel);

        setVersionTextView();
        setImageButtons();
        setMyTimePicker();
        setTextViewCountdown();
        setActionsButtons();

        return root;
    }

    public void updateUIConnect(){
        enableControls();
    }

    public void updateUIDisconnect(){
        disableControls();
    }

    private void setActionsButtons() {
        buttonRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isRunning) {
                    isRunning = true;

                    long millis = TimeUnit.HOURS.toMillis(myTimePicker.getHour()) + TimeUnit.MINUTES.toMillis(myTimePicker.getMinute());

                    if (millis == 0) {
                        millis = 30000;
                    }

                    if(selectedAction == PowerActionEnum.POWER_OFF)
                        clientManager.sendMessage(MessageTypes.POWER_OFF_MESSAGE, String.valueOf(millis));
                    else if(selectedAction == PowerActionEnum.SLEEP)
                        clientManager.sendMessage(MessageTypes.SUSPEND_OFF_MESSAGE, String.valueOf(millis));

                    myTimePicker.setVisibility(View.INVISIBLE);
                    textViewCountdown.setVisibility(View.VISIBLE);
                    createCountdownTimer(millis).start();
                }
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRunning) {
                    isRunning = false;

                    clientManager.sendMessage(MessageTypes.CANCEL_POWER_ACTION_MESSAGE, null);
                    countDownTimer.cancel();

                    textViewCountdown.setVisibility(View.INVISIBLE);
                    myTimePicker.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private CountDownTimer createCountdownTimer(long millis) {
        countDownTimer = new CountDownTimer(millis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished);
                long minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished);

                String hms = (hours + ":")
                        + (minutes - TimeUnit.HOURS.toMinutes(hours) + ":"
                        + (TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(minutes)));

                textViewCountdown.setText(hms);
            }

            @Override
            public void onFinish() {
                textViewCountdown.setText(INITIAL_TIME);
            }
        };

        return countDownTimer;
    }

    private void setTextViewCountdown() {
        textViewCountdown.setVisibility(View.INVISIBLE);
        textViewCountdown.setText(INITIAL_TIME);
    }

    private void setMyTimePicker() {
        myTimePicker.setIs24HourView(true);
        myTimePicker.setHour(1);
        myTimePicker.setMinute(20);
        myTimePicker.setEnabled(false);
        myTimePicker.setActivated(false);
    }

    private void setImageButtons() {
        imageButtonPowerOff.setEnabled(false);
        imageButtonSleep.setEnabled(false);
        imageButtonPowerOff.setSelected(true);

        imageButtonPowerOff.setOnClickListener(new View.OnClickListener() {
            public void onClick(View button) {
                if (!button.isSelected()) {
                    button.setSelected(!button.isSelected());
                    imageButtonSleep.setSelected(!button.isSelected());

                    if (button.isSelected()) {
                        selectedAction = PowerActionEnum.POWER_OFF;
                    }
                }
            }
        });

        imageButtonSleep.setOnClickListener(new View.OnClickListener() {
            public void onClick(View button) {
                if (!button.isSelected()) {
                    button.setSelected(!button.isSelected());
                    imageButtonPowerOff.setSelected(!button.isSelected());

                    if (button.isSelected()) {
                        selectedAction = PowerActionEnum.SLEEP;
                    }
                }
            }
        });
    }

    private void disableControls() {
        myTimePicker.setEnabled(false);
        myTimePicker.setActivated(false);
        buttonRun.setEnabled(false);
        buttonCancel.setEnabled(false);
        imageButtonPowerOff.setEnabled(false);
        imageButtonSleep.setEnabled(false);
    }

    private void enableControls() {
        myTimePicker.setEnabled(true);
        myTimePicker.setActivated(true);
        buttonRun.setEnabled(true);
        buttonCancel.setEnabled(true);
        imageButtonPowerOff.setEnabled(true);
        imageButtonSleep.setEnabled(true);
    }
}
