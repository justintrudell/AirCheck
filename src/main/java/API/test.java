package API;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * Created by vishalkuo on 2016-04-22.
 */
public class test {
    static OkHttpClient client = new OkHttpClient();
    ≠≠

    static String run(String url) throws IOException{
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }
}
