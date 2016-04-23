package API;

import Helpers.Constants;
import Models.Monoxide;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class GetMonoxide {

    public static void main(String[] args) throws Exception {
        Monoxide monoxide = GetMonoxide(0.0, 10.0);
        System.out.println(monoxide.getValue());
    }
    static OkHttpClient client = new OkHttpClient();

    public static Monoxide GetMonoxide(double longitude, double latitude) throws Exception {
        String test = CallMonoxideAPI(String.format("%s/pollution/v1/co/%s,%s/current.json?appid=%s",
                Constants.ApiBaseUrl, longitude, latitude, Constants.ApiToken));
        JsonParser p = new JsonParser();
        JsonObject result = p.parse(test).getAsJsonObject();
        if(result.has("message") && result.get("message").getAsString().equals("not found")) {
            return null;
        }
        JsonObject monoxideObject = p.parse(test).getAsJsonObject().getAsJsonArray("data").get(0).getAsJsonObject();
        Monoxide monoxide = new Monoxide(monoxideObject.get(Constants.MonoxidePrecision).getAsDouble(),
                monoxideObject.get(Constants.MonoxidePressure).getAsDouble(), monoxideObject.get(Constants.MonoxideValue).getAsDouble());
        return monoxide;
    }



    static String CallMonoxideAPI(String url) throws IOException{
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }
}
