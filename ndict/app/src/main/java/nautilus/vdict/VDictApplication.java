package nautilus.vdict;

import android.app.Application;

import nautilus.vdict.util.FileUtil;

/**
 * Created by davu on 12/26/2015.
 */
public class VDictApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        if(!FileUtil.checkDataExisted(this)) {
            FileUtil.initDataFiles(this);
        }
    }
}
