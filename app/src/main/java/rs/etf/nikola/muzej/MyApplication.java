package rs.etf.nikola.muzej;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

public class MyApplication extends Application {
    private static Context context;

    public void onCreate() {
        super.onCreate();
        SharedPreferences prefs;
        MyApplication.context = getBaseContext();
        prefs = getSharedPreferences("rs.etf.nikola.muzej.MyApplication", MODE_PRIVATE);
        if (prefs.getBoolean("firstrun", true)) {
            try {
                FileOutputStream fos = context.openFileOutput("museum.dat", Context.MODE_PRIVATE);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(null);
                oos.close();
                fos.close();
            } catch (Exception ex) {
                System.err.println(ex.getMessage());
            }
            prefs.edit().putBoolean("firstrun", false).commit();
        }
    }



    public static Context getAppContext() {
        return MyApplication.context;
    }
}
