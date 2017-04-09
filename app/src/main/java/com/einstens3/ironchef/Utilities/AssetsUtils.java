package com.einstens3.ironchef.Utilities;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

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
}
