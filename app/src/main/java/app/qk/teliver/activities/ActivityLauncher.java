package app.qk.teliver.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioGroup;

import app.qk.teliver.R;
import app.qk.teliver.utils.Constants;
import app.qk.teliver.utils.MPreference;

public class ActivityLauncher extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        final MPreference preference = new MPreference(this);
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radio_user);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                Intent intent;
                if (i == R.id.radio_customer) {
                    intent = new Intent(ActivityLauncher.this, ActivityCustomer.class);
                    preference.storeBoolean(Constants.LOGGED_IN_CUSTOMER, true);
                } else {
                    intent = new Intent(ActivityLauncher.this, ActivityDriver.class);
                    preference.storeBoolean(Constants.LOGGED_IN_CUSTOMER, false);
                }
                preference.storeBoolean(Constants.IS_LOGGED_IN, true);
                startActivity(intent);
            }
        });
    }
}
