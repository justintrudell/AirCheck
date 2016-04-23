package API;

import Helpers.AirCheckConstants;
import Models.Monoxide;
import Models.Weather;
import com.sun.org.apache.xalan.internal.xsltc.compiler.Constants;
import spark.ModelAndView;
import spark.template.jade.JadeTemplateEngine;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.get;

/**
 * Created by vishalkuo on 2016-04-22.
 */
public class Entry {
    public static void main(String[] args){
        Map<String, String> map = new HashMap<>();

        get("/", (req, res) -> new ModelAndView(map, "index"), new JadeTemplateEngine());

        // Route for forms
        get("/data", (request, response) -> {
            response.redirect("/");

            // Getting CO RVM value
            double latitude = Double.parseDouble(request.queryParams("latitude"));
            double longitude = Double.parseDouble(request.queryParams("longitude"));
            System.out.println(latitude);
            System.out.println(longitude);
            Monoxide mon = GetMonoxide.GetMonoxide(longitude, latitude);
            String rvm = mon != null ? String.valueOf(mon.getValue()) : AirCheckConstants.ErrorMsg;
            map.put("rvm", rvm);

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
            boolean itcyEyes = Integer.valueOf(request.queryParams("itchyEyes")) == 1;
            double latitude = Double.parseDouble(request.queryParams("latitude"));
            double longitude = Double.parseDouble(request.queryParams("longitude"));
            UserFeelings feels = new UserFeelings(coughLevel, howIsBreath, wheezing, sneezing, noseBlock, itcyEyes, latitude, longitude);
            map.put("message", "Thanks for submitting!");
            response.redirect("/");
            return null;
        });

    }

    public static void testMonoxide(){
        try{
            Monoxide m = GetMonoxide.GetMonoxide(0.0, 10.0);
            System.out.println(m.getPrecision());
        } catch(Exception e){
            return;
        }
    }

}
