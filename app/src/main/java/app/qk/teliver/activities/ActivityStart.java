package app.qk.teliver.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import app.qk.teliver.utils.Constants;
import app.qk.teliver.utils.MPreference;


public class ActivityStart extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MPreference preference = new MPreference(this);
        Class className;
        if (preference.getBoolean(Constants.IS_LOGGED_IN)) {
            if (preference.getBoolean(Constants.LOGGED_IN_CUSTOMER))
                className = ActivityCustomer.class;
            else className = ActivityDriver.class;
        } else className = ActivityLauncher.class;
        startActivity(new Intent(this, className));
        finish();
    }
}
