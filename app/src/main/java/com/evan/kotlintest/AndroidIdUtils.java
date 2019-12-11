package com.evan.kotlintest;

import android.content.Context;
import android.os.Environment;
import android.provider.Settings;

import java.io.*;

public class AndroidIdUtils {
    private static final String filePath = Environment.getExternalStorageDirectory().toString() + "/jmad/android_id.data";
    private static final String idSpliterator = "#_#";

    public static String getAndroidId(Context context, String prdId) {
        File file = new File(filePath);
        String id = null;
        try {
            if (file.exists()) {
                BufferedReader br = new BufferedReader(new FileReader(file));
                for (; ; ) {
                    id = br.readLine();
                    if (id == null) break;
                    if (id.startsWith(prdId)) {
                        return id.split(idSpliterator)[1];
                    }
                }
                br.close();
            } else {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            id = Settings.System.getString(context.getContentResolver(), Settings.System.ANDROID_ID);
            BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
            bw.newLine();
            bw.write(prdId + idSpliterator + id);
            bw.flush();
            bw.close();
        } catch (Exception e) {
        }
        return id;
    }
}
