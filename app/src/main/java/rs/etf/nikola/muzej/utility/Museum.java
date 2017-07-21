package rs.etf.nikola.muzej.utility;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;

import rs.etf.nikola.muzej.MyApplication;

public class Museum extends LinkedList<Exhibit> {
    public static Museum instance = Museum.loadFromDisk();

    public static Museum loadFromDisk() {
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

    public static Museum loadFromDisk(String path) {
        Museum museum = null;
        try {
            FileInputStream fis = new FileInputStream(new File(path));
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

    public static void saveToDisk() {
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
    }

    public static void saveToDisk(String path) {
        try {
            FileOutputStream fos = new FileOutputStream(new File(path, "museum.dat"));
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(Museum.instance);
            oos.close();
            fos.close();
        }
        catch(Exception ex) {
            System.err.println(ex.getMessage());
        }
    }

    public static boolean doesNameExist(String name) {
        for(Exhibit exhibit:instance) {
            if(exhibit.getName().equals(name))
                return true;
        }
        return false;
    }

    public static Exhibit getExhibitByName(String name) {
        for(Exhibit exhibit:instance) {
            if(exhibit.getName().equals(name))
                return exhibit;
        }
        return null;
    }
}
