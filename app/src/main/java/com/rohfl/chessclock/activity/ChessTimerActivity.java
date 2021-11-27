package com.rohfl.chessclock.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rohfl.chessclock.R;

public class ChessTimerActivity extends AppCompatActivity implements NumberPicker.OnValueChangeListener{

    private RelativeLayout blackTimerRl, whiteTimerRl;
    private TextView startStopTv;

    boolean isWhiteTurn = true;
    boolean isStop = false;

    int whiteTime = 0, blackTime = 0;



    Handler whiteHandler;
    Handler blackHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            blackTimerRl = findViewById(R.id.black_timer);
            whiteTimerRl = findViewById(R.id.white_timer);
            startStopTv = findViewById(R.id.start_stop_button);

            updateStatusBarColorMain("#FFFFFF");
            showTimeSelectionDialog();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void showTimeSelectionDialog() {
        try {
            Dialog timeSelectionDialog = new Dialog(this);
            timeSelectionDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            timeSelectionDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            View view = LayoutInflater.from(this).inflate(R.layout.select_time_dialog, null, false);

            timeSelectionDialog.setContentView(view);
            timeSelectionDialog.getWindow().setGravity(Gravity.CENTER);
            timeSelectionDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            timeSelectionDialog.setCancelable(false);
            timeSelectionDialog.setCanceledOnTouchOutside(false);
            timeSelectionDialog.show();
            Window window = timeSelectionDialog.getWindow();
            window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            NumberPicker numberPicker = timeSelectionDialog.findViewById(R.id.number_picker);
            numberPicker.setMinValue(1);
            numberPicker.setMaxValue(30);

            numberPicker.setOnValueChangedListener(this);

            TextView okay_tv = timeSelectionDialog.findViewById(R.id.ok_tv);

            okay_tv.setOnClickListener(v-> {

            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startTimers() {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void removeCallbacks() {
        try {
            if (whiteHandler != null) {
                whiteHandler.removeCallbacksAndMessages(null);
            }
            if (blackHandler != null) {
                blackHandler.removeCallbacksAndMessages(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        try {
            whiteTime = newVal*60;
            blackTime = newVal*60;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    public void updateStatusBarColorMain(String color) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = this.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.parseColor("#3D3D3D"));
                window.setNavigationBarColor(Color.parseColor(color));
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                int systemUiVisibility = this.getWindow().getDecorView().getSystemUiVisibility();
                int flagLightStatusBar = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                systemUiVisibility |= flagLightStatusBar;
                this.getWindow().getDecorView().setSystemUiVisibility(systemUiVisibility);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}