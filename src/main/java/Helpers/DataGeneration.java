package Helpers;

import Models.Coordinate;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Justin on 23/04/2016.
 */
public class DataGeneration {

    public static ArrayList<Coordinate> cityList;

    public void GenerateData(int iterations) throws Exception {

        //CallCitiesAPI();
        cityList = GetCities();
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:userEntries.db");

            for(int i = 0; i < iterations; i++) {

                int coughLevel = RandInt(0,10);
                int howIsBreath = RandInt(0,10);
                int wheezing = RandInt(0,10);
                int sneezing = RandInt(0,10);
                int _noseBlock = RandInt(0,1);
                int _itchyEyes = RandInt(0,1);
                int _intensity = RandInt(0, 10);
                Coordinate coord = GetRandomLongAndLat();
                double longitude = coord.getLongitude();
                double latitude = coord.getLatitude();
                GetRandomLongAndLat();
                stmt = c.createStatement();
                String sql = "CREATE TABLE IF NOT EXISTS Users (" +
                        "cough_level int, " +
                        "breath int, " +
                        "wheezing int, " +
                        "sneezing int, " +
                        "nose_block boolean, " +
                        "itchy_eyes boolean, " +
                        "city text, " +
                        "longitude double, " +
                        "latitude double, " +
                        "intensity double)";
                stmt.executeUpdate(sql);
                stmt = c.createStatement();
                sql = "INSERT INTO Users values(" +
                        coughLevel + "," +
                        howIsBreath + "," +
                        wheezing + "," +
                        sneezing + "," +
                        _noseBlock + "," +
                        _itchyEyes + ",'" +
                        " " + "'," +
                        longitude + "," +
                        latitude + "," +
                        _intensity +
                        ")";
                stmt.executeUpdate(sql);
            }
            stmt.close();
            c.close();
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

    public static ArrayList<Coordinate> GetCities() {
        ArrayList<Coordinate> list = new ArrayList<>();
        list.add(new Coordinate( 43.653, -79.383));
        list.add(new Coordinate( 49.282, -123.120));
        list.add(new Coordinate( 40.712, -74.005));
        list.add(new Coordinate( 34.052, -118.243));
        list.add(new Coordinate( 37.774, -122.419));
        list.add(new Coordinate( 42.360, 71.058));
        list.add(new Coordinate( 41.878, 87.629));
        list.add(new Coordinate( 45.421, 75.697));
        list.add(new Coordinate( 42.331, 83.045));
        return list;
    }

    private static Coordinate GetRandomLongAndLat() {
        Coordinate c = cityList.get(RandInt(0, cityList.size() - 1));
        double lat = c.getLatitude();
        double lon = c.getLongitude();
        lat += RandInt(-400,400) / 100d;
        lon += RandInt(-400,400) / 100d;
        c.setLatitude(lat);
        c.setLongitude(lon);
        return c;
    }

    /*static OkHttpClient client = new OkHttpClient();

    static String CallCitiesAPI() throws IOException {
        String url = "http://gael-varoquaux.info/blog/wp-content/uploads/2008/12/cities.txt";
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        String s = response.body().string().replace("#", "").replace("name","").replace("longitude","").replace("latitude","");

        String pattern = "/\\s\\s+/g, ' '";

        System.out.println(s.replaceAll(pattern, " "));
        return null;
    } */
}
