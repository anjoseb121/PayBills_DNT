package io.branio.paybills;

import android.content.Context;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by anjose on 6/10/17.
 */

class BillsClient {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final String BASE_URL = "http://104.131.120.128";

    private OkHttpClient client;

    public BillsClient() {
        this.client = new OkHttpClient();
    }

    public void saveBill(Bill b, Callback callback) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL).newBuilder();
        urlBuilder.addQueryParameter("v", b.getCompany());
        urlBuilder.addQueryParameter("q", b.getDueDate());
        urlBuilder.addQueryParameter("rsz", b.getMonth());
        urlBuilder.addQueryParameter("rsz", b.getType());
        urlBuilder.addQueryParameter("rsz", String.valueOf(b.getValue()));
        urlBuilder.addQueryParameter("rsz", String.valueOf(b.isPaid()));
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();
        // Get a handler that can be used to post to the main thread
        client.newCall(request).enqueue(callback);
    }

}
