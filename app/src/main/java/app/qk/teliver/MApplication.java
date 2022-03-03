package app.qk.teliver;

import androidx.multidex.MultiDexApplication;

import io.teliver.sdk.core.TLog;
import io.teliver.sdk.core.Teliver;


public class MApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        Teliver.init(this, "key");
        TLog.setVisible(true);
    }
}
