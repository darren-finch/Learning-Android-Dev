package com.example.eggtimer;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.*;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity
{
    private TextView eggTimerTextView;
    private SeekBar timerSeekbar;
    private long eggTimerDuration;
    private Boolean isCounting = false;
    private CountDownTimer countdown;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        eggTimerTextView = findViewById(R.id.timerText);
        timerSeekbar = findViewById(R.id.timeSeekbar);
        timerSeekbar.setMax(100);
        timerSeekbar.setMin(1);
        timerSeekbar.setProgress(1);
        updateTimer(60);

        timerSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                updateTimer(progress * 60);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {
                if(countdown != null)
                    countdown.cancel();
                isCounting = false;
            }
        });
    }

    private void updateTimer(int secondsLeft)
    {
        int minutes = secondsLeft / 60;
        int seconds = secondsLeft - (minutes * 60);

        String minutesLeftText = (minutes < 10 ? "0" : "") + minutes;
        String secondsLeftText = (seconds < 10 ? "0" : "") + seconds;
        if(seconds == 0)
            secondsLeftText = "00";
        if(minutes == 0)
            minutesLeftText = "00";

        eggTimerTextView.setText(minutesLeftText + ':' + secondsLeftText);
    }

    public void startTimer(View view)
    {
        if(!isCounting)
        {
            eggTimerDuration = TimeUnit.MILLISECONDS.convert(timerSeekbar.getProgress(), TimeUnit.MINUTES);
            countdown = new CountDownTimer(eggTimerDuration, 1000)
            {
                public void onTick(long millisUntilFinished)
                {
                    updateTimer((int) millisUntilFinished / 1000);
                }

                public void onFinish()
                {
                    eggTimerTextView.setText("Done!");
                    MediaPlayer airhorn = MediaPlayer.create(getApplicationContext(), R.raw.airhorn);
                    airhorn.start();
                    isCounting = false;
                }
            }.start();

            isCounting = true;
        }
    }
}
