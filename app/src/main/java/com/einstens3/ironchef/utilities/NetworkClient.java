package com.einstens3.ironchef.utilities;

/**
 * Created by raprasad on 4/8/17.
 */

import com.loopj.android.http.*;

public class NetworkClient {
    private static final String BASE_URL = "https://api.edamam.com/search";
    private static final String APP_ID = "58565136";
    private static final String APP_KEY = "2b2526cd061344b22ed16ae3125007ef";

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }

    public static void getRecipe(String search, int fromNo, int toNo, AsyncHttpResponseHandler handler) {
        String apiUrl = BASE_URL;
        if (search != null && !search.isEmpty()){
            RequestParams params = new RequestParams();
            params.put("q", search);
            params.put("app_id",APP_ID);
            params.put("app_key",APP_KEY);
            params.put("from", fromNo);
            params.put("to", toNo);
            client.get(apiUrl, params, handler);
        }
    }

}