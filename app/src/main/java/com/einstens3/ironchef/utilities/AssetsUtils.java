package com.einstens3.ironchef.utilities;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class AssetsUtils {
    public static File copyFileFromAssetToChacheDir(Context context, String filename)
            throws IOException {
        File file = new File(context.getCacheDir(), filename);
        if (!file.exists()) {
            InputStream is = context.getAssets().open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(buffer);
            fos.close();
        }
        return file;
    }

    public static String fromAssetFile(Context context, String filename) throws IOException {
        InputStream in = context.getAssets().open(filename);
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String read;
        while ((read = br.readLine()) != null) {
            sb.append(read);
        }
        br.close();
        return sb.toString();
    }
}
