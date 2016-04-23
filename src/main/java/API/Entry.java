package API;

import DAO.DAOEntryMethodCaller;
import DAO.DataAccessObject;
import Helpers.AirCheckConstants;
import Helpers.DataGeneration;
import Models.Monoxide;
import Models.UserFeelings;
import Models.Weather;
import com.google.gson.JsonObject;
import spark.ModelAndView;
import spark.template.jade.JadeTemplateEngine;
import twitter4j.*;

import java.util.HashMap;
import java.util.Map;

import static API.GetLocation.CoordsToCity;
import static spark.Spark.get;
import static spark.Spark.staticFileLocation;

/**
 * Created by vishalkuo on 2016-04-22.
 */
public class Entry {

    public static void main(String[] args) throws Exception {
        RunTwitterStream();

        Map<String, String> map = new HashMap<>();
        map.put("color_quality", "-1");
        staticFileLocation("/public");
        // Create
        DAOEntryMethodCaller.createTables();

        get("/", (req, res) -> new ModelAndView(map, "index"), new JadeTemplateEngine());

        // Route for forms
        get("/data", (request, response) -> {
            response.type("application/json");
            // Getting humidity value
            String city = request.queryParams("city");
            Weather weather = GetWeather.getWeather(city);
            String humidity = weather != null ? String.valueOf(weather.getHumidity()) : AirCheckConstants.ErrorMsg;


            // Getting CO VMR value
            double latitude = weather.getLatitude();
            double longitude = weather.getLongitude();
            Monoxide mon = GetMonoxide.GetMonoxide(longitude, latitude);
            // Multiply by a million to get parts per milllion
            String quality = mon != null ? Monoxide.ppmToQuality(mon.getValue() * 1000000) : AirCheckConstants.ErrorMsg;

            JsonObject ret = new JsonObject();

            ret.addProperty("humidity", humidity);
            ret.addProperty("quality", quality);

            return ret;
        });

        get("/getCityCoords", (request, response) -> {
            String city = request.queryParams("city");
            Weather weather = GetWeather.getWeather(city);
            double latitude = weather.getLatitude();
            double longitude = weather.getLongitude();
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

        get("/generateUsers", (request, response) -> {
            response.redirect("/");
            DataGeneration g = new DataGeneration();
            g.GenerateData(1000);
            return null;
        });

        get("/userJSON", "application/json", (request, response) -> {
            response.type("application/json");
            return DataAccessObject.processUsersJson();
        }, new JsonTransformer());

        get("/cityJSON", "application/json", (request, response) -> {
            response.type("application/json");
            return DataAccessObject.processCities();
        }, new JsonTransformer());

    }

    private static void RunTwitterStream() throws Exception {
        TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
        StatusListener listener = new StatusListener() {
            @Override
            public void onStatus(Status status) {
                String city = "";
                double _longitude = 0;
                double _latitude = 0;
                if(status.getGeoLocation() != null) {
                    try {
                        _longitude = status.getGeoLocation().getLongitude();
                        _latitude = status.getGeoLocation().getLatitude();
                        city = CoordsToCity(status.getGeoLocation().getLongitude(), status.getGeoLocation().getLatitude());
                    }
                    catch(Exception e) {
                        e.printStackTrace();
                    }
                }
                else {
                    if(status.getText().contains("#")) {
                        String[] vars = status.getText().split(" ");
                        for(int i = 0; i < vars.length; i++) {
                            if(vars[i].contains("#")) {
                                city = vars[i].replace("#","");
                                try {
                                    Weather w = GetWeather.getWeather(city);
                                    _latitude = w.getLatitude();
                                    _longitude = w.getLongitude();
                                }
                                catch(Exception e)
                                {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
                System.out.println(String.format("%s LONG %s LAT %s CITY", _longitude, _latitude, city));
            }

            @Override
            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
                //System.out.println("Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
            }

            @Override
            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
                System.out.println("Got track limitation notice:" + numberOfLimitedStatuses);
            }

            @Override
            public void onScrubGeo(long userId, long upToStatusId) {
                System.out.println("Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
            }

            @Override
            public void onStallWarning(StallWarning warning) {
                System.out.println("Got stall warning:" + warning);
            }

            @Override
            public void onException(Exception ex) {
                ex.printStackTrace();
            }
        };

        twitterStream.addListener(listener);
        FilterQuery filter = new FilterQuery();
        String[] keywordsArray = { "@AircheckAppsTO" };
        filter.track(keywordsArray);
        twitterStream.filter(filter);
    }
}
