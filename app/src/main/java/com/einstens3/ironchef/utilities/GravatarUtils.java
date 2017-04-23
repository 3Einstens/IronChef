package com.einstens3.ironchef.utilities;

import java.util.Locale;

public class GravatarUtils {
    public static String url(String email, int size) {
        return String.format(Locale.ENGLISH, "http://www.gravatar.com/avatar/%s.jpg?s=%d", MD5Utils.md5Hex(email), size);
    }
}
