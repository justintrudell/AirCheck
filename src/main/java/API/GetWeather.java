package API;

import Helpers.AirCheckConstants;
import Models.Weather;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * Created by jerry on 2016-04-22.
 */
public class GetWeather {
    public static void main(String[] args) throws Exception {
        Weather weather = getWeather(0.0, 10.0);
        System.out.println(weather.getHumidity());
    }

    static OkHttpClient client = new OkHttpClient();

    public static Weather getWeather(double longitude, double latitude) throws Exception {
        String test = CallWeatherAPI(String.format("%s/data/2.5/weather?lat=%s&lon=%s&appid=%s",
                AirCheckConstants.ApiBaseUrl, longitude, latitude, AirCheckConstants.ApiToken));
        JsonParser p = new JsonParser();
        JsonObject result = p.parse(test).getAsJsonObject();
        if(result.has("message") && result.get("message").getAsString().equals("not found")) {
            return null;
        }
        JsonObject weatherObject = result.getAsJsonObject("main");
        Weather weather = new Weather(weatherObject.get("temp").getAsDouble(),
                weatherObject.get("humidity").getAsDouble(), weatherObject.get("pressure").getAsDouble());
        return weather;
    }

    static String CallWeatherAPI(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }
}
