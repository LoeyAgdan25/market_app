package invest.com.swapp.helper;

import android.content.Context;
import android.os.AsyncTask;
import androidx.annotation.WorkerThread;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import okio.BufferedSource;
import okio.Okio;

public class AdBlocker {
    private static final String AD_HOSTS_FILE = "pgl.yoyo.org.txt";
    private static final Set<String> AD_HOSTS = new HashSet<>();

    public static void init(final Context context) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    loadFromAssets(context);
                } catch (IOException e) {
                    // noop
                }
                return null;
            }
        }.execute();
    }

    @WorkerThread
    private static void loadFromAssets(Context context) throws IOException {
        InputStream stream = context.getAssets().open(AD_HOSTS_FILE);
        BufferedSource buffer = Okio.buffer(Okio.source(stream));
        String line;
        while ((line = buffer.readUtf8Line()) != null) {
            AD_HOSTS.add(line);
        }
        buffer.close();
        stream.close();
    }
}
