package app.qk.teliver;

import android.support.multidex.MultiDexApplication;

import com.teliver.sdk.core.TLog;
import com.teliver.sdk.core.Teliver;

public class MApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        Teliver.init(this, "TELIVER_KEY");
        TLog.setVisible(true);
    }
}
