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
import android.widget.Toast;

import com.rohfl.chessclock.R;

public class ChessTimerActivity extends AppCompatActivity implements NumberPicker.OnValueChangeListener {

    private RelativeLayout blackTimerRl, whiteTimerRl;
    private TextView startStopTv, whiteTimerTv, blackTimerTv;

    boolean isWhiteTurn = true;
    boolean isStop = false;

    int whiteTime = 0, blackTime = 0;

    private boolean isGameStarted = false;


    Handler whiteHandler;
    Handler blackHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            whiteTimerRl = findViewById(R.id.white_timer);
            blackTimerRl = findViewById(R.id.black_timer);
            whiteTimerTv = findViewById(R.id.white_timer_tv);
            blackTimerTv = findViewById(R.id.black_timer_tv);
            startStopTv = findViewById(R.id.start_stop_button);

            updateStatusBarColorMain("#FFFFFF");
            showTimeSelectionDialog();

            whiteTime = 60;
            blackTime = 60;

            whiteTimerRl.setOnClickListener(v -> {
                if (isGameStarted)
                    isWhiteTurn = false;
            });

            blackTimerRl.setOnClickListener(v -> {
                if (isGameStarted)
                    isWhiteTurn = true;
            });

            startStopTv.setOnClickListener(v -> {
                if (!isGameStarted) {
                    isGameStarted = true;
                    isStop = false;
                    startStopTv.setText("pause");
                    startTimers();
                } else {
                    if (!isStop) {
                        isStop = true;
                        startStopTv.setText("resume");
                    } else {
                        isStop = false;
                        startStopTv.setText("pause");
                    }
                }
            });

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

            okay_tv.setOnClickListener(v -> {
                timeSelectionDialog.dismiss();
                String time = String.format("%02d:%02d", (whiteTime / 60), (whiteTime % 60));
                whiteTimerTv.setText(time);
                time = String.format("%02d:%02d", (blackTime / 60), (blackTime % 60));
                blackTimerTv.setText(time);
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startTimers() {
        try {
            whiteHandler = new Handler();
            blackHandler = new Handler();

            whiteHandler.postDelayed(
                    new Runnable() {
                        @Override
                        public void run() {
                            if (!isStop) {
                                if (isWhiteTurn) {
                                    whiteTime--;
                                    String time = String.format("%02d:%02d", (whiteTime / 60), (whiteTime % 60));
                                    if (whiteTime == 0) {
                                        Toast.makeText(ChessTimerActivity.this, "Black Won.", Toast.LENGTH_SHORT).show();
                                    }
                                    whiteTimerTv.setText(time);
                                }
                            }
                            if (whiteTime != 0 && blackTime != 0) {
                                whiteHandler.postDelayed(this, 1000);
                            } else {
                                removeCallbacks();
                            }
                        }
                    }
                    , 1000);

            blackHandler.postDelayed(
                    new Runnable() {
                        @Override
                        public void run() {
                            if (!isStop) {
                                if (!isWhiteTurn) {
                                    blackTime--;
                                    String time = String.format("%02d:%02d", (blackTime / 60), (blackTime % 60));
                                    if (blackTime == 0) {
                                        Toast.makeText(ChessTimerActivity.this, "White Won.", Toast.LENGTH_SHORT).show();
                                    }
                                    blackTimerTv.setText(time);
                                }
                            }
                            if (whiteTime != 0 && blackTime != 0) {
                                blackHandler.postDelayed(this, 1000);
                            } else {
                                removeCallbacks();
                            }
                        }
                    }
                    , 1000);

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
            removeCallbacks();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        try {
            whiteTime = newVal * 60;
            blackTime = newVal * 60;
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