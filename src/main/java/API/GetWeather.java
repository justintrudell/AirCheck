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
        Weather weather = getWeather("Ottawa");
        System.out.println(weather.getHumidity());
    }

    static OkHttpClient client = new OkHttpClient();

    public static Weather getWeather(String city) throws Exception {
        String test = CallWeatherAPI(String.format("%s/data/2.5/weather?q=%s&appid=%s",
                AirCheckConstants.ApiBaseUrl, city, AirCheckConstants.ApiToken));
        JsonParser p = new JsonParser();
        System.out.println(test);
        JsonObject result = p.parse(test).getAsJsonObject();
        if (!result.has("main")) {
            return null;
        }
        JsonObject weatherMainObj = result.getAsJsonObject("main");
        JsonObject weatherCoordObj = result.getAsJsonObject("coord");
        Weather weather = new Weather(weatherMainObj.get("temp").getAsDouble(),
                weatherMainObj.get("humidity").getAsDouble(), weatherMainObj.get("pressure").getAsDouble(),
                weatherCoordObj.get("lon").getAsDouble(), weatherCoordObj.get("lat").getAsDouble());
        return weather;
    }

    static String CallWeatherAPI(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }
}
