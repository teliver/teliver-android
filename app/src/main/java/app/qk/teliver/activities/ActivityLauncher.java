package app.qk.teliver.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.MotionLayout;

import app.qk.teliver.R;
import app.qk.teliver.utils.Constants;
import app.qk.teliver.utils.MPreference;

public class ActivityLauncher extends AppCompatActivity {

    private LinearLayout viewOperator, viewConsumer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_launcher);
    }

    @SuppressLint("CutPasteId")
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        viewOperator = findViewById(R.id.image_operator);
        viewConsumer = findViewById(R.id.image_consumer);
        View btnContinue = findViewById(R.id.view_continue);

        viewConsumer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewConsumer.setSelected(!viewConsumer.isSelected());
                viewOperator.setSelected(false);
            }
        });


        viewOperator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewOperator.setSelected(!viewOperator.isSelected());
                viewConsumer.setSelected(false);
            }
        });


        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });

        startMotion();
    }

    private void startMotion() {

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                    MotionLayout motionLayout = findViewById(R.id.start_screen);
                    motionLayout.transitionToEnd();
            }
        }, 1500);
    }

    private void validate() {
        try {
            MPreference preference = new MPreference(this);
            Intent intent;
            if (viewConsumer.isSelected()) {
                intent = new Intent(ActivityLauncher.this, ActivityCustomer.class);
                preference.storeBoolean(Constants.LOGGED_IN_CUSTOMER, true);
            } else if (viewOperator.isSelected()) {
                intent = new Intent(ActivityLauncher.this, ActivityDriver.class);
                preference.storeBoolean(Constants.LOGGED_IN_CUSTOMER, false);
            } else {
                Toast.makeText(this, "Select One to Continue", Toast.LENGTH_SHORT).show();
                return;
            }
            preference.storeBoolean(Constants.IS_LOGGED_IN, true);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
