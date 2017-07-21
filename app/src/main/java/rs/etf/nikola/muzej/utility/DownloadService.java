package rs.etf.nikola.muzej.utility;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import rs.etf.nikola.muzej.MyApplication;

public class DownloadService extends IntentService {
    public static final int UPDATE_PROGRESS = 8344;
    int state = 0;

    public DownloadService() {
        super("DownloadService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String urlToDownload = intent.getStringExtra("url");
        ResultReceiver receiver = intent.getParcelableExtra("receiver");
        try {
            URL url = new URL(urlToDownload);
            URLConnection connection = url.openConnection();

            InputStream input = new BufferedInputStream(connection.getInputStream());
            OutputStream output = MyApplication.getAppContext().openFileOutput("museum_new.dat", Context.MODE_PRIVATE);

            int fileLength = connection.getContentLength();

            byte data[] = new byte[1024];
            long total = 0;
            int count;
            while ((count = input.read(data)) != -1) {
                total += count;
                Bundle resultData = new Bundle();
                resultData.putInt("state", state);
                resultData.putInt("progress", (int) (total * 100 / fileLength));
                receiver.send(UPDATE_PROGRESS, resultData);
                output.write(data, 0, count);
            }

            output.flush();
            output.close();
            input.close();
            state = 1;
        } catch (IOException e) {
            state = 2;
            e.printStackTrace();
        }

        Bundle resultData = new Bundle();
        resultData.putInt("state", state);
        resultData.putInt("progress", 100);
        receiver.send(UPDATE_PROGRESS, resultData);
    }
}
