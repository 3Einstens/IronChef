package com.einstens3.ironchef.Utilities;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class HttpUtils {
    public static void download(URL url, File file) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        try {
            connection.setDoInput(true);
            connection.connect();
            InputStream is = connection.getInputStream();
            OutputStream os = new FileOutputStream(file);
            IOUtils.convert(is, os);
        } finally {
            connection.disconnect();
        }
    }
}
