package DAO;

import API.AirCheckTweeter;
import API.AirCheckUser;
import API.GetLocation;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import twitter4j.TwitterException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Created by vishalkuo on 2016-04-23.
 */
public class DataAccessObject {
    private static Connection c = null;
    private static Statement stmt = null;

    public static String processUsers() throws Exception{
        String sql = "SELECT * FROM Users";
        c = DriverManager.getConnection("jdbc:sqlite:userEntries.db");
        stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        JsonArray jarr = new JsonArray();
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        while(rs.next()){
            JsonObject jobj = new JsonObject();
            jobj.addProperty("longitude", rs.getString("longitude"));
            jobj.addProperty("latitude", rs.getString("latitude"));
            jobj.addProperty("weight", rs.getString("intensity"));
            jarr.add(jobj);
        }
        rs.close();
        stmt.close();
        c.close();
        return gson.toJson(jarr);
    }

    public static JsonArray processUsersJson() throws Exception{
        String sql = "SELECT * FROM Users";
        c = DriverManager.getConnection("jdbc:sqlite:userEntries.db");
        stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        JsonArray jarr = new JsonArray();
        while(rs.next()){
            JsonObject jobj = new JsonObject();
            jobj.addProperty("longitude", rs.getString("longitude"));
            jobj.addProperty("latitude", rs.getString("latitude"));
            jobj.addProperty("weight", rs.getString("intensity"));
            jarr.add(jobj);
        }
        rs.close();
        stmt.close();
        c.close();
        return jarr;
    }

    public static void createUsers() throws Exception{
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
        c = DriverManager.getConnection("jdbc:sqlite:userEntries.db");
        stmt = c.createStatement();
        stmt.executeUpdate(sql);
        stmt.close();
        c.close();
    }

    public static void createCities() throws Exception {
        String sql = "CREATE TABLE IF NOT EXISTS Cities (" +
                "city text PRIMARY KEY, " +
                "latitude double DEFAULT 0, " +
                "longitude double DEFAULT 0, " +
                "intensity double DEFAULT 0, " +
                "temp double DEFAULT 0, " +
                "humidity double DEFAULT 0, " +
                "pressure double DEFAULT 0)";

        c = DriverManager.getConnection("jdbc:sqlite:userEntries.db");
        stmt = c.createStatement();
        stmt.executeUpdate(sql);
        stmt.close();
        c.close();
    }

    public static JsonArray processCities() throws Exception {
        String sql = "SELECT * FROM Cities";
        c = DriverManager.getConnection("jdbc:sqlite:userEntries.db");
        stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        JsonArray jarr = new JsonArray();
        while(rs.next()){
            JsonObject jobj = new JsonObject();
            jobj.addProperty("longitude", rs.getDouble("longitude"));
            jobj.addProperty("latitude", rs.getDouble("latitude"));
            jobj.addProperty("temp", rs.getDouble("temp"));
            jobj.addProperty("weight", rs.getDouble("intensity"));
            if(rs.getDouble("intensity") > 150d)
                try{
                    SendTweet(rs.getDouble("intensity"), GetLocation.CoordsToCity(rs.getDouble("longitude"), rs.getDouble("latitude")));
                } catch (TwitterException e){
                    System.out.println("lol");
                }

            jarr.add(jobj);
        }
        rs.close();
        stmt.close();
        c.close();
        return jarr;
    }

    private static void SendTweet(double intensity, String city) throws Exception {
        AirCheckTweeter.TweetMessage(String.format
                ("NOTICE: %s is experiencing frequent reports of poor air quality. Stay indoors if at all possible.", city));
    }
}
