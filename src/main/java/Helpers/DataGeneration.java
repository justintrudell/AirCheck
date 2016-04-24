package Helpers;

import API.GetWeather;
import Models.Coordinate;
import Models.UserFeelings;
import Models.Weather;
import org.parboiled.common.Tuple2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by Justin on 23/04/2016.
 */
public class DataGeneration {

    public static HashMap<String,Coordinate> cityList;
    public static ArrayList<String> cityTitleList = new ArrayList<>();

    public static void GenerateData(int iterations) throws Exception {
        cityList = GetCities();
        try {

            for(int i = 0; i < iterations; i++) {

                int coughLevel = RandInt(0, 10);
                int howIsBreath = RandInt(0, 10);
                int wheezing = RandInt(0, 10);
                int sneezing = RandInt(0, 10);
                boolean _noseBlock = RandInt(0, 1) == 1;
                boolean _itchyEyes = RandInt(0, 1) == 1;
                String city = GetRandomCity();
                double latitude = cityList.get(city).getLatitude();
                double longitude = cityList.get(city).getLongitude();
                UserFeelings u = new UserFeelings(coughLevel, howIsBreath, wheezing, sneezing, _noseBlock,
                        _itchyEyes, city, latitude, longitude);
                u.Save();
                Weather weather = GetWeather.getWeather(city);
                weather.save(city, latitude, longitude);
            }
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
    }

    public static int RandInt(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }

    public static HashMap<String,Coordinate> GetCities() throws Exception {
        HashMap<String,Coordinate> list = new HashMap<>();
        list.put("Toronto", new Coordinate(43.653, -79.383));
        list.put("Boston", new Coordinate(42.360, -71.059));
        list.put("Chicago", new Coordinate(41.878, -87.630));
        list.put("San Fransisco", new Coordinate(37.775, -122.4194));
        list.put("London", new Coordinate(51.507, -0.128));
        list.put("Rome", new Coordinate(41.903, 12.496));
        list.put("Barcelona", new Coordinate(41.385, 2.173));
        list.put("Sydney", new Coordinate(-33.868, 151.207));
        list.put("Atlanta", new Coordinate(33.749, -84.388));
        list.put("Montreal", new Coordinate(45.502, -73.567));
        list.put("Ottawa", new Coordinate(45.422, -75.697));
        list.put("Buffalo", new Coordinate(42.886, -78.878));
        list.put("Pittsburgh", new Coordinate(40.4406, -79.996));
        list.put("Detroit", new Coordinate(42.331, -83.046));
        list.put("Cleveland", new Coordinate(41.499, -81.694));
        list.put("Washington", new Coordinate(38.907, -77.037));
        list.put("Beijing", new Coordinate(39.904, 116.407));
        list.put("Shanghai", new Coordinate(31.2304, 121.4734));
        for(Map.Entry<String,Coordinate> city : list.entrySet()) {
            cityTitleList.add(city.getKey());
        }
        return list;
    }

    private static String GetRandomCity() {
        int rand = RandInt(0, cityTitleList.size() - 1);
        return cityTitleList.get(rand);
    }
}
