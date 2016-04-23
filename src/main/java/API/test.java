package API;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * Created by vishalkuo on 2016-04-22.
 */
public class test {
   public static void main(String args[]){
       Connection c = null;
       Statement stmt = null;
       try {
           Class.forName("org.sqlite.JDBC");
           c = DriverManager.getConnection("jdbc:sqlite:test.db");

           stmt = c.createStatement();
           String sql = "CREATE TABLE Users(" +
                   "cough_level int, " +
                   "breath int, " +
                   "wheezing int, " +
                   "sneezing int, " +
                   "nose_block boolean, " +
                   "itchy_eyes boolean, " +
                   "latitude real, " +
                   "longitude real)";
           stmt.executeUpdate(sql);
           stmt.close();
           c.close();
       } catch ( Exception e ) {
           System.err.println( e.getClass().getName() + ": " + e.getMessage() );
           System.exit(0);
       }
       System.out.println("Opened database successfully");
   }
}
