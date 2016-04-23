package API;

import Helpers.AirCheckConstants;
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
        // Casting lat and lon to int so we can match data better
        String test = CallMonoxideAPI(String.format("%s/pollution/v1/co/%s,%s/current.json?appid=%s",
                AirCheckConstants.ApiBaseUrl, (int)latitude, (int)longitude, AirCheckConstants.ApiToken));
        JsonParser p = new JsonParser();
        System.out.println(test);
        JsonObject result = p.parse(test).getAsJsonObject();
        if(!result.has("data")) {
            return null;
        }
        JsonObject monoxideObject = result.getAsJsonArray("data").get(0).getAsJsonObject();
        Monoxide monoxide = new Monoxide(monoxideObject.get(AirCheckConstants.MonoxidePrecision).getAsDouble(),
                monoxideObject.get(AirCheckConstants.MonoxidePressure).getAsDouble(), monoxideObject.get(AirCheckConstants.MonoxideValue).getAsDouble());
        return monoxide;
    }



    static String CallMonoxideAPI(String url) throws IOException{
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }
}
