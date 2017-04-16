package com.einstens3.ironchef.utilities;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class IOUtils {
    public static void convert(InputStream is, OutputStream os) throws IOException {
        try {
            int read = 0;
            byte[] bytes = new byte[1024];
            while ((read = is.read(bytes)) != -1) {
                os.write(bytes, 0, read);
            }
        } finally {
            is.close();
            os.close();
        }
    }
}
