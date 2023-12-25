package site.pointman.chatbot.utill;

import com.google.gson.Gson;
import okhttp3.*;

import java.io.IOException;
import java.util.Map;

public class HttpUtils {

    /**
     * POST METHOD
     * @param url
     * @param headers
     * @param body
     * @param mediaType
     * @return
     */
    public static String post(String url,Map<String, Object> headers, Map<String, Object> body, MediaType mediaType) {
        OkHttpClient client = new OkHttpClient();
        String authorization = "";
        String contentType = "";
        if(headers.containsKey("Authorization")){
            authorization = String.valueOf(headers.get("Authorization"));
        }

        if(headers.containsKey("Content-type")){
            contentType = String.valueOf(headers.get("Content-type"));
        }

        RequestBody requestBody = RequestBody.create(new Gson().toJson(body), mediaType);
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .addHeader("Content-type", contentType)
                .addHeader("Authorization", authorization)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * GET METHOD
     * @param url
     * @param headers
     * @return
     */
    public static String get(String url,Map<String, Object> headers) {
        OkHttpClient client = new OkHttpClient();

        String authorization = "";
        String contentType = "";
        if(headers.containsKey("Authorization")){
            authorization = String.valueOf(headers.get("Authorization"));
        }

        if(headers.containsKey("Content-type")){
            contentType = String.valueOf(headers.get("Content-type"));
        }

        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("Content-type", contentType)
                .addHeader("Authorization", authorization)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
