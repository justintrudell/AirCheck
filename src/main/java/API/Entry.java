package API;

import DAO.DAOEntryMethodCaller;
import DAO.DataAccessObject;
import Helpers.AirCheckConstants;
import Helpers.DataGeneration;
import Models.Monoxide;
import Models.UserFeelings;
import Models.Weather;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import spark.ModelAndView;
import spark.template.jade.JadeTemplateEngine;
import twitter4j.*;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static API.GetLocation.CoordsToCity;
import static spark.Spark.get;
import static spark.Spark.staticFileLocation;

/**
 * Created by vishalkuo on 2016-04-22.
 */
public class Entry {

    static OkHttpClient client = new OkHttpClient();

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
            weather.save(city, Double.valueOf(request.queryParams("latitude")), Double.valueOf(request.queryParams("longitude")));
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

        get("/generateUserData", (request, response) -> {
            DataGeneration.GenerateData(50);
            System.out.println("Generated DATA!");
            response.redirect("/");
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

        get("/typeform", (request, response) -> {
            // UserFeelings data members
            int coughLevel = 0;
            int howIsBreath = 0;
            int wheezing = 0;
            int sneezing = 0;
            boolean noseBlock = false;
            boolean itchyEyes = false;
            String city = request.queryParams("city");
            double longitude = 0;
            double latitude = 0;

            response.type("application/json");
            if(request.queryParams("lat") != null) {
                latitude = Double.valueOf(request.queryParams("lat"));
            }
            if(request.queryParams("lon") != null) {
                longitude = Double.valueOf(request.queryParams("lon"));
            }
            Request req = new Request.Builder().url("https://api.typeform.com/v1/form/UlrIrF?key=3c664b74af9b1aadf9d563ead04dd82c7932623e&completed=true").build();
            Response res = client.newCall(req).execute();
            JsonParser parser = new JsonParser();
            JsonObject obj = parser.parse(res.body().string()).getAsJsonObject();
            JsonArray resps = obj.get("responses").getAsJsonArray();
            JsonObject data = resps.get(resps.size() - 1).getAsJsonObject().get("answers").getAsJsonObject();
            System.out.println(data);
            for(Map.Entry<String,JsonElement> entry : data.entrySet()) {
                if(entry.getKey().contains("list_")) {
                    String value = entry.getValue().getAsString();
                    switch(value) {
                        case "Cough": coughLevel = 10;
                        case "Nasal Obstruction" : noseBlock = true;
                        case "Shortness of Breath" : howIsBreath = 10;
                        case "Wheezing" : wheezing = 10;
                        case "Sneezing" : sneezing = 10;
                        case "Itchy Eyes" : itchyEyes = true;
                    }
                }
            }
            UserFeelings user = new UserFeelings(coughLevel, howIsBreath, wheezing, sneezing, noseBlock, itchyEyes,
                    city, longitude, latitude);
            DAOEntryMethodCaller.createTables();
            user.Save();
            return obj;
        });

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
                        String[] vars = status.getText().toLowerCase().split(" ");
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
                ArrayList<String> fields = new ArrayList<>(Arrays.asList(status.getText().toLowerCase().split(" ")));
                int coughLevel = 0;
                if(fields.contains("cough")) {
                    coughLevel = 5;
                    int i = fields.indexOf("cough");
                    if(i > 0)
                    {
                        for(String s : AirCheckConstants.seriousSnynonyms) {
                            if(fields.get(i - 1).contains(s)) {
                                fields.remove(i - 1);
                                coughLevel += 5;
                                break;
                            }
                        }
                    }
                    if(coughLevel <= 5 && i < fields.size()) {
                        for(String s : AirCheckConstants.seriousSnynonyms) {
                            if(fields.get(i + 1).contains(s)) {
                                fields.remove(i + 1);
                                coughLevel += 5;
                                break;
                            }
                        }
                    }
                }

                int howIsBreath = 0;
                if(fields.contains("breath")) {
                    howIsBreath = 5;
                    int i = fields.indexOf("breath");
                    if(i > 0)
                    {
                        for(String s : AirCheckConstants.seriousSnynonyms) {
                            if(fields.get(i - 1).contains(s)) {
                                fields.remove(i - 1);
                                howIsBreath += 5;
                                break;
                            }
                        }
                    }
                    if(howIsBreath <= 5 && i < fields.size()) {
                        for(String s : AirCheckConstants.seriousSnynonyms) {
                            if(fields.get(i + 1).contains(s)) {
                                fields.remove(i + 1);
                                howIsBreath += 5;
                                break;
                            }
                        }
                    }
                }

                int wheezing = 0;
                if(fields.contains("wheezing")) {
                    wheezing = 5;
                    int i = fields.indexOf("wheezing");
                    if(i > 0)
                    {
                        for(String s : AirCheckConstants.seriousSnynonyms) {
                            if(fields.get(i - 1).contains(s)) {
                                fields.remove(i - 1);
                                wheezing += 5;
                                break;
                            }
                        }
                    }
                    if(wheezing <= 5 && i < fields.size()) {
                        for(String s : AirCheckConstants.seriousSnynonyms) {
                            if(fields.get(i + 1).contains(s)) {
                                fields.remove(i + 1);
                                wheezing += 5;
                                break;
                            }
                        }
                    }
                }

                int sneezing = 0;
                if(fields.contains("sneezing")) {
                    sneezing = 5;
                    int i = fields.indexOf("sneezing");
                    if(i > 0)
                    {
                        for(String s : AirCheckConstants.seriousSnynonyms) {
                            if(fields.get(i - 1).contains(s)) {
                                fields.remove(i - 1);
                                sneezing += 5;
                                break;
                            }
                        }
                    }
                    if(sneezing <= 5 && i < fields.size()) {
                        for(String s : AirCheckConstants.seriousSnynonyms) {
                            if(fields.get(i + 1).contains(s)) {
                                fields.remove(i + 1);
                                sneezing += 5;
                                break;
                            }
                        }
                    }
                }

                boolean noseBlock = false;
                if(fields.contains("nose") && fields.contains("block")) {
                    noseBlock = true;
                }

                boolean itchyEyes = false;
                if(fields.contains("itchy") && fields.contains("eyes")) {
                    itchyEyes = true;
                }
                UserFeelings feels = new UserFeelings(coughLevel, howIsBreath, wheezing, sneezing,
                        noseBlock, itchyEyes, city, _longitude, _latitude);
                feels.Save();
                try{
                    Weather weather = GetWeather.getWeather(city);
                    weather.save(city, _latitude, _longitude);
                } catch(Exception e)
                {
                    System.out.println("lol");
                }


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
