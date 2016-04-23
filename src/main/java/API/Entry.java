package API;

import DAO.DataAccessObject;
import Helpers.AirCheckConstants;
import Models.Monoxide;
import Models.UserFeelings;
import Models.Weather;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import spark.ModelAndView;
import spark.template.jade.JadeTemplateEngine;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.get;
import static spark.Spark.staticFileLocation;

/**
 * Created by vishalkuo on 2016-04-22.
 */
public class Entry {
    public static double _longitude;
    public static double _latitude;

    public static void main(String[] args) throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("color_quality", "-1");
        staticFileLocation("/public");
        GetUser.ProcessUserFeelings();

        get("/", (req, res) -> new ModelAndView(map, "index"), new JadeTemplateEngine());

        // Route for forms
        get("/data", (request, response) -> {
            response.redirect("/");
            // Getting humidity value
            String city = request.queryParams("city");
            Weather weather = GetWeather.getWeather(city);
            String humidity = weather != null ? String.valueOf(weather.getHumidity()) : AirCheckConstants.ErrorMsg;
            map.put("humidity", humidity);


            // Getting CO VMR value
            double latitude = weather.getLatitude();
            double longitude = weather.getLongitude();
            System.out.println(latitude);
            System.out.println(longitude);
            Monoxide mon = GetMonoxide.GetMonoxide(longitude, latitude);
            // Multiply by a million to get parts per milllion
            //System.out.println(mon.getValue() * 1000000);
            String quality = mon != null ? Monoxide.ppmToQuality(mon.getValue() * 1000000) : AirCheckConstants.ErrorMsg;
            map.put("quality", quality);
            map.put("color_quality", quality);

            return null;
        });

        get("/getCityCoords", (request, response) -> {
            String city = request.queryParams("city");
            Weather weather = GetWeather.getWeather(city);
            double latitude = weather.getLatitude();
            double longitude = weather.getLongitude();
            System.out.println(latitude);
            System.out.println(longitude);
            JsonObject obj = new JsonObject();
            obj.addProperty("latitude", latitude);
            obj.addProperty("longitude", longitude);
            return obj;
        });

        get("/symptomsform", (req, res) ->{
            map.put("message", "test");
            return new ModelAndView(map, "symptomsform");
        }, new JadeTemplateEngine());

        get("/symptomsentry", (request, response) -> {
            System.out.println(request.body());
            int coughLevel = Integer.valueOf(request.queryParams("coughLevel"));
            int howIsBreath = Integer.valueOf(request.queryParams("howIsBreath"));
            int wheezing = Integer.valueOf(request.queryParams("wheezing"));
            int sneezing = Integer.valueOf(request.queryParams("sneezing"));
            boolean noseBlock = false;
            boolean itchyEyes = false;
            if(request.queryParams("noseBlock") != null) {
                noseBlock = request.queryParams("noseBlock").equals("on");
            }
            if(request.queryParams("itchyEyes") != null) {
                itchyEyes = request.queryParams("itchyEyes").equals("on");
            }
            String city = request.queryParams("city");
            double longitude = 0;
            if(request.queryParams("longitude") != null && !request.queryParams("longitude").isEmpty())
                longitude = Double.parseDouble(request.queryParams("longitude"));
            double latitude = 0;
            if(request.queryParams("latitude") != null && !request.queryParams("latitude").isEmpty())
                latitude = Double.parseDouble(request.queryParams("latitude"));
            UserFeelings feels = new UserFeelings(coughLevel, howIsBreath, wheezing, sneezing, noseBlock,
                    itchyEyes, city, longitude, latitude);
            feels.Save();
            map.put("message", "Thanks for submitting!");
            response.redirect("/");
            return null;
        });

        get("/UserView", (request, response) -> {
            String res = DataAccessObject.processUsers();
            map.put("res", res);
            return new ModelAndView(map, "user_view");
        }, new JadeTemplateEngine());

    }
}
