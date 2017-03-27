package rs.etf.nikola.muzej.utility;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;

import rs.etf.nikola.muzej.MyApplication;

public class Museum extends LinkedList<Exhibit> {
    public static Museum instance = Museum.loadFromDisk();

    private static Museum loadFromDisk() {
        Museum museum = null;
        try {
            FileInputStream fis = MyApplication.getAppContext().openFileInput("museum.dat");
            ObjectInputStream ois = new ObjectInputStream(fis);
            museum = (Museum) ois.readObject();
            ois.close();
            fis.close();
        }
        catch(Exception ex) {
            System.err.println(ex.getMessage());
        }
        if(museum == null)
            museum = new Museum();
        return museum;
    }

    public static int saveToDisk() {
        try {
            FileOutputStream fos = MyApplication.getAppContext().openFileOutput("museum.dat", Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(Museum.instance);
            oos.close();
            fos.close();
        }
        catch(Exception ex) {
            System.err.println(ex.getMessage());
        }
        return 1;
    }
}
