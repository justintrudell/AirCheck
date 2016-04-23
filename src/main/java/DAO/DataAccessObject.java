package DAO;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

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
}
