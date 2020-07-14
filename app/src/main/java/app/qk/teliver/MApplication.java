package app.qk.teliver;

import androidx.multidex.MultiDexApplication;

import com.teliver.sdk.core.TLog;
import com.teliver.sdk.core.Teliver;

public class MApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        Teliver.init(this,"Teliver key");
        TLog.setVisible(true);
    }
}
