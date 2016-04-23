package DAO;

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
        while(rs.next()){
            JsonObject jobj = new JsonObject();
            jobj.addProperty("coughLevel", rs.getString("cough_level"));
            jobj.addProperty("breathlevel", rs.getString("breath"));
            jobj.addProperty("wheezing", rs.getString("wheezing"));
            jobj.addProperty("sneezing", rs.getString("sneezing"));
            jobj.addProperty("nose_block", rs.getBoolean("nose_block"));
            jobj.addProperty("itchy_eyes", rs.getBoolean("itchy_eyes"));
            jobj.addProperty("city", rs.getString("city"));
            jarr.add(jobj);
        }
        rs.close();
        stmt.close();
        c.close();
        System.out.println(jarr);
        return jarr.toString();
    }
}
