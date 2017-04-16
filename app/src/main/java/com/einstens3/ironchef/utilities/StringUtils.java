package com.einstens3.ironchef.utilities;

import java.util.List;

public class StringUtils {
    public static String fromList(List<String> list) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < list.size(); i++) {
            if (i != 0)
                sb.append(", ");
            sb.append(list.get(i));
        }
        return sb.toString();
    }
}
