package API;

import Helpers.AirCheckConstants;
import Models.Monoxide;
import Models.UserFeelings;
import Models.Weather;
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
    public static void main(String[] args){
        Map<String, String> map = new HashMap<>();
        map.put("color_quality", "-1");
        staticFileLocation("/public");

        get("/", (req, res) -> new ModelAndView(map, "index"), new JadeTemplateEngine());

        // Route for forms
        get("/data", (request, response) -> {
            response.redirect("/");

            // Getting CO VMR value
            double latitude = Double.parseDouble(request.queryParams("latitude"));
            double longitude = Double.parseDouble(request.queryParams("longitude"));
            System.out.println(latitude);
            System.out.println(longitude);
            Monoxide mon = GetMonoxide.GetMonoxide(longitude, latitude);
            // Multiply by a billion to get parts per billion
            //System.out.println(mon.getValue() * 1000000);
            String quality = mon != null ? Monoxide.ppmToQuality(mon.getValue() * 1000000) : AirCheckConstants.ErrorMsg;
            map.put("quality", quality);
            map.put("color_quality", quality);

            // Getting humidity value
            Weather weather = GetWeather.getWeather(longitude, latitude);
            String humidity = weather != null ? String.valueOf(weather.getHumidity()) : AirCheckConstants.ErrorMsg;
            map.put("humidity", humidity);
            return null;
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
            boolean noseBlock = Integer.valueOf(request.queryParams("noseBlock")) == 1;
            boolean itchyEyes = Integer.valueOf(request.queryParams("itchyEyes")) == 1;
            double latitude = 0;
            if(request.queryParams("latitude") != null && !request.queryParams(("latitude")).isEmpty())
                latitude = Double.parseDouble(request.queryParams("latitude"));
            double longitude = 0;
            if(request.queryParams("longitude") != null && !request.queryParams(("longitude")).isEmpty())
                latitude = Double.parseDouble(request.queryParams("longitude"));
            UserFeelings feels = new UserFeelings(coughLevel, howIsBreath, wheezing, sneezing, noseBlock, itchyEyes, latitude, longitude);
            feels.Save();
            map.put("message", "Thanks for submitting!");
            response.redirect("/");
            return null;
        });

    }
}
