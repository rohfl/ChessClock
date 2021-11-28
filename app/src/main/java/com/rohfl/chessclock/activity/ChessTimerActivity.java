package com.rohfl.chessclock.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rohfl.chessclock.R;

public class ChessTimerActivity extends AppCompatActivity implements NumberPicker.OnValueChangeListener,
        MediaPlayer.OnCompletionListener {

    private RelativeLayout blackTimerRl, whiteTimerRl;
    private TextView startStopTv, whiteTimerTv, blackTimerTv;
    private ImageView stopIv;

    boolean isWhiteTurn = true;
    boolean isStop = false;

    int whiteTime = 0, blackTime = 0;

    private boolean isGameStarted = false;
    private boolean doubleBackToExitPressedOnce = false;

    Handler mainHandler;

    MediaPlayer mediaPlayer;

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
            stopIv = findViewById(R.id.stop_iv);

            mediaPlayer = MediaPlayer.create(this, R.raw.game_over);
            mediaPlayer.setOnCompletionListener(this);

            updateStatusBarColorMain("#FFFFFF");
            showTimeSelectionDialog();

            whiteTime = 60;
            blackTime = 60;

            whiteTimerRl.setOnClickListener(v -> {
                if (isGameStarted && !isStop)
                    isWhiteTurn = false;
            });

            blackTimerRl.setOnClickListener(v -> {
                if (isGameStarted && !isStop)
                    isWhiteTurn = true;
            });

            startStopTv.setOnClickListener(v -> {
                if (!isGameStarted) {
                    isGameStarted = true;
                    isStop = false;
                    startStopTv.setText(getString(R.string.pause));
                    startTimers();
                } else {
                    if (!isStop) {
                        isStop = true;
                        startStopTv.setText(getString(R.string.resume));
                    } else {
                        isStop = false;
                        startStopTv.setText(getString(R.string.pause));
                    }
                }
            });

            stopIv.setOnClickListener(v -> {
                removeCallbacks();
                showTimeSelectionDialog();
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
//            timeSelectionDialog.getWindow().getAttributes().windowAnimations = R.style.SlideUpDownAnim;
            View view = LayoutInflater.from(this).inflate(R.layout.select_time_dialog, null, false);

            timeSelectionDialog.setContentView(view);
            timeSelectionDialog.getWindow().setGravity(Gravity.CENTER);
            timeSelectionDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            timeSelectionDialog.setCancelable(false);
            timeSelectionDialog.setCanceledOnTouchOutside(false);
            timeSelectionDialog.show();

            NumberPicker numberPicker = timeSelectionDialog.findViewById(R.id.number_picker);
            numberPicker.setMinValue(1);
            numberPicker.setMaxValue(30);

            numberPicker.setOnValueChangedListener(this);

            TextView okay_tv = timeSelectionDialog.findViewById(R.id.ok_tv);
            TextView exit_tv = timeSelectionDialog.findViewById(R.id.exit_tv);

            okay_tv.setOnClickListener(v -> {
                timeSelectionDialog.dismiss();
                String time = String.format("%02d:%02d", (whiteTime / 60), (whiteTime % 60));
                whiteTimerTv.setText(time);
                time = String.format("%02d:%02d", (blackTime / 60), (blackTime % 60));
                blackTimerTv.setText(time);
            });

            exit_tv.setOnClickListener(v -> {
                timeSelectionDialog.dismiss();
                finish();
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startTimers() {
        try {
            mainHandler = new Handler();

            mainHandler.postDelayed(
                    new Runnable() {
                        @Override
                        public void run() {
                            if (!isStop) {
                                if (isWhiteTurn) {
                                    whiteTime--;
                                    String time = String.format("%02d:%02d", (whiteTime / 60), (whiteTime % 60));
                                    whiteTimerTv.setText(time);
                                } else {
                                    blackTime--;
                                    String time = String.format("%02d:%02d", (blackTime / 60), (blackTime % 60));
                                    blackTimerTv.setText(time);
                                }
                            }
                            if (whiteTime != 0 && blackTime != 0) {
                                mainHandler.postDelayed(this, 1000);
                            } else {
                                mediaPlayer.start();
                                showWinnerDialog();
                                removeCallbacks();
                            }
                        }
                    }
                    , 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showWinnerDialog() {
        try {
            Dialog winnerDialog = new Dialog(this);
            winnerDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            winnerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
//            winnerDialog.getWindow().getAttributes().windowAnimations = R.style.SlideUpDownAnim;
            View view = LayoutInflater.from(this).inflate(R.layout.winner_dialog, null, false);

            winnerDialog.setContentView(view);
            winnerDialog.getWindow().setGravity(Gravity.CENTER);
            winnerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            winnerDialog.setCancelable(false);
            winnerDialog.setCanceledOnTouchOutside(false);
            winnerDialog.show();

            TextView winnerTv = winnerDialog.findViewById(R.id.winner_tv);
            TextView exitTv = winnerDialog.findViewById(R.id.exit_tv);
            TextView rematchTv = winnerDialog.findViewById(R.id.rematch_tv);

            if (whiteTime == 0) {
                winnerTv.setText(getString(R.string.black_won));
            } else {
                winnerTv.setText(getString(R.string.white_won));
            }

            rematchTv.setOnClickListener(v -> {
                winnerDialog.dismiss();
                showTimeSelectionDialog();
            });

            exitTv.setOnClickListener(v -> {
                winnerDialog.dismiss();
                finish();
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void removeCallbacks() {
        try {
            isWhiteTurn = true;
            isStop = false;

            whiteTime = 60;
            blackTime = 60;

            isGameStarted = false;
            startStopTv.setText(getString(R.string.start));

            if (mainHandler != null) {
                mainHandler.removeCallbacksAndMessages(null);
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
                systemUiVisibility &= ~flagLightStatusBar;
                this.getWindow().getDecorView().setSystemUiVisibility(systemUiVisibility);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        try {
            mediaPlayer.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            finish();
        } else {
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit.", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }
}